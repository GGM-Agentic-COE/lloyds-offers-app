package com.lloyds.offers.user.application.command;

import com.lloyds.offers.user.domain.model.UserProfile;
import com.lloyds.offers.user.domain.port.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateProfileHandler {

    private final UserProfileRepository repository;

    @Transactional
    public UserProfile handle(String userId, String displayName, List<String> categories,
                              UserProfile.BudgetSensitivity budgetSensitivity, Integer preferredRadiusMeters) {
        UserProfile profile = repository.findByUserId(userId)
                .orElseGet(() -> UserProfile.builder().userId(userId).budgetSensitivity(UserProfile.BudgetSensitivity.MEDIUM).build());

        if (displayName != null) profile.setDisplayName(displayName);
        if (categories != null) profile.setCategories(categories);
        if (budgetSensitivity != null) profile.setBudgetSensitivity(budgetSensitivity);
        if (preferredRadiusMeters != null) profile.setPreferredRadiusMeters(preferredRadiusMeters);

        return repository.save(profile);
    }
}
