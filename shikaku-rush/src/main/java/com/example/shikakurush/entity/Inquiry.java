package com.example.shikakurush.entity;

import lombok.Data;

@Data
public class Inquiry {
    private int id;
    private Integer userId;
    private String title;
    private String body;
}