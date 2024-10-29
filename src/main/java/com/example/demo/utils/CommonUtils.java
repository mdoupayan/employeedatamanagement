package com.example.demo.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class CommonUtils {
	
	public String capitalizeWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Split the string into words, capitalize each, and join them back together
        return Arrays.stream(input.split(" "))
                .map(word -> word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

}
