package com.adsologist.adsologist.response;

import lombok.Data;

@Data
public class UserResponse {
    private Integer id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String username;
}
