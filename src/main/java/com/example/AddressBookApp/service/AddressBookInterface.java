package com.example.AddressBookApp.service;

import com.example.AddressBookApp.dto.AddressBookDTO;
import com.example.AddressBookApp.dto.ResponseDTO;
import com.example.AddressBookApp.model.AddressBookModel;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public interface AddressBookInterface {
    ResponseDTO<String, String> add(@RequestBody AddressBookDTO addressBookDTO);
    List<AddressBookModel> getAllAddress();
    AddressBookModel getById(Long id);
    ResponseDTO<String, String> updateAddress(Long id, AddressBookDTO addressBookDTO);
    ResponseDTO<String, String> deleteAddress(Long id);
}