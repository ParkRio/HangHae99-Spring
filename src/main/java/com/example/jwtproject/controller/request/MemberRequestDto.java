package com.example.jwtproject.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    // Todo :: Validation 사용을 원할 시 dto 를 사용할 controller 에 @Valid 어노테이션을 추가한다.

    @NotBlank // @NotNUll < @NotEmpty < @NotBlank -> validation 강도
    @Size(min = 4, max =12)
    @Pattern(regexp = "[a-zA-Z\\d]*${3,12}") //@Pattern, Size, NotBlank -> validation dependency
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, max = 12)
    @Pattern(regexp = "[a-zA-Z\\d]*${3,12}")
    private String password;

    @NotBlank
    private String passwordConfirm;
}
