package com.example.membership.service;

import org.springframework.stereotype.Service;

@Service
public class RatePointService implements PointService{

    private static final int RATE_POINT = 1;

    public int calculateAmount(int price) {
        return price * RATE_POINT / 100;
    }
}
