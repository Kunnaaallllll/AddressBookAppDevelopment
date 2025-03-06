package com.example.AddressBookApp.service;


import com.example.AddressBookApp.dto.AddressBookDTO;
import com.example.AddressBookApp.model.AddressBookModel;
import com.example.AddressBookApp.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class AddressBookService {

    @Autowired
    AddressBookRepository addressBookRepository;

    AddressBookModel addressBookModel=new AddressBookModel();


    public void add(@RequestBody AddressBookDTO addressBookDTO){
        addressBookModel.setAddress(addressBookDTO.getAddress());
        addressBookModel.setPhoneNumber(addressBookDTO.getPhoneNumber());
        addressBookModel.setName(addressBookDTO.getName());

        addressBookRepository.save(addressBookModel);
    }

    //saari details lene k liye bnaya ye
    public List<AddressBookModel> getDetails(){
        return addressBookRepository.findAll();
    }

    public AddressBookModel getDetailsById(Long id){
        return addressBookRepository.findById(id).orElse(null);
    }

    public AddressBookModel updateDetails(Long id, AddressBookDTO addressBookDTO){
        AddressBookModel oldDetails=addressBookRepository.findById(id).orElse(null);
        if(oldDetails!=null){
            oldDetails.setName(addressBookDTO.getName());
            oldDetails.setAddress(addressBookDTO.getAddress());
            oldDetails.setPhoneNumber(addressBookDTO.getPhoneNumber());
        }
        return oldDetails;
    }

    public void deleteDetails(@PathVariable Long id){
        if (!addressBookRepository.existsById(id)) {
            System.out.println("no id found");
        }else{
            addressBookRepository.deleteById(id);
        }
    }
}
