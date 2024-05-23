package com.kropsz.github.backendboxboxd.web.dtos.mapper;

import com.kropsz.github.backendboxboxd.entities.User;
import com.kropsz.github.backendboxboxd.web.dtos.UserRegisterDto;
import com.kropsz.github.backendboxboxd.web.dtos.UserResponse;

import org.modelmapper.ModelMapper;

public class UserMapper {

    public  static User toUser(UserRegisterDto dto){
        return new ModelMapper().map(dto, User.class);
    }

    public static UserResponse toUserDto(User user){
        return new ModelMapper().map(user, UserResponse.class);
    }
}
