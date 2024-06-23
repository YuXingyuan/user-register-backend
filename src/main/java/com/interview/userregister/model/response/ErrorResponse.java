package com.interview.userregister.model.response;

import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends BaseResponse {

    private String message;
}
