package ua.mykola.UserAccountsManagementSystem.rest.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ErrorMessage {
    private int status;
    private String message;
}
