package com.library.library_project.dto;

import com.library.library_project.model.User;
import com.library.library_project.model.UserStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserRequest {

    private String userName;

    @NotBlank(message = "user phone number should not be blank")
    private String phoneNo;

    private String email;

    private String address;

    public User toUser() {
        return User
                .builder()
                .name(this.userName)
                .email(this.email)
                .phoneNo(this.phoneNo)
                .address(this.address)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }
}
