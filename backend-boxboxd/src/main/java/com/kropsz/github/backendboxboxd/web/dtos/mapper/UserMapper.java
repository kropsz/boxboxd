package com.kropsz.github.backendboxboxd.web.dtos.mapper;

import com.kropsz.github.backendboxboxd.entities.User;
import com.kropsz.github.backendboxboxd.web.dtos.UserRegisterDto;
import org.modelmapper.ModelMapper;

public class UserMapper {

    public  static User toUser(UserRegisterDto dto){
        return new ModelMapper().map(dto, User.class);
    }
}
