package com.shop.exception.order;


public class OutOfStockException extends RuntimeException{

    public OutOfStockException(String message){
        super(message);
    }
}
