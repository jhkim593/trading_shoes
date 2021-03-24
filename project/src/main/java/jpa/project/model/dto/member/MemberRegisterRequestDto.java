package jpa.project.model.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRegisterRequestDto {



    private String username;

    private String password;

    private String email;
}
