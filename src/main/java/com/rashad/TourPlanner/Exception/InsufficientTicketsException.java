package com.rashad.TourPlanner.Exception;

public class InsufficientTicketsException extends RuntimeException{
    public InsufficientTicketsException(String message) {
        super(message);
    }
}
