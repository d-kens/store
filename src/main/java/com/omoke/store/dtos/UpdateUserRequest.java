package com.omoke.store.dtos;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private  String name;
    private String email;
}
