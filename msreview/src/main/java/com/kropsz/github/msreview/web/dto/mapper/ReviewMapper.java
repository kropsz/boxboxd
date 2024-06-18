package com.kropsz.github.msreview.web.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.kropsz.github.msreview.entities.Reviews;
import com.kropsz.github.msreview.web.dto.RecivePayload;

public class ReviewMapper {

    public static RecivePayload toDto(Reviews produto) {
        return new ModelMapper().map(produto, RecivePayload.class);
    }

    public static List<RecivePayload> toListDto(List<Reviews> reviews) {
        return reviews.stream().map(review -> toDto(review)).collect(Collectors.toList());
    }

}
