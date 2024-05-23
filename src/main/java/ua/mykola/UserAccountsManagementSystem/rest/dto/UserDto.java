package ua.mykola.UserAccountsManagementSystem.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ua.mykola.UserAccountsManagementSystem.entity.Gender;
import ua.mykola.UserAccountsManagementSystem.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only letters or numbers")
    private String username;

    @NotBlank(message = "Gender is required")
    private String gender;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    private LocalDateTime accountCreation;

    private Integer age;


    public User toEntity() {
        return User.builder()
                .id(id)
                .username(username)
                .birthDate(birthDate)
                .gender(Gender.valueOf(gender))
                .accountCreation(accountCreation)
                .build();
    }

    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .age(Period.between(user.getBirthDate(), LocalDate.now()).getYears())
                .accountCreation(user.getAccountCreation())
                .birthDate(user.getBirthDate())
                .gender(user.getGender().name())
                .build();
    }
}
