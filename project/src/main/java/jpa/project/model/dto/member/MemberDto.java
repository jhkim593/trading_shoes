package jpa.project.model.dto.member;

import jpa.project.entity.Member;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class MemberDto {
    private Long id;


    @NotEmpty(message = "이름값은 필수")
    private String username;


    private String email;

    private String password;

    private List<String>roles=new ArrayList<>();


    public static MemberDto createMemberDto(Member member){
      return new MemberDto(member.getId(), member.getUsername(), member.getEmail(), member.getPassword(),
             member.getRoles());

    }
}
