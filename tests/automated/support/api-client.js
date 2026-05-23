/**
 * Test support utilities — API client and app driver abstractions
 * Used by all step definitions across epics
 */

import axios from 'axios';

const BASE_URL = process.env.API_BASE_URL || 'https://staging-api.offers.lloyds.com/api/v1';
const TEST_HARNESS_URL = process.env.TEST_HARNESS_URL || 'https://staging-api.offers.lloyds.com/test';

/**
 * Make an API call with optional auth token
 */
export async function api(method, path, body = null, token = null) {
  const headers = {
    'Content-Type': 'application/json',
    'X-Correlation-Id': crypto.randomUUID(),
  };
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const config = { method, url: `${BASE_URL}${path}`, headers, validateStatus: () => true };
  if (body) config.data = body;

  return axios(config);
}

/**
 * Retrieve OTP from test harness (intercepts SMS/email in staging)
 */
export async function getOtpFromTestHarness(identifier) {
  const res = await axios.get(`${TEST_HARNESS_URL}/otp?identifier=${encodeURIComponent(identifier)}`);
  return res.data.code;
}

/**
 * Reset test state (cleanup between scenarios)
 */
export async function resetState() {
  // Called by Before hook if needed
}
