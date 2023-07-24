package net.example.spring.web.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz
                        .requestMatchers(//"/", "/index.html",
                                         "/asset-manifest.json",
                                         "/favicon.ico",
                                         "/logo.svg",
                                         "/logo192.png",
                                         "/manifest.json",
                                         "/robots.txt",
                                         "/static/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(withDefaults())
                .logout(withDefaults())
                .httpBasic(withDefaults())
                .csrf(csrf -> csrf.disable())
                //.csrf(csrf -> csrf
                //        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                //.addFilterAfter(new CookieCsrfFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling(ehc -> ehc
                        .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                                            noHTMLAcceptRequestMatcher()))
                .build();
    }

    /*
     * Discriminate between user browsing resources, and programmatic JS XHR/fetch requests.
     */
    private static NegatedRequestMatcher noHTMLAcceptRequestMatcher() {
        MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
        requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
        return new NegatedRequestMatcher(requestMatcher);
    }

}
