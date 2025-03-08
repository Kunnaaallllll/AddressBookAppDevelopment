package com.example.AddressBookApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {

    @NotBlank(message = "Full Name Cannot be Empty")
    @Size(min =3, message = "Full Name must be MIN 3 Character Long")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Name must start with a capital letter and contain only letters")
    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message = "Email Cannot be Empty")
    @Email
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.[A-Z])(?=.\\d)[A-Za-z\\d@$!%*?&]{6,}$",
            message = "Password must have at least 6 characters, 1 uppercase letter, and 1 digit")
    private String password;
}