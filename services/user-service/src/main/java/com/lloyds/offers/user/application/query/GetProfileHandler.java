package com.lloyds.offers.user.application.query;

import com.lloyds.offers.user.domain.model.UserProfile;
import com.lloyds.offers.user.domain.port.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetProfileHandler {

    private final UserProfileRepository repository;

    public Optional<UserProfile> handle(String userId) {
        return repository.findByUserId(userId);
    }
}
