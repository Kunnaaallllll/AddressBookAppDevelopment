package com.example.AddressBookApp.dto;


//import jakarta.persistence.Column;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class AddressBookDTO {
    private String name;
    private String address;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    private String phoneNumber;

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

}
