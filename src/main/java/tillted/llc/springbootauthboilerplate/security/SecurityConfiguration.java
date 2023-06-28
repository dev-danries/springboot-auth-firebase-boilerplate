package tillted.llc.springbootauthboilerplate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tillted.llc.springbootauthboilerplate.security.config.FirebaseSecurityConfiguration;
import tillted.llc.springbootauthboilerplate.security.filters.FirebaseAuthFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration implements FirebaseSecurityConfiguration {

    private RedisTemplate<String, Object> securityRedisTemplate;

    public SecurityConfiguration(RedisTemplate<String, Object> redisTemplate) {
        this.securityRedisTemplate = redisTemplate;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.anyRequest().authenticated())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(restAuthenticationEntryPoint()))
                .addFilterBefore(new FirebaseAuthFilter(securityRedisTemplate), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
