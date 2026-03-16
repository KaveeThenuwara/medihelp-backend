package lk.ijse.medihelpbackend.config;


import lk.ijse.medihelpbackend.service.custom.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;


@EnableWebSecurity
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/authenticate",
                                "/api/v1/auth/send-register-code",
                                "/api/v1/auth/verify-register-code",
                                "/api/v1/auth/forgot-password",
                                "/api/v1/auth/reset-password",
                                "/api/v1/user/register",
                                "/api/v1/auth/refreshToken",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()

                        // ADMIN only - Using hasAuthority because roles are stored without ROLE_ prefix
                        .requestMatchers("/api/v1/user/all").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/user/delete/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/admin/**").hasAuthority("ADMIN")

                        // ADMIN + RECEPTION — manage users/patients
                        .requestMatchers("/api/v1/user/role/**").hasAnyAuthority("ADMIN", "RECEPTION")
                        .requestMatchers("/api/v1/user/search/**").hasAnyAuthority("ADMIN", "RECEPTION", "USER")

                        // ADMIN + RECEPTION — all appointments
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/appointments")
                               .hasAnyAuthority("ADMIN", "RECEPTION")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/appointments/**")
                               .hasAnyAuthority("ADMIN", "RECEPTION")

                        // All authenticated roles — scoped appointment access
                        .requestMatchers("/api/v1/appointments/**")
                               .hasAnyAuthority("ADMIN", "RECEPTION", "DOCTOR", "USER")

                        // Doctors — read for all, write for ADMIN only
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/doctors/**")
                               .hasAnyAuthority("ADMIN", "RECEPTION", "DOCTOR", "USER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/doctors/**").hasAuthority("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/doctors/**").hasAuthority("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/doctors/**").hasAuthority("ADMIN")

                        // Payments
                        .requestMatchers("/api/v1/payments/**").hasAnyAuthority("ADMIN", "RECEPTION", "USER")

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
