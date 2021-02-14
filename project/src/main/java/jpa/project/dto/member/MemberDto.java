package jpa.project.dto.member;

import jpa.project.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class MemberDto {
    private Long id;


    @NotEmpty(message = "이름값은 필수")
    private String username;

//    @NotBlank(message = "이메일은 필수 입력값입니다.")
//    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;
//    @NotBlank(message = "비밀번호는 필수입력값 입니다.")
//    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
//    message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;

    private List<String>roles=new ArrayList<>();

    public MemberDto(Member member) {
        this.id= member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.password = member.getPassword();

        for (String role : member.getRoles()) {
            this.roles.add(role);

        }

    }
}
