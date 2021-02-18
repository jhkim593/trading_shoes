package jpa.project.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jpa.project.dto.member.MemberDto;
import jpa.project.dto.member.MemberRegisterRequestDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter

@Builder

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity implements UserDetails {
    @Id  @GeneratedValue
    @Column(name="member_id")
    private Long id;

    @Column(unique = true)
    private String username;

//    @Column( nullable = false)
    private String password;
    @Column(length = 150)
    private String email;

    private String refreshToken;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Board>boards=new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<RegistedShoes>registedShoes=new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Board_liked>board_likeds=new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles=new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "buyer")
    private List<Order> purchases = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "seller")
    private List<Order> sales = new ArrayList<>();

    @Embedded
    private Address address;




    //멤버 요소 추가?
    public Member(MemberRegisterRequestDto mrrDto) {

        this.username = mrrDto.getUsername();
        this.password = mrrDto.getPassword();
        this.email=mrrDto.getEmail();
    }
    public void update(MemberRegisterRequestDto mrrDto){
        if(mrrDto.getUsername()!=null){
        this.username = mrrDto.getUsername();
        }
        if(mrrDto.getPassword()!=null){
        this.password = mrrDto.getPassword();
        }
        if(mrrDto.getEmail()!=null) {
            this.email = mrrDto.getEmail();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername(){
        return this.username;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }


    public void changeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
//    public static Member createMember(String username,){
//        Member member=new Member();
//        member.username=username;
//        member.password=password;
//        member.email=email;
//
//
//    }
}
