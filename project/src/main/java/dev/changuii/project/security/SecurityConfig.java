package dev.changuii.project.security;

import dev.changuii.project.security.filter.JWTAuthenticationFilter;
import dev.changuii.project.security.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;


    private static final String[] AUTH_WHITELIST = {
            "/api/auth/signin", //로그인
            "/api/auth/signup", //회원가입
            "/error/**", // Custom exception
            "/sample/**",
            "/**"
    };

    @Bean
    public SecurityFilterChain filterChainInternal(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 정책: STATELESS
                .formLogin((form)->form.disable())
                .httpBasic(AbstractHttpConfigurer::disable)//FormLogin, BasicHttp 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .anyRequest().authenticated() // 그 외 모든 요청은 인증 요구
                )
//                .exceptionHandling(ex -> ex
//                        .accessDeniedHandler(new CustomAccessDeniedHandler()) // 접근 거부 핸들러
//                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // 인증 실패 핸들러
//                )
                .addFilterBefore(new JWTAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class); // JWT 인증 필터 추가

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //Todo: 도메인 변경 필요함
        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:3000", "https://wanted66.netlify.app"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
