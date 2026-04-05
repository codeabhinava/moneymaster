package com.example.moneymaster.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AppRegistration {

    private String userName;
    private String password;
    private String phoneNo;
    private String email;

}
