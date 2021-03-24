package jpa.project.model.dto.member;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberLoginResponseDto {
    private Long id;
    private String token;
    private String refreshToken;
}
