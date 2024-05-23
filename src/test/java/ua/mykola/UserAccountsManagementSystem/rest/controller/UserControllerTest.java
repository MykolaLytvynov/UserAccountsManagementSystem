package ua.mykola.UserAccountsManagementSystem.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ua.mykola.UserAccountsManagementSystem.exception.NotFoundException;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UpdateUserDto;
import ua.mykola.UserAccountsManagementSystem.rest.dto.UserDto;
import ua.mykola.UserAccountsManagementSystem.service.UserService;
import ua.mykola.UserAccountsManagementSystem.testEntities.UserUtil;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private final String NOT_FOUND_MESSAGE = "User by id:101 was not found";
    private final String FUTURE_BIRTH_DATE_MESSAGE = "Birth date must be in the past";

    @Test
    @DisplayName("Creating user")
    void givenUserDto_whenCreateUser_thenSuccessResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getBohnJoDto();
        UserDto createdUserDto = UserUtil.getBohnJoPersistedDto();
        given(userService.save(any(UserDto.class))).willReturn(createdUserDto);

        //when
        ResultActions result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(createdUserDto.getUsername())));
    }

    @Test
    @DisplayName("Creating user with incorrect username")
    void givenUserDtoWithIncorrectUsername_whenCreateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getBohnJoDto();
        userToSave.setUsername("$$$$$WE");
        String errorMessage = "Username must contain only letters or numbers";


        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Creating user with a future birth date")
    void givenUserDtoWithFutureBirthDate_whenCreateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getBohnJoDto();
        userToSave.setBirthDate(LocalDate.now().plusYears(1));


        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(FUTURE_BIRTH_DATE_MESSAGE)));
    }

    @Test
    @DisplayName("Creating user with empty username")
    void givenUserDtoWithEmptyUsername_whenCreateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getBohnJoDto();
        userToSave.setUsername(null);
        String errorMessage = "Username is required";


        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Creating user with empty gender")
    void givenUserDtoWithEmptyGender_whenCreateUser_thenErrorResponse() throws Exception {
        //given
        UserDto userToSave = UserUtil.getBohnJoDto();
        userToSave.setGender(null);
        String errorMessage = "Gender is required";

        //when
        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToSave)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(errorMessage)));
    }

    @Test
    @DisplayName("Getting user by id")
    void givenId_whenGetById_thenSuccessResponse() throws Exception {
        //given
        UserDto user = UserUtil.getBohnJoPersistedDto();
        given(userService.getById(any(long.class))).willReturn(user);

        //when
        ResultActions result = mockMvc.perform(get("/users/1"));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(user.getUsername())));
    }

    @Test
    @DisplayName("Getting user by non-existent id")
    void givenNonExistentId_whenGetById_thenErrorResponse() throws Exception {
        //given
        given(userService.getById(any(long.class))).willThrow(new NotFoundException(NOT_FOUND_MESSAGE));

        //when
        ResultActions result = mockMvc.perform(get("/users/101"));

        //then
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.NOT_FOUND.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(NOT_FOUND_MESSAGE)));
    }

    @Test
    @DisplayName("Updating user")
    public void givenUserDto_whenUpdateUser_thenSuccessResponse() throws Exception {
        //given
        UpdateUserDto userToUpdate = UserUtil.getUpdatedBohnJoDto();
        UserDto updatedUserDto = UserUtil.getUpdatedBohnJoPersistedDto();
        given(userService.update(anyLong(), any(UpdateUserDto.class))).willReturn(updatedUserDto);

        //when
        ResultActions result = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToUpdate)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", CoreMatchers.is(updatedUserDto.getUsername())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender", CoreMatchers.is(updatedUserDto.getGender())));
    }

    @Test
    @DisplayName("Updating user with a future birth date")
    public void givenUpdateUserDtoWithFutureBirthDate_whenUpdateUser_thenErrorResponse() throws Exception {
        //given
        UpdateUserDto userToUpdate = UserUtil.getUpdatedBohnJoDto();
        userToUpdate.setBirthDate(LocalDate.now().plusYears(1));

        //when
        ResultActions result = mockMvc.perform(patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userToUpdate)));

        //then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.BAD_REQUEST.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(FUTURE_BIRTH_DATE_MESSAGE)));
    }

    @Test
    @DisplayName("Deleting user by id")
    void givenId_whenDeleteById_thenSuccessResponse() throws Exception {
        //given
        doNothing().when(userService).delete(anyLong());

        //when
        ResultActions result = mockMvc.perform(delete("/users/1"));

        //then
        verify(userService, times(1)).delete(anyLong());
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User was deleted"));
    }

    @Test
    @DisplayName("Getting user by non-existent id")
    void givenNonExistentId_whenDeleteById_thenErrorResponse() throws Exception {
        //given
        doThrow(new NotFoundException(NOT_FOUND_MESSAGE)).when(userService).delete(anyLong());

        //when
        ResultActions result = mockMvc.perform(delete("/users/101"));

        //then
        result.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(HttpStatus.NOT_FOUND.value())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(NOT_FOUND_MESSAGE)));
    }
}
