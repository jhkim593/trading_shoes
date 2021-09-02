package jpa.project.model.dto.member;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegisterRequestDto {



    private String username;

    private String password;

    private String email;
}
