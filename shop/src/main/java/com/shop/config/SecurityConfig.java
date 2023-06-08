package com.shop.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //설정
@EnableWebSecurity
//WebSecurityConfigurerAdapter 상속 받는 클래스에 @EnableWebSecurity 선언을하면
//SpringSecurityFilterChain이 자동 포함 메소드를 오버라이딩 할 수 있습니다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception{
            http.formLogin()
                    .loginPage("/members/login") //로그인할 url
                    .defaultSuccessUrl("/") //성공하면 /로 간다
                    .usernameParameter("email") // 유저네임 email을 가져와서 쓴다
                    .failureUrl("/members/login/error")
                    .and() //그리고
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃때 members/logout을
                    //매칭한다
                    .logoutSuccessUrl("/"); //성공하면 루트로간다


            http.authorizeRequests()
                    .mvcMatchers("/","/members/**","/item/**","/images/**","/menu/**").permitAll()
                    .mvcMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated();

            http.exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

    }

    @Bean //원두 -> 객체 빈객체 -> SpringContainer 들어갑니다. 이객체 하나로 돌려쓴다.(싱글톤)
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder(); // 비밀번호를 암호화 하는 해시함수
    }

    @Override
    public void configure(WebSecurity web)throws Exception{
        web.ignoring().antMatchers("/css/**","/js/**","/img/**");
        //static 폴더안에있는 /css /js /img안에 있는 하위의 모든 파일은 인증을 무시한다.
    }
}
