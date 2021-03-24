package jpa.project.model.dto.member;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRegisterResponseDto {
    private Long id;
    private String username;
    private String email;
}
