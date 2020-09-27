package com.example.demo.user_service;

import com.example.demo.DemoApplication;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/banking/user")
@ComponentScan
public class UserController {

    @Autowired
    private  UserConfig userService;

    final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){

        try{
            final List<User> userList = userService.getAllUsers();

            return new ResponseEntity<>(userList, HttpStatus.OK);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") UUID userId){

        try{
            final User user = userService.getUserById(userId);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        catch (MissingResourceException | NotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDataFormat userBody) throws ParseException {
        Date userDate = new SimpleDateFormat("yyyy-MM-dd").parse(userBody.birthdayDate);
        try{
            User newUser = new User(userBody.userName, userBody.userSurname, userBody.phoneNumber,
                    userDate, userBody.passportId, userBody.userTin, userBody.userEmail);
            userService.addUser(newUser);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (MissingResourceException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/dummy")
    public ResponseEntity<Void> createDummyUser() throws ParseException {
        Date birthdayDate = new GregorianCalendar(2000, Calendar.JANUARY, 22).getTime();
        User dummyUser = new User( "Otto",
                "Bismarck", "+3801234511", birthdayDate,"5123",
                "2961293318", "bism@arck.com");
        userService.addUser(dummyUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
