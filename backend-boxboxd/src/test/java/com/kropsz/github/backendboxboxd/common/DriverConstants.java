package com.kropsz.github.backendboxboxd.common;

import java.time.LocalDate;

import com.kropsz.github.backendboxboxd.entities.Driver;

public class DriverConstants {
    
        public static final Driver VALID_DRIVER = new Driver("HAM",
            "44",
            "Lewis",
            "Hamilton",
            (byte) 0,
            "BOT",
            "Mercedes",
            LocalDate.of(1985, 1, 7),
            "British",
            "Um dos maiores pilotos da história da Fórmula 1.",
            "http://example.com/lewis_hamilton",
            100,
            150,
            90,
            1,
            9.5,
            5,
            5000,
            2000);

}
