package com.example.AddressBookApp.controllers;

import com.example.AddressBookApp.dto.AddressBookDTO;
import com.example.AddressBookApp.model.AddressBookModel;
import com.example.AddressBookApp.service.AddressBookInterface;
import com.example.AddressBookApp.exception.AddressBookException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class AddressBookController {

    @Autowired
    AddressBookInterface addressBookInterface;
    private static final Logger log = LoggerFactory.getLogger(AddressBookController.class);


    @GetMapping("/")
    public String sayGreeting() {
        log.debug("Greeting endpoint accessed");
        return "Welcome to Address Book Application";
    }

    @PostMapping("/addAddress")
    public ResponseEntity<String> addAddress(@Valid @RequestBody AddressBookDTO addressBookDTO) {
        log.info("Received request to add address: {}", addressBookDTO);
        try {
            ResponseEntity<String> response = addressBookInterface.add(addressBookDTO);
            log.info("Address added successfully: {}", addressBookDTO);
            return response;
        } catch (Exception e) {
            log.error("Error while adding address: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Failed to add address. Error: " + e.getMessage());
        }
    }

    @GetMapping("/getAddress")
    public ResponseEntity<?> getAddress() {
        log.debug("Fetching all addresses");
        try {
            List<AddressBookModel> addresses = addressBookInterface.getAllAddress();
            if (addresses.isEmpty()) {
                log.warn("No addresses found in the database");
                return ResponseEntity.status(204).body("No addresses found");
            }
            log.info("Successfully fetched {} addresses", addresses.size());
            return ResponseEntity.ok(addresses);
        } catch (Exception e) {
            log.error("Error while fetching addresses: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/getAddress/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable Long id) {
        log.debug("Fetching address details for ID: {}", id);
        try {
            Object address = addressBookInterface.getById(id);
            if (address == null) {
                log.warn("No address found for ID: {}", id);
                throw new AddressBookException("Address Not Found for ID: " + id);
            }
            log.info("Address details fetched successfully for ID: {}", id);
            return ResponseEntity.ok(address);
        } catch (Exception e) {
            log.error("Error fetching address details for ID: {}", id, e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @PutMapping("/updateAddress/{id}")
    public ResponseEntity<String> updateAddressById(@PathVariable Long id, @Valid @RequestBody AddressBookDTO addressBookDTO) {
        log.info("Received request to update address for ID: {}", id);
        try {
            ResponseEntity<String> response = addressBookInterface.updateAddress(id, addressBookDTO);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Address not found for ID: {}", id);
                return ResponseEntity.status(404).body("Address Not Found");
            }
            log.info("Address updated successfully for ID: {}", id);
            return response;
        } catch (Exception e) {
            log.error("Error updating address for ID: {}", id, e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteAddress/{id}")
    public ResponseEntity<String> deleteAddressById(@PathVariable Long id) {
        log.info("Received request to delete address for ID: {}", id);
        try {
            ResponseEntity<String> response = addressBookInterface.deleteAddress(id);
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Address not found for deletion, ID: {}", id);
            } else {
                log.info("Address deleted successfully for ID: {}", id);
            }
            return response;
        } catch (Exception e) {
            log.error("Error deleting address for ID: {}", id, e);
            return ResponseEntity.status(500).body("Internal Server Error: " + e.getMessage());
        }
    }
}