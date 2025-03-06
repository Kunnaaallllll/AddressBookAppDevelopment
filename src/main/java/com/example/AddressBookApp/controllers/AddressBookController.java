package com.example.AddressBookApp.controllers;


import com.example.AddressBookApp.dto.AddressBookDTO;
import com.example.AddressBookApp.model.AddressBookModel;
import com.example.AddressBookApp.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    @GetMapping
    public String greetUser() {
        return "Welcome to Address Book Application";
    }

    @PostMapping("/addAddress")
    public ResponseEntity<String> addAddress(@RequestBody AddressBookDTO addressBookDTO) {
        addressBookService.add(addressBookDTO);
        return ResponseEntity.ok("details successfully added");
    }

    @GetMapping("/getDetails")
    public ResponseEntity<List<AddressBookModel>> getDetails(){
        return ResponseEntity.ok(addressBookService.getDetails());
    }

    @GetMapping("/getDetails/{id}")
    public AddressBookModel getDetailsById(@PathVariable Long id){
        return addressBookService.getDetailsById(id);
    }

    @PutMapping("/updateDetails/{id}")
    public AddressBookModel updateDetails(@PathVariable Long id,@RequestBody AddressBookDTO addressBookDTO){
        return addressBookService.updateDetails(id,addressBookDTO);
    }

    @DeleteMapping("/deleteDetails/{id}")
    public ResponseEntity<String> deleteDetails(@PathVariable Long id){
        addressBookService.deleteDetails(id);
        return ResponseEntity.ok("deatil delated successfully");
    }
}
