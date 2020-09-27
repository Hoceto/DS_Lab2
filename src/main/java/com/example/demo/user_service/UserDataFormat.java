package com.example.demo.user_service;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDataFormat {

    @NotNull
    String userName;

    @NotNull
    String userSurname;

    @NotNull
    String phoneNumber;

//    @NotNull
//    Date birthdayDate;

    @NotNull
    String birthdayDate;

    @NotNull
    String passportId;

    @NotNull
    String userTin;

    @NotNull
    String userEmail;
}
