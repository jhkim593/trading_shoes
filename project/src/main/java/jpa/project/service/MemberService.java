package jpa.project.service;

import jpa.project.advide.exception.CUserNotFoundException;
import jpa.project.entity.Member;
import jpa.project.model.dto.member.MemberDto;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService  {

    private final MemberRepository memberRepository;


    @Transactional
    public MemberDto update(MemberRegisterRequestDto mrrDto,Long id){
        Optional<Member> findMember = memberRepository.findById(id);
        Member member = findMember.orElseThrow(() -> new CUserNotFoundException());
        member.update(mrrDto);

        return MemberDto.createMemberDto(member);


    }
    @Transactional
    public void delete(Long id){
        memberRepository.deleteById(id);
    }

    public MemberDto find(Long id){
        Optional<Member> findMember = memberRepository.findById(id);
        Member member = findMember.orElseThrow(CUserNotFoundException::new);
        return MemberDto.createMemberDto(member);
    }


    public List<MemberDto>findAll(){
        List<Member> member = memberRepository.findAll();
        List<MemberDto> memberDtos = member.stream().map(m -> MemberDto.createMemberDto(m)).collect(Collectors.toList());

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







}
