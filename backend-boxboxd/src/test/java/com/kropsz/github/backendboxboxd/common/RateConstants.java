package com.kropsz.github.backendboxboxd.common;

import com.kropsz.github.backendboxboxd.entities.like.EntityType;
import com.kropsz.github.backendboxboxd.entities.rating.Rate;

public class RateConstants {
    
    public final static Rate VALID_RATE = new Rate(
            1L,
            1L,
            "entityId",
            EntityType.CIRCUIT,
            5.0
    );
}
