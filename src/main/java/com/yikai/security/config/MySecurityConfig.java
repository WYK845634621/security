package com.yikai.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Description
 * @Tips
 * @Author yikai.wang
 * @Number K1171305
 * @Date 2020/11/25 17:31
 */
@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedPage("/denied.html");

        http.formLogin()
                //访问的登录页的url
                .loginPage("/login.html")
                //表单url
                .loginProcessingUrl("/user/login")
                //登录成功后的
                .defaultSuccessUrl("/test/index").permitAll()
                //无需认证页面
                .and().authorizeRequests().antMatchers("/","/test/hello","/user/login").permitAll()
//                .antMatchers("/test/index").hasAnyAuthority("common,admin")
                //hasRole方法自动添加了"ROLE_"前缀
                .antMatchers("/test/index").hasRole("manager")
                .anyRequest().authenticated()
                .and().csrf().disable();
    }
}
