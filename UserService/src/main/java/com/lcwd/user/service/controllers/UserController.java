package com.lcwd.user.service.controllers;

import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/users")
public class UserController {

    /*
    Whenever you hit /users url, if it post type then user create ho jaayega,
    if it get type then all users will be fetched and returned
     */

    private org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    //create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        /*
        User ka data json ke format me aayega, by using @RequestBody, the data will
        be deserialize into User type.
         */

        User createdUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);

    }


    //single user get
// this api is calling hotel service and rating service
    // here we will implement circuit breaker
    @GetMapping("/{userId}")
    @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        User fetchedUser = userService.getUser(userId);
        return ResponseEntity.ok(fetchedUser);
    }


    // checking if the changes reflect in github or not.ß
    //creating fallback method for circuit breaker
    // method definition should be same as the method for which this method will
    // behave as fallback method and then at the end or parameter take Exception type too
    public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex){

        logger.info("Fallback is executed because service is down.", ex.getMessage());
       User user = User.builder().email("dummy@email.com").
               name("dummyname")
               .about("This user is created dummy because service is down")
               .userId("user1234")
               .build();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }


    //get all user
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

}
