package ua.mykola.UserAccountsManagementSystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.mykola.UserAccountsManagementSystem.entity.Gender;
import ua.mykola.UserAccountsManagementSystem.entity.User;
import ua.mykola.UserAccountsManagementSystem.exception.DuplicateException;
import ua.mykola.UserAccountsManagementSystem.exception.NotFoundException;
import ua.mykola.UserAccountsManagementSystem.exception.ValidationException;
import ua.mykola.UserAccountsManagementSystem.repository.UserRepository;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UpdateUserDto;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public UserDto save(UserDto userDto) {
        validateGender(userDto.getGender());

        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new DuplicateException("Username exists");
        }

        User user = userDto.toEntity();
        user.setAccountCreation(LocalDateTime.now());
        userRepository.save(user);
        return UserDto.fromEntity(user);
    }

    @Override
    public UserDto getById(Long id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User by id:" + id + " was not found"));
        return UserDto.fromEntity(foundUser);
    }

    @Override
    public UserDto update(long id, UpdateUserDto updateUserDto) {
        if (updateUserDto.getGender() != null) {
            validateGender(updateUserDto.getGender());
        }
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User by id:" + id + " was not found"));

        if (updateUserDto.getBirthDate() != null) {
            foundUser.setBirthDate(updateUserDto.getBirthDate());
        }
        if (updateUserDto.getGender() != null) {
            foundUser.setGender(Gender.valueOf(updateUserDto.getGender()));
        }
        userRepository.save(foundUser);

        return UserDto.fromEntity(foundUser);
    }

    @Override
    public void delete(long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User by id:" + id + " was not found");
        }
        userRepository.deleteById(id);
    }

    private void validateGender(String gender) {
        if (Arrays.stream(Gender.values())
                .noneMatch(gen -> gender.equals(gen.name()))) {
            throw new ValidationException("Such gender doesn't exist");
        }
    }
}
