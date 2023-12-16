package com.lcwd.rating.services;

import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.repository.RatingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {

    Rating createRating(Rating rating);


    List<Rating> getRatings();

    List<Rating> getRatingsByUserId(String userId);

    List<Rating> getRatingByHotelId(String hotelId);
}
