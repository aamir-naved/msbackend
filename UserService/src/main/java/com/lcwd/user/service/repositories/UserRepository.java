package com.lcwd.user.service.repositories;


import com.lcwd.user.service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    //If you want to implement any custom methods then you can here.
}
