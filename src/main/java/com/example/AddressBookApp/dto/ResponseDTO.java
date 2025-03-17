package com.example.AddressBookApp.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T,U> {
    private T message;
    private U messageData;


    public T getMessage() {
        return message;
    }

    public U getMessageData() {
        return messageData;
    }

    public void setMessage(T message) {
        this.message = message;
    }

    public void setMessageData(U messageData) {
        this.messageData = messageData;
    }
}