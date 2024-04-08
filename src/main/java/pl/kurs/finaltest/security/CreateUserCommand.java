package pl.kurs.finaltest.security;

import jakarta.validation.constraints.NotBlank;

public class CreateUserCommand {

    @NotBlank(message = "Nick wymagany")
    private String username;
    @NotBlank(message = "Hasło wymagane")
    private String password;
    @NotBlank(message = "Rola wymagana")
    private String role;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
