package com.example.AddressBookApp.service;


import com.example.AddressBookApp.dto.AddressBookDTO;
import com.example.AddressBookApp.model.AddressBookModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public interface AddressBookInterface {
    void add(@RequestBody AddressBookDTO addressBookDTO);
    List<AddressBookModel> getDetails();
    AddressBookModel getDetailsById(Long id);
    AddressBookModel updateDetails(Long id, AddressBookDTO addressBookDTO);
    ResponseEntity<String> deleteDetails(Long id);
}
