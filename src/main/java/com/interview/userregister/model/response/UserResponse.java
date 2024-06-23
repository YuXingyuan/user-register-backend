package com.interview.userregister.model.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponse extends BaseResponse {

    private String id;
    private String name;
    private String email;
    private Integer age;
    private Boolean active;
    private Boolean emailSent;
}
