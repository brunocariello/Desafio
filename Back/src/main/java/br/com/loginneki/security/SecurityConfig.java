package br.com.loginneki.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.http.HttpMethod;

import br.com.loginneki.util.JwtAuthenticationFilter;
import br.com.loginneki.util.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(JwtUtil jwtUtil, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtUtil = jwtUtil;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Configuring SecurityFilterChain");

        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(authorize -> authorize
                // Permissões liberadas
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios/cadastrar").permitAll()
                .requestMatchers(HttpMethod.GET, "/skills/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/usuario-skill/user/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/usuario-skill/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuario-skill").permitAll()
                .requestMatchers(HttpMethod.GET, "/usuario-skill/valid-ids").permitAll()
                .requestMatchers(HttpMethod.PUT, "/usuario-skill/update-level").permitAll()
                .requestMatchers(HttpMethod.GET, "/levels/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/users/me/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/usuario-skill/by-usuario/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/skills/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/skills/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/usuario-skill/**").permitAll()
                .requestMatchers(HttpMethod.PUT, "/usuario-skill/**").permitAll()



                .anyRequest().authenticated()
            )
            .headers(headers -> headers
                .frameOptions().sameOrigin()
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.debug("Creating PasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        logger.debug("Creating AuthenticationManager bean");
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Ajuste conforme necessário
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JwtAuthenticationFilter("/api/**", jwtUtil, authenticationManager());
    }
}
