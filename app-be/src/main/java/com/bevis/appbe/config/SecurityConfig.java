package com.bevis.appbe.config;

import com.bevis.security.JwtConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.bevis.security.AuthoritiesConstants.ADMIN;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;
    private final UserDetailsService userDetailsService;

    //TODO configure security routes
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/admin-api/**").hasAnyAuthority(ADMIN)
                .antMatchers("/api/vendors/**").authenticated()
                .antMatchers("/api/asset-types/**").authenticated()
                .antMatchers("/api/masters/**").authenticated()
                .antMatchers("/api/account/region").authenticated()
                .antMatchers("/api/account").authenticated()
                .antMatchers("/api/coins**").authenticated()
                .antMatchers(HttpMethod.GET, "/api/authenticate").authenticated()
                .antMatchers("/**").permitAll()
                .and()
                .apply(jwtConfigurer)
                .and()
                .csrf().disable()
                .headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/v2/api-docs/**")
                .antMatchers("/webjars/springfox-swagger-ui/**")
                .antMatchers("/swagger-ui.html");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        try {
            auth.authenticationProvider(authenticationProvider());
        } catch (Exception e) {
            throw new BeanInitializationException("Security configuration failed", e);
        }
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
