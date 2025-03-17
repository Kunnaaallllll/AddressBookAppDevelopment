package com.example.AddressBookApp.service;

import com.example.AddressBookApp.dto.AddressBookDTO;
import com.example.AddressBookApp.dto.ResponseDTO;
import com.example.AddressBookApp.model.AddressBookModel;
import com.example.AddressBookApp.repository.AddressBookRepository;
import com.example.AddressBookApp.exception.AddressBookException;
import com.example.AddressBookApp.service.AddressBookInterface;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class AddressBookService implements AddressBookInterface {

    @Autowired
    AddressBookRepository addressBookRepositories;
    private static final Logger log = LoggerFactory.getLogger(AddressBookService.class);


    @Override
    public ResponseDTO<String, String> add(AddressBookDTO addressBookDTO) {
        log.info("Received request to add address: {}", addressBookDTO);
        AddressBookModel addressBookModel = new AddressBookModel();
        addressBookModel.setName(addressBookDTO.getName());
        addressBookModel.setAddress(addressBookDTO.getAddress());
        addressBookModel.setPhoneNumber(addressBookDTO.getPhoneNumber());
        ResponseDTO<String, String> res = new ResponseDTO<>();
        try {
            addressBookRepositories.save(addressBookModel);
            log.info("Address added successfully: {}", addressBookDTO);
            res.setMessage("message");
            res.setMessageData("Address Added Successfully");
            return res;
        } catch (Exception e) {
            log.error("Error while adding address: {}", e.getMessage(), e);
            res.setMessage("error");
            res.setMessageData(e.getMessage());
            return res;
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
        return addressBookRepositories.findById(id)
                .orElseThrow(() -> new AddressBookException("Address Not Found for ID: " + id));
    }

    @Override
    public ResponseDTO<String, String> updateAddress(Long id, AddressBookDTO addressBookDTO) {
        ResponseDTO<String, String> res = new ResponseDTO<>();
        log.info("Received request to update address for ID: {}", id);
        AddressBookModel existingAddress = addressBookRepositories.findById(id).orElse(null);
        if (existingAddress != null) {
            existingAddress.setName(addressBookDTO.getName());
            existingAddress.setAddress(addressBookDTO.getAddress());
            existingAddress.setPhoneNumber(addressBookDTO.getPhoneNumber());
            addressBookRepositories.save(existingAddress);
            log.info("Address updated successfully for ID: {}", id);
            res.setMessage("message");
            res.setMessageData("Update DONE Successfully");
            return res;
        } else {
            log.warn("Address update failed, ID not found: {}", id);
            res.setMessage("error");
            res.setMessageData("Address Not Found");
            return res;
        }
    }
    @Override
    public ResponseDTO<String, String> deleteAddress(Long id) {
        log.info("Received request to delete address for ID: {}", id);
        ResponseDTO<String, String> res = new ResponseDTO<>();
        if (!addressBookRepositories.existsById(id)) {
            log.warn("Address not found for deletion, ID: {}", id);
            res.setMessage("error");
            res.setMessageData("Address ID Not FOUND");
            return res;
        }
        addressBookRepositories.deleteById(id);
        log.info("Address deleted successfully for ID: {}", id);
        res.setMessage("message");
        res.setMessageData("Address Deleted Successfully");
        return res;
    }
}