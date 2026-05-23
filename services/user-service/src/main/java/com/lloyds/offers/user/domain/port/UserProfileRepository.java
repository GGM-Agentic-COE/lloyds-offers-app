package com.lloyds.offers.user.domain.port;

import com.lloyds.offers.user.domain.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByUserId(String userId);
    void deleteByUserId(String userId);
}
