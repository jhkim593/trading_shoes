package jpa.project.Service;

import jpa.project.advide.exception.CNotOwnerException;
import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.advide.exception.CUsernameSigninFailedException;
import jpa.project.dto.member.MemberDto;
import jpa.project.dto.member.MemberRegisterRequestDto;
import jpa.project.entity.Member;
import jpa.project.repository.MemberRepository;
import jpa.project.repository.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService  {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //api
    @Transactional
    public Long save(String username,String password,String email){
        String role="ROLE_USER";
        if(username.equals("a")){
            role="ROLE_ADMIN";
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .roles(Collections.singletonList(role))
                .build();

        memberRepository.save(member);





//        validJoinUser(member);



        return member.getId();
    }



    @Transactional
    public MemberDto join(MemberRegisterRequestDto mrrDto){

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        mrrDto.setPassword(passwordEncoder.encode(mrrDto.getPassword()));

        Member member = Member.builder()
                .username(mrrDto.getUsername())
                .password(passwordEncoder.encode(mrrDto.getPassword()))
                .email(mrrDto.getEmail())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        memberRepository.save(member);




//        validJoinUser(member);



        return new MemberDto(member);
    }

    private void validJoinUser(Member member) {

        Optional<Member> findMember = memberRepository.findByUsername(member.getUsername());
        if(!findMember.isEmpty()){
            throw new IllegalStateException("이미 가입된 회원입니다");

        }

    }
    @Transactional
    public MemberDto update(MemberRegisterRequestDto mrrDto,Long id){
        Optional<Member> findMember = memberRepository.findById(id);
        Member member = findMember.orElseThrow(() -> new NoSuchElementException());
        member.update(mrrDto);

        return new MemberDto(member);


    }
    @Transactional
    public void delete(Long id){
        memberRepository.deleteById(id);
    }


    public MemberDto find(Long id){
        Optional<Member> findMember = memberRepository.findById(id);
        Member member = findMember.orElseThrow(CUserNotFoundException::new);


        return new MemberDto(member);
    }

    public List<MemberDto>findAll(){
        List<Member> member = memberRepository.findAll();
        List<MemberDto>memberDtos=new ArrayList<>();
        for (Member member1 : member) {
            MemberDto memberDto=new MemberDto(member1);
            memberDtos.add(memberDto);

        }
        return memberDtos;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        //Optional<Member> userEntityWapper = memberRepository.findByName(name);
//        Member userEntity = memberRepository.findByUsername(username).get(); //get으로 꺼내와도되나?
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        if (("admin@example.com").equals(username)) {
//            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
//        } else {
//            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
//        }
//
//        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
//    }

    public MemberDto findByUsername(String username){
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = findMember.orElseThrow(CUsernameSigninFailedException::new);
        return new MemberDto(member);

    }





}
