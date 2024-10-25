package com.pettory.pettory.security.config;

import com.pettory.pettory.security.handler.JwtAuthenticationEntryPoint;
import com.pettory.pettory.security.filter.CustomAuthenticationFilter;
import com.pettory.pettory.security.filter.JwtFilter;
import com.pettory.pettory.security.handler.JwtAccessDeniedHandler;
import com.pettory.pettory.security.handler.LoginFailureHandler;
import com.pettory.pettory.security.handler.LoginSuccessHandler;
import com.pettory.pettory.security.util.JwtUtil;
import com.pettory.pettory.user.command.application.service.UserCommandService;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserCommandService userCommandService;
    private final Environment env;
    private final JwtUtil jwtUtil;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> {
                    authz.requestMatchers(new AntPathRequestMatcher("/login", "POST")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/users/**", "POST")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/users/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/users/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/users/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/families/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/families/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/families/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/invitations/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/invitations/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/pets/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/pets/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/pets/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/pets/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/walking-records/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/walking-records/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/walking-records/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/walking-records/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/feeding-records/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/feeding-records/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/feeding-records/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/feeding-records/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/ws/**","POST")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/ws/**","GET")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/jointshopping/**", "GET")).hasAuthority("ROLE_USER")
                            .requestMatchers(new AntPathRequestMatcher("/jointshopping/**", "POST")).hasAuthority("ROLE_USER")
                            .requestMatchers(new AntPathRequestMatcher("/jointshopping/**", "PUT")).hasAuthority("ROLE_USER")
                            .requestMatchers(new AntPathRequestMatcher("/jointshopping/**", "DELETE")).hasAuthority("ROLE_USER")
                            .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/swagger-ui/index.html")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/webjars/**")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/question/v1/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/question/v1/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/question/v1/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/question/v1/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/answer/v1/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/answer/v1/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/answer/v1/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/answer/v1/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group-application/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group-application/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group-application/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group-application/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group-record/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group-record/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group-record/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/walking-group-record/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/register-walking-group/**", "GET")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/register-walking-group/**", "POST")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/register-walking-group/**", "PUT")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/api/register-walking-group/**", "DELETE")).hasAnyAuthority("ROLE_USER", "ROLE_VET", "ROLE_ADMIN")

                            //board 부분
                            .requestMatchers(new AntPathRequestMatcher("/board/posts/**", "GET")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/board/posts/**", "POST")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/board/posts/**", "PUT")).hasAuthority("ROLE_USER")
                            .requestMatchers(new AntPathRequestMatcher("/board/posts/**", "DELETE")).hasAuthority("ROLE_USER")
                            .requestMatchers(new AntPathRequestMatcher("/board/comments/**", "GET")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/board/comments/**", "POST")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/board/comments/**", "PUT")).hasAuthority("ROLE_USER")
                            .requestMatchers(new AntPathRequestMatcher("/board/comments/**", "DELETE")).hasAuthority("ROLE_USER")
//                            .requestMatchers(new AntPathRequestMatcher("/board/categorys/**", "GET")).hasAuthority("ROLE_ADMIN")
//                            .requestMatchers(new AntPathRequestMatcher("/board/categorys/**", "PUT")).hasAuthority("ROLE_ADMIN")
//                            .requestMatchers(new AntPathRequestMatcher("/board/categorys/**", "POST")).hasAuthority("ROLE_ADMIN")
//                            .requestMatchers(new AntPathRequestMatcher("/board/categorys/**", "DELETE")).hasAuthority("ROLE_ADMIN")
                            .requestMatchers(new AntPathRequestMatcher("/board/categorys/**", "GET")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/board/categorys/**", "PUT")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/board/categorys/**", "POST")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/board/categorys/**", "DELETE")).permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // JWT 토큰 확인 필터 추가
        http.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // 커스텀 로그인 필터 추가
        // default 인증 담당 필터보다 앞에 커스텀 필터 추가
        http.addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 인증, 인가 실패 핸들러 설정
        http.exceptionHandling(
                exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(new JwtAccessDeniedHandler());
                    exceptionHandling.authenticationEntryPoint(new JwtAuthenticationEntryPoint());
                }
        );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); // 허용할 도메인
        config.addAllowedHeader("*"); // 모든 헤더 허용
        config.addAllowedMethod("*"); // 모든 HTTP 메소드 허용
        config.addExposedHeader("token"); // 서버측에서 보내는 헤더에 대한 허용 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 CORS 적용
        return source;
    }


    private Filter getAuthenticationFilter() {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();   // 커스텀 로그인 필터
        customAuthenticationFilter.setAuthenticationManager(getAuthenticationManager());    // 인증 매니저
        customAuthenticationFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(env));  // 인증 성공 후 작업
        customAuthenticationFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        return customAuthenticationFilter;
    }

    // 커스텀 로그인 필터에 설정할 인증 매니저
    private AuthenticationManager getAuthenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userCommandService);
        return new ProviderManager(provider);
    }

}