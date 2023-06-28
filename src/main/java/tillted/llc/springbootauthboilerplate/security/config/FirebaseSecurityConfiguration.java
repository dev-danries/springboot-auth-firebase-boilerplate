package tillted.llc.springbootauthboilerplate.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

public interface FirebaseSecurityConfiguration {

    @Bean
    default AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Autowired ObjectMapper objectMapper;

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                Map<String, Object> errorObject = new HashMap<>();
                int errorCode = 401;
                errorObject.put("message", "Unauthorized access of protected resource, invalid credentials");
                errorObject.put("error", HttpStatus.UNAUTHORIZED);
                errorObject.put("code", errorCode);
                errorObject.put("timestamp", new Timestamp(new Date().getTime()));
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(errorCode);
                response.getWriter().write(objectMapper.writeValueAsString(errorObject));
            }
        };
    }

    @Bean
    default CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
