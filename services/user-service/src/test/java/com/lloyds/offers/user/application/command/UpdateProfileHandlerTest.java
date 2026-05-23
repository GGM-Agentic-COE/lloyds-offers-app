package com.lloyds.offers.user.application.command;

import com.lloyds.offers.user.domain.model.UserProfile;
import com.lloyds.offers.user.domain.port.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProfileHandlerTest {

    @Mock
    private UserProfileRepository repository;

    @InjectMocks
    private UpdateProfileHandler handler;

    @Test
    void shouldCreateNewProfileWhenNotExists() {
        when(repository.findByUserId("user-1")).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile result = handler.handle("user-1", "Alice", List.of("food", "travel"),
                UserProfile.BudgetSensitivity.HIGH, 3000);

        assertThat(result.getUserId()).isEqualTo("user-1");
        assertThat(result.getDisplayName()).isEqualTo("Alice");
        assertThat(result.getCategories()).containsExactly("food", "travel");
        assertThat(result.getBudgetSensitivity()).isEqualTo(UserProfile.BudgetSensitivity.HIGH);
        assertThat(result.getPreferredRadiusMeters()).isEqualTo(3000);
        verify(repository).save(any());
    }

    @Test
    void shouldUpdateExistingProfile() {
        UserProfile existing = UserProfile.builder()
                .id("id-1").userId("user-1").displayName("Old").budgetSensitivity(UserProfile.BudgetSensitivity.LOW).build();
        when(repository.findByUserId("user-1")).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UserProfile result = handler.handle("user-1", "New", null, null, null);

        assertThat(result.getDisplayName()).isEqualTo("New");
        assertThat(result.getBudgetSensitivity()).isEqualTo(UserProfile.BudgetSensitivity.LOW);
    }
}
