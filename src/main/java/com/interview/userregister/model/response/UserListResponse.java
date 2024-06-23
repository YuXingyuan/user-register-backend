package com.interview.userregister.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserListResponse extends BaseResponse {

    private List<UserResponse> userList;

}
