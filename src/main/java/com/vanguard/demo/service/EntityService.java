package com.vanguard.demo.service;

public interface EntityService<T> {


    T getOrCreateEntity(String attributeValue);
}
