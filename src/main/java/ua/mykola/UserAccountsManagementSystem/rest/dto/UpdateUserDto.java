package ua.mykola.UserAccountsManagementSystem.rest.dto;

import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserDto {
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private String gender;
}
