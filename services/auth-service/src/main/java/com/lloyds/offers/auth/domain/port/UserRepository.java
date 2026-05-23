package com.lloyds.offers.auth.domain.port;

import com.lloyds.offers.auth.domain.model.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByIdentifier(String identifier);
    Optional<User> findById(UUID id);
    User save(User user);
}
