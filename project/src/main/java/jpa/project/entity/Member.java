package jpa.project.entity;

import jpa.project.model.dto.member.MemberRegisterRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter

@Builder
@Setter

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id  @GeneratedValue
    @Column(name="member_id")
    private Long id;

    @Column(unique = true)
    private String username;

//    @Column( nullable = false)
    private String password;

    @Column(length = 150,nullable = false)
    private String email;

    private String provider;

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
    public Member(String username,String password,String email) {

        this.username = username;
        this.password = password;
        this.email=email;
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
