package com.lcwd.user.service.services.impl;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    // Injecting feign client hotel service here


    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {

        //generated unique user id
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);



        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user =  userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with given id is not available"+userId));
        //get rating of particular user here by calling RATING SERVICE

//        http://localhost:8083/ratings/users/c7760a2c-5ae3-4f3f-a65e-28f55041b45c

        Rating[] ratingsData = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
        logger.info("{}",ratingsData);

        List<Rating> ratings = Arrays.stream(ratingsData).toList();

//        http://localhost:8082/hotels/b47b11b3-664a-41c0-a14e-8bda7094a712

        //get hotel using rating, because rating has hotel id

//        restTemplate.getForEntity(" http://localhost:8082/hotels/"+)

       List<Rating> ratingList = ratings.stream().map(rating -> {
           //instead of using rest template
//           ResponseEntity<Hotel> hotelEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId() ,Hotel.class);
//           Hotel hotel = hotelEntity.getBody();

           // we are using feign client here.
           Hotel hotel = hotelService.getHotel(rating.getHotelId());
           rating.setHotel(hotel);

           return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);


        return user;
    }
}

