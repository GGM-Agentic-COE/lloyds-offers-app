/**
 * Step definitions for EP-01 Auth & Registration tests
 * Framework: Cucumber.js + Appium (mobile) + Axios (API)
 */

import { Given, When, Then, Before, After } from '@cucumber/cucumber';
import { expect } from 'chai';
import { api, resetState, getOtpFromTestHarness } from '../support/api-client';
import { app, waitForScreen } from '../support/app-driver';

let state = {};

Before(function () {
  state = {};
});

After(async function () {
  // Cleanup: delete test user if created
  if (state.userId && state.accessToken) {
    await api('DELETE', `/api/v1/users/me`, null, state.accessToken).catch(() => {});
  }
});

// ═══════════════════════════════════════════════════════════
// GIVEN steps
// ═══════════════════════════════════════════════════════════

Given('the auth-service API is running', async function () {
  const res = await api('GET', '/health');
  expect(res.status).to.equal(200);
});

Given('the app is on the registration screen', async function () {
  await app.launchApp();
  await waitForScreen('registration');
});

Given('no account exists for {string}', async function (identifier) {
  state.identifier = identifier;
  // Ensure clean state via test harness
  await api('DELETE', `/test/users?identifier=${encodeURIComponent(identifier)}`).catch(() => {});
});

Given('an account already exists for {string}', async function (identifier) {
  state.identifier = identifier;
  await api('POST', '/api/v1/auth/register', {
    identifier, identifierType: identifier.includes('@') ? 'email' : 'phone', deviceId: 'test-device'
  });
});

Given('I have registered {int} accounts from this device in the last hour', async function (count) {
  for (let i = 0; i < count; i++) {
    await api('POST', '/api/v1/auth/register', {
      identifier: `+44770090100${i}`, identifierType: 'phone', deviceId: 'rate-limit-device'
    });
  }
  state.deviceId = 'rate-limit-device';
});

Given('a user has registered with {string} and status is {string}', async function (identifier, status) {
  const res = await api('POST', '/api/v1/auth/register', {
    identifier, identifierType: identifier.includes('@') ? 'email' : 'phone', deviceId: 'test-device'
  });
  state.userId = res.data.data.userId;
  state.identifier = identifier;
});

Given('an OTP has been sent', async function () {
  // OTP is sent during registration — retrieve from test harness
  state.otp = await getOtpFromTestHarness(state.identifier);
});

Given('the user has completed OTP verification', async function () {
  const otp = await getOtpFromTestHarness(state.identifier);
  const res = await api('POST', '/api/v1/auth/verify-otp', { userId: state.userId, code: otp });
  state.accessToken = res.data.data.accessToken;
  state.refreshToken = res.data.data.refreshToken;
});

Given('a verified user exists with biometric enabled', async function () {
  // Setup via test harness
  const setup = await api('POST', '/test/users/verified', { biometricEnabled: true });
  state.userId = setup.data.userId;
  state.accessToken = setup.data.accessToken;
  state.refreshToken = setup.data.refreshToken;
});

Given('the user\'s access token has expired', function () {
  state.accessToken = 'expired-token';
});

Given('the network is unavailable', async function () {
  await app.setNetworkCondition('offline');
});

// ═══════════════════════════════════════════════════════════
// WHEN steps
// ═══════════════════════════════════════════════════════════

When('I enter {string} in the identifier field', async function (value) {
  await app.typeInField('identifier-input', value);
});

When('I tap the {string} button', async function (buttonLabel) {
  await app.tapButton(buttonLabel);
});

When('I enter the correct 6-digit OTP', async function () {
  const otp = state.otp || await getOtpFromTestHarness(state.identifier);
  await app.enterOtp(otp);
});

When('I enter {string} as the OTP', async function (code) {
  await app.enterOtp(code);
});

When('{int} seconds elapse without entering the code', async function (seconds) {
  await app.wait(seconds * 1000);
});

When('I enter an incorrect OTP {int} times', async function (count) {
  for (let i = 0; i < count; i++) {
    await app.enterOtp(`00000${i}`);
    await app.tapButton('Verify');
    await app.wait(500);
  }
});

When('I tap {string} {int} times within {int} minutes', async function (button, count, _minutes) {
  for (let i = 0; i < count; i++) {
    await app.tapButton(button);
    await app.wait(1000);
  }
});

When('I select the {string} option', async function (option) {
  await app.tapElement(`option-${option.toLowerCase().replace(/ /g, '-')}`);
});

When('I select {string} and tap {string}', async function (option, button) {
  await app.tapElement(`option-${option.toLowerCase().replace(/ /g, '-')}`);
  await app.tapButton(button);
});

When('I grant {string} permission', async function (_permission) {
  await app.grantOsPermission();
});

When('I deny the OS location permission', async function () {
  await app.denyOsPermission();
});

When('the OS biometric prompt succeeds', async function () {
  await app.simulateBiometricSuccess();
});

// ═══════════════════════════════════════════════════════════
// THEN steps
// ═══════════════════════════════════════════════════════════

Then('the API responds with status {int}', async function (status) {
  expect(state.lastResponse.status).to.equal(status);
});

Then('the response contains a userId', function () {
  expect(state.lastResponse.data.data.userId).to.be.a('string');
  state.userId = state.lastResponse.data.data.userId;
});

Then('the response contains verificationRequired as true', function () {
  expect(state.lastResponse.data.data.verificationRequired).to.be.true;
});

Then('the response contains an accessToken', function () {
  expect(state.lastResponse.data.data.accessToken).to.be.a('string');
  state.accessToken = state.lastResponse.data.data.accessToken;
});

Then('the response contains a refreshToken', function () {
  expect(state.lastResponse.data.data.refreshToken).to.be.a('string');
  state.refreshToken = state.lastResponse.data.data.refreshToken;
});

Then('I am navigated to the OTP verification screen', async function () {
  await waitForScreen('otp-verification');
});

Then('I am navigated to the biometric enrollment screen', async function () {
  await waitForScreen('biometric-enrollment');
});

Then('I am navigated to the location consent screen', async function () {
  await waitForScreen('consent-location');
});

Then('I am navigated to the transaction consent screen', async function () {
  await waitForScreen('consent-transaction');
});

Then('I am on the main app Offers tab', async function () {
  await waitForScreen('offers-tab');
});

Then('the inline error {string} is displayed', async function (message) {
  const error = await app.getElementText('field-error');
  expect(error).to.include(message);
});

Then('the error message {string} is displayed', async function (message) {
  const error = await app.getVisibleError();
  expect(error).to.include(message);
});

Then('the {string} button is disabled', async function (buttonLabel) {
  const disabled = await app.isButtonDisabled(buttonLabel);
  expect(disabled).to.be.true;
});

Then('the user status is updated to {string}', async function (status) {
  const user = await api('GET', `/test/users/${state.userId}`);
  expect(user.data.status).to.equal(status);
});

Then('the API records consent with type {string} and decision {string}', async function (type, decision) {
  const res = await api('GET', '/api/v1/auth/consent', null, state.accessToken);
  const consent = res.data.data.consents.find(c => c.consentType === type);
  expect(consent.decision).to.equal(decision);
});

Then('the user record has biometric_enabled = {word}', async function (value) {
  const user = await api('GET', '/api/v1/users/me', null, state.accessToken);
  expect(user.data.data.biometricEnabled).to.equal(value === 'true');
});

Then('the Retry-After header is present', function () {
  expect(state.lastResponse.headers['retry-after']).to.exist;
});
