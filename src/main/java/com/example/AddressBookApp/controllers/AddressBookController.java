package com.example.AddressBookApp.controllers;


import com.example.AddressBookApp.dto.AddressBookDTO;
import com.example.AddressBookApp.model.AddressBookModel;
import com.example.AddressBookApp.service.AddressBookInterface;
import com.example.AddressBookApp.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class AddressBookController {

    @Autowired
    AddressBookInterface addressBookInterface;

    @GetMapping
    public String greetUser() {
        return "Welcome to Address Book Application";
    }

    @PostMapping("/addAddress")
    public ResponseEntity<String> addAddress(@RequestBody AddressBookDTO addressBookDTO) {
        addressBookInterface.add(addressBookDTO);
        return ResponseEntity.ok("details successfully added");
    }

    @GetMapping("/getDetails")
    public ResponseEntity<List<AddressBookModel>> getDetails(){
        return ResponseEntity.ok(addressBookInterface.getDetails());
    }

    @GetMapping("/getDetails/{id}")
    public AddressBookModel getDetailsById(@PathVariable Long id){
        return addressBookInterface.getDetailsById(id);
    }

    @PutMapping("/updateDetails/{id}")
    public AddressBookModel updateDetails(@PathVariable Long id,@RequestBody AddressBookDTO addressBookDTO){
        return addressBookInterface.updateDetails(id,addressBookDTO);
    }

    @DeleteMapping("/deleteDetails/{id}")
    public ResponseEntity<String> deleteDetails(@PathVariable Long id){
        addressBookInterface.deleteDetails(id);
        return ResponseEntity.ok("deatil delated successfully");
    }
}
