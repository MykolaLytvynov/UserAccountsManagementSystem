package ua.mykola.UserAccountsManagementSystem.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.mykola.UserAccountsManagementSystem.entity.User;
import ua.mykola.UserAccountsManagementSystem.exception.DuplicateException;
import ua.mykola.UserAccountsManagementSystem.exception.NotFoundException;
import ua.mykola.UserAccountsManagementSystem.exception.ValidationException;
import ua.mykola.UserAccountsManagementSystem.repository.UserRepository;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UpdateUserDto;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UserDto;
import ua.mykola.UserAccountsManagementSystem.testEntities.UserUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final String DUPLICATED_USERNAME_MESSAGE = "Username exists";
    private final String NOT_FOUND_MESSAGE = "User by id:101 was not found";
    private final String VALIDATION_GENDER_MESSAGE = "Such gender doesn't exist";
    private final Long NON_EXISTENT_ID = 101l;

    @Test
    @DisplayName("Creating user")
    void givenUserToSave_whenSaveUser_thenRepositoryIsCalled() {
        //given
        UserDto userToSaveDto = UserUtil.getBohnJoDto();
        given(userRepository.save(any(User.class)))
                .willReturn(UserUtil.getBohnJoPersisted());

        //when
        UserDto savedUser = userService.save(userToSaveDto);

        //then
        assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Creating user with duplicated username")
    void givenUserWithDuplicatedUsernameToSave_whenSaveUser_thenExceptionIsThrown() {
        //given
        UserDto userToSaveDto = UserUtil.getBohnJoDto();
        given(userRepository.existsByUsername(any(String.class)))
                .willReturn(true);

        //when
        DuplicateException ex = assertThrows(DuplicateException.class,
                () -> userService.save(userToSaveDto));

        //then
        assertEquals(DUPLICATED_USERNAME_MESSAGE, ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Creating user with incorrect gender")
    void givenUserWithIncorrectGenderToSave_whenSaveUser_thenExceptionIsThrown() {
        //given
        UserDto userToSaveDto = UserUtil.getBohnJoDto();
        userToSaveDto.setGender("Non-existent gender");

        //when
        ValidationException ex = assertThrows(ValidationException.class,
                () -> userService.save(userToSaveDto));

        //then
        assertEquals(VALIDATION_GENDER_MESSAGE, ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Updating user")
    void givenUserToUpdate_whenUpdateUser_thenRepositoryIsCalled() {
        //given
        UpdateUserDto updatedFieldsUserDto = UserUtil.getUpdatedBohnJoDto();
        User oldUser = UserUtil.getBohnJoPersisted();
        User updatedUser = UserUtil.getUpdatedBohnJoPersisted();
        given(userRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(oldUser));
        given(userRepository.save(any(User.class)))
                .willReturn(updatedUser);

        //when
        UserDto obtainedUser = userService.update(1l, updatedFieldsUserDto);

        //then
        assertNotNull(obtainedUser);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Updating non-existent user")
    void givenNonExistentUserToUpdate_whenUpdateUser_thenExceptionIsThrown() {
        //given
        UpdateUserDto updatedFieldsUserDto = UserUtil.getUpdatedBohnJoDto();
        given(userRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(null));

        //when
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.update(NON_EXISTENT_ID, updatedFieldsUserDto));

        //then
        assertEquals(NOT_FOUND_MESSAGE, ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Updating user with incorrect gender")
    void givenUserWithIncorrectGenderToUpdate_whenUpdateUser_thenExceptionIsThrown() {
        //given
        UpdateUserDto updatedFieldsUserDto = UserUtil.getUpdatedBohnJoDto();
        updatedFieldsUserDto.setGender("Non-existent gender");

        //when
        ValidationException ex = assertThrows(ValidationException.class,
                () -> userService.update(1, updatedFieldsUserDto));

        //then
        assertEquals(VALIDATION_GENDER_MESSAGE, ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Getting user by id")
    void givenId_whenGetUserById_thenUserIsReturned() {
        //given
        Optional<User> user = Optional.of(UserUtil.getBohnJoPersisted());
        given(userRepository.findById(any(Long.class)))
                .willReturn(user);

        //when
        UserDto obtainedUser = userService.getById(1l);

        //then
        assertNotNull(obtainedUser);
        verify(userRepository, times(1)).findById(any(Long.class));
    }

    @Test
    @DisplayName("Getting non-existent user")
    void givenNonExistentId_whenGetUserById_thenExceptionIsThrown() {
        //given
        given(userRepository.findById(any(Long.class)))
                .willReturn(Optional.ofNullable(null));

        //when
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.getById(NON_EXISTENT_ID));

        //then
        assertEquals(NOT_FOUND_MESSAGE, ex.getMessage());
    }

    @Test
    @DisplayName("Deleting user by id")
    void givenId_whenDeleteUserById_thenRepositoryIsCalled() {
        //given
        given(userRepository.existsById(any(Long.class)))
                .willReturn(true);

        //when
        userService.delete(1l);

        //then
        verify(userRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    @DisplayName("Deleting non-existent user")
    void givenNonExistentId_whenDeleteUserById_thenExceptionIsThrown() {
        //given
        given(userRepository.existsById(any(Long.class)))
                .willReturn(false);

        //when
        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> userService.delete(NON_EXISTENT_ID));

        //then
        assertEquals(NOT_FOUND_MESSAGE, ex.getMessage());
    }
}
