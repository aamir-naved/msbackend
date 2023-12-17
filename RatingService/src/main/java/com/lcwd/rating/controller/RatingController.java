package com.lcwd.rating.controller;

import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.ratelimiter.RateLimiter;
import com.lcwd.rating.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
   private RateLimiter rateLimiter;


    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping
    public ResponseEntity<Rating> create(@RequestBody Rating rating){
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.createRating(rating));
    }



    @GetMapping
    public ResponseEntity<List<Rating>> getRatings(){
        return ResponseEntity.ok(ratingService.getRatings());
    }


//    //original method
//    @GetMapping("/users/{userId}")
//    public ResponseEntity<List<Rating>> getRatingsByUserId(@PathVariable String userId){
//        return ResponseEntity.ok(ratingService.getRatingsByUserId(userId));
//    }


    //some changes to implement rate limiter
    @PreAuthorize("hasAuthority('SCOPE_internal') || hasAuthority('Admin')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Rating>> getRatingsByUserId(@PathVariable String userId){

        ArrayList<Rating> message = new ArrayList<>();
        message.add(new Rating("API rate limit exceed"));
        ResponseEntity<List<Rating>> data = ResponseEntity.badRequest().body(message);
        if (rateLimiter.tryAcquire()){
            try {
                data = ResponseEntity.ok(ratingService.getRatingsByUserId(userId));
                return data;
            }finally {
                rateLimiter.release();
            }
        }else{
            return data;
        }

    }


    @GetMapping("/hotels/{hotelId}")
    public ResponseEntity<List<Rating>> getRatingsByHotelId(@PathVariable String hotelId){
        return ResponseEntity.ok(ratingService.getRatingByHotelId(hotelId));
    }
}
