package tillted.llc.springbootauthboilerplate.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tillted.llc.springbootauthboilerplate.security.utils.FirebaseUtils;

@Configuration
public class FirebaseRunnerAfterInit {

    @Bean
    public CommandLineRunner runner() {
        return args -> FirebaseUtils.init();
    }

}
