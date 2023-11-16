package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CreateOrUpdateAdDto {

    private Integer price;
    private String title;
    private String description;

}
