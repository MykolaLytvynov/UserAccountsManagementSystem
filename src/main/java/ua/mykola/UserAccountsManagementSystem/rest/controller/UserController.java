package ua.mykola.UserAccountsManagementSystem.rest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.mykola.UserAccountsManagementSystem.exception.ValidationException;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UpdateUserDto;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UserDto;
import ua.mykola.UserAccountsManagementSystem.service.UserService;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable long id) {
        UserDto obtainedUser = userService.getById(id);
        return ResponseEntity.ok(obtainedUser);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto userDto,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMessages);
        }
        UserDto savedUser = userService.save(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") long id,
                                          @Valid @RequestBody UpdateUserDto updateUserDto,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getFieldErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            throw new ValidationException(errorMessages);
        }

        UserDto updatedUser = userService.update(id, updateUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.ok("User was deleted");
    }

}
