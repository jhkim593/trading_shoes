package jpa.project.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberLoginRequestDto {
    private String username;
    private String password;
}
