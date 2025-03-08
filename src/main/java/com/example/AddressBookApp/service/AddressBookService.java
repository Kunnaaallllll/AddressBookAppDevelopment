package com.example.AddressBookApp.service;

import com.example.AddressBookApp.controllers.AddressBookController;
import com.example.AddressBookApp.dto.AddressBookDTO;
import com.example.AddressBookApp.model.AddressBookModel;
import com.example.AddressBookApp.repository.AddressBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class AddressBookService implements AddressBookInterface {

    @Autowired
    AddressBookRepository addressBookRepositories;
    private static final Logger log = LoggerFactory.getLogger(AddressBookService.class);


    @Override
    public ResponseEntity<String> add(AddressBookDTO addressBookDTO) {
        log.info("Received request to add address: {}", addressBookDTO);
        AddressBookModel addressBookModel = new AddressBookModel();
        addressBookModel.setName(addressBookDTO.getName());
        addressBookModel.setAddress(addressBookDTO.getAddress());
        addressBookModel.setPhoneNumber(addressBookDTO.getPhoneNumber());
        try {
            addressBookRepositories.save(addressBookModel);
            log.info("Address added successfully: {}", addressBookDTO);
            return ResponseEntity.ok("Address Added Successfully");
        } catch (Exception e) {
            log.error("Error while adding address: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @Override
    public List<AddressBookModel> getAllAddress() {
        log.debug("Fetching all address details");
        return addressBookRepositories.findAll();
    }

    @Override
    public AddressBookModel getById(Long id) {
        log.debug("Fetching address details for ID: {}", id);
        AddressBookModel address = addressBookRepositories.findById(id).orElse(null);
        if (address == null) {
            log.warn("Address not found for ID: {}", id);
            return null;
        }
        log.info("Address found for ID: {}", id);
        return address;
    }

    @Override
    public ResponseEntity<String> updateAddress(Long id, AddressBookDTO addressBookDTO) {
        log.info("Received request to update address for ID: {}", id);
        AddressBookModel existingAddress = addressBookRepositories.findById(id).orElse(null);
        if (existingAddress != null) {
            existingAddress.setName(addressBookDTO.getName());
            existingAddress.setAddress(addressBookDTO.getAddress());
            existingAddress.setPhoneNumber(addressBookDTO.getPhoneNumber());
            addressBookRepositories.save(existingAddress);
            log.info("Address updated successfully for ID: {}", id);
            return ResponseEntity.ok("Update DONE Successfully");
        } else {
            log.warn("Address update failed, ID not found: {}", id);
            return ResponseEntity.status(404).body("Address Not Found");
        }
    }

    @Override
    public ResponseEntity<String> deleteAddress(Long id) {
        log.info("Received request to delete address for ID: {}", id);
        if (!addressBookRepositories.existsById(id)) {
            log.warn("Address not found for deletion, ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address ID Not FOUND");
        }
        addressBookRepositories.deleteById(id);
        log.info("Address deleted successfully for ID: {}", id);
        return ResponseEntity.ok("Address Deleted Successfully");
    }
}