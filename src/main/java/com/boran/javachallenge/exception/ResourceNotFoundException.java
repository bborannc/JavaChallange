package com.boran.javachallenge.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s ID'si %d olan kaynak bulunamadı.", resourceName, id));
    }


    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s ('%s' ile tanımlanan) bulunamadı.", resourceName, identifier));
    }


    public ResourceNotFoundException(String resourceName, Long id, String message) {
        super(String.format("%s ID'si %d olan kaynak bulunamadı. Detay: %s", resourceName, id, message));
    }
}
