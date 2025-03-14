package com.example.AddressBookApp.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="AddressBook")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data //ye Getter Setter ki parent class hai ek trh se
public class AddressBookModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String address;

    @Column(nullable=false)
    private String phoneNumber;

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
