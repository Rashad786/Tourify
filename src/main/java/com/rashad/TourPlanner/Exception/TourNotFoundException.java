package com.rashad.TourPlanner.Exception;

public class TourNotFoundException extends RuntimeException{
    public TourNotFoundException(String message) {
        super(message);
    }
}
