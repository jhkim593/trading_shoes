//package jpa.project.config.security;
//
//import jpa.project.Service.MemberService;
//import jpa.project.Service.UserLoginFailHandler;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//@EnableWebSecurity
//@Configuration
//
//@RequiredArgsConstructor
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    private final MemberService memberService;
//    private final UserLoginFailHandler userLoginFailHandler;
//
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//
//
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                // 페이지 권한 설정
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .antMatchers("/user/myinfo").hasRole("MEMBER")
//                .antMatchers("/**").permitAll()
//                .and() // 로그인 설정
//                .formLogin()
//                .loginPage("/user/login")
//                .defaultSuccessUrl("/user/login/result")
//                .failureHandler(userLoginFailHandler)
//                .permitAll()
//                .and() // 로그아웃 설정
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                .logoutSuccessUrl("/user/logout/result")
//                .invalidateHttpSession(true)
//                .and()
//                // 403 예외처리 핸들링
//
//                .exceptionHandling().accessDeniedPage("/user/denied")
//                .and()
////                .csrf()
////                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//        .csrf().disable();
//
//
//    }
//
//
//    public void configure(WebSecurity web)throws Exception{
//
//        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
//    }
//}
