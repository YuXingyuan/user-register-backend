package com.interview.userregister.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Builder
@Data
public class UserRequest extends BaseRequest {

    @NotEmpty(message = "User name cannot be empty")
    @Size(max = 20, message = "User name cannot exceed 20 characters")
    @Schema(description= "User name", example = "Jack", maxLength = 20)
    private String name;

    @NotEmpty(message = "User email cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "User email format is invalid")
    @Schema(description= "User email", example = "jack@gmail.com")
    private String email;

    @NotNull(message = "User age cannot be empty")
    @Range(min = 1, max = 99, message = "User age should be from 0 to 99")
    @Schema(description= "User age", minimum = "1", maximum = "99", example = "25")
    private Integer age;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description= "User activeness", example = "true")
    private Boolean active = Boolean.TRUE;

}
