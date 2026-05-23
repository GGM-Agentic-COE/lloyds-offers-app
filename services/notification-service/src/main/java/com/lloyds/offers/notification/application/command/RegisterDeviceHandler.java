package com.lloyds.offers.notification.application.command;

import com.lloyds.offers.notification.domain.model.DeviceToken;
import com.lloyds.offers.notification.domain.port.DeviceTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterDeviceHandler {

    private final DeviceTokenRepository repository;

    public RegisterDeviceHandler(DeviceTokenRepository repository) {
        this.repository = repository;
    }

    public DeviceToken handle(DeviceToken device) {
        return repository.save(device);
    }

    public void deregister(String id) {
        repository.findById(id).ifPresent(d -> {
            d.setActive(false);
            repository.save(d);
        });
    }
}
