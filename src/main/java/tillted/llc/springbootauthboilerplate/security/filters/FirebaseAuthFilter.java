package tillted.llc.springbootauthboilerplate.security.filters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import tillted.llc.springbootauthboilerplate.security.models.FirebaseUser;
import tillted.llc.springbootauthboilerplate.security.utils.FirebaseMapper;
import tillted.llc.springbootauthboilerplate.security.utils.FirebaseUtils;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class FirebaseAuthFilter extends OncePerRequestFilter {

    private static final String[] WHITELIST = {
            "/favicon.ico", "/session/login", "/session/test", "/public/**/",
            "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security",
            "/swagger-ui.html", "/webjars/**",
            "/actuator/info", "/v2/api-docs"
    };

    private RedisTemplate<String, Object> redisTemplate;

    public FirebaseAuthFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        verifyToken(request);
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return Arrays.stream(WHITELIST).anyMatch(e -> new AntPathMatcher().match(e, path));
    }

    private void verifyToken(HttpServletRequest request) {
        FirebaseToken decodedToken = null;
        String bearerToken = null;
        try {
            bearerToken = FirebaseUtils.getBearerToken(request);
            if (Boolean.TRUE.equals(redisTemplate.hasKey(bearerToken))) {
                log.debug("Authentication Found in Redis");
                UsernamePasswordAuthenticationToken authReq = (UsernamePasswordAuthenticationToken)
                        redisTemplate.opsForValue().get(bearerToken);
                Optional.ofNullable(authReq).ifPresentOrElse(present -> {
                    authReq.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authReq);
                }, () -> {
                    throw new RuntimeException("Authentication Error with Redis");
                });
                return;
            } else {
                decodedToken = FirebaseAuth.getInstance().verifyIdToken(bearerToken);
            }
        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
        }

        if (Optional.ofNullable(decodedToken).isPresent()) {
            FirebaseUser firebaseUser = FirebaseMapper.INSTANCE.toFirebaseUser(decodedToken);
            firebaseUser.setRole("USER");
            Map<String, Object> claims = decodedToken.getClaims();
            if (!claims.isEmpty() && claims.containsKey("role")) {
                firebaseUser.setRole(((String) claims.get("role")).toUpperCase(Locale.ROOT));
            }
            Set<GrantedAuthority> roles = new HashSet<>();
            roles.add(new SimpleGrantedAuthority(String.format("ROLE_%s", firebaseUser.getRole())));
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(firebaseUser, null, roles);
            redisTemplate.opsForValue().set(bearerToken, authReq);
            authReq.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authReq);
        }
    }
}
