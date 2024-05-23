package ua.mykola.UserAccountsManagementSystem.testEntities;

import ua.mykola.UserAccountsManagementSystem.entity.Gender;
import ua.mykola.UserAccountsManagementSystem.entity.User;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UpdateUserDto;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UserDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserUtil {

    public static UserDto getBohnJoDto() {
        return UserDto.builder()
                .username("BohnJo")
                .birthDate(LocalDate.now().minusYears(20))
                .gender(Gender.MALE.name())
                .build();
    }

    public static User getBohnJoPersisted() {
        return User.builder()
                .id(1L)
                .username("BohnJo")
                .birthDate(LocalDate.now().minusYears(20))
                .gender(Gender.MALE)
                .accountCreation(LocalDateTime.now())
                .build();
    }

    public static UserDto getBohnJoPersistedDto() {
        return UserDto.fromEntity(getBohnJoPersisted());
    }

    public static UpdateUserDto getUpdatedBohnJoDto() {
        return UpdateUserDto.builder()
                .birthDate(LocalDate.now().minusYears(19))
                .gender(Gender.FEMALE.name())
                .build();
    }

    public static User getUpdatedBohnJoPersisted() {
        return User.builder()
                .id(1l)
                .username("BohnJo")
                .birthDate(LocalDate.now().minusYears(19))
                .gender(Gender.FEMALE)
                .accountCreation(LocalDateTime.now().minusHours(1))
                .build();
    }

    public static UserDto getUpdatedBohnJoPersistedDto() {
        return UserDto.fromEntity(getUpdatedBohnJoPersisted());
    }
}
