package ua.mykola.UserAccountsManagementSystem.service;

import ua.mykola.UserAccountsManagementSystem.rest.dto.UpdateUserDto;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UserDto;

public interface UserService {

    UserDto save(UserDto userDto);

    UserDto getById(Long id);

    UserDto update(long id, UpdateUserDto updateUserDto);

    void delete(long id);
}
