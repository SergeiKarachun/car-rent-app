package by.sergey.carrentapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .antMatchers("/login", "/users/sign-up", "/v3/api-docs/**", "/swagger-ui/**", "/api/v1/cars{\\d+}/car_image").permitAll()
                        .antMatchers(HttpMethod.POST, "/users/create").permitAll()
                        .anyRequest().authenticated())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID"))
                .formLogin(login -> login
                        .loginPage("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/cars")
                        .permitAll());

        return http.build();
    }
}
