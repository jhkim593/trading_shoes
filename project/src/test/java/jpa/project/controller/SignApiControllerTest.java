package jpa.project.controller;

import jpa.project.config.security.JwtTokenProvider;
import jpa.project.model.dto.member.MemberLoginRequestDto;
import jpa.project.model.dto.member.MemberLoginResponseDto;
import jpa.project.model.dto.member.MemberRegisterRequestDto;
import jpa.project.repository.member.MemberRepository;
import jpa.project.service.SignService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SignApiControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired
    SignService signService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MemberRepository memberRepository;
    @BeforeEach
    public void beforeEach(){

     signService.signup(new MemberRegisterRequestDto("dd","dd","z"));
     signService.signup(new MemberRegisterRequestDto("11","zzz","z"));
    }
    @Test
    public void signUp()throws Exception{
       //given
     MemberRegisterRequestDto mem=new MemberRegisterRequestDto("d1d","dd","zz");


        MultiValueMap<String,String>params=new LinkedMultiValueMap<>();
        params.add("username","11");
        params.add("password","zzz");
        params.add("email","zzdd");
       //when
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content))
                .params(params))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());



       //then
    }
    @Test
    public void signIn()throws Exception{
       //given
        MultiValueMap<String,String>params=new LinkedMultiValueMap<>();
        params.add("username","11");
        params.add("password","zzz");

       //when

       //then

        mockMvc.perform(MockMvcRequestBuilders.post("/signin")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content))
                .params(params))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void logOutTest()throws Exception{
       //given
        MemberLoginResponseDto signin = signService.signin(new MemberLoginRequestDto("dd", "dd"));
        String token = signin.getToken();


        //when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/logout")
                .header("X-AUTH-TOKEN", token))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertThat(jwtTokenProvider.validateToken(token)).isFalse();

    }


}