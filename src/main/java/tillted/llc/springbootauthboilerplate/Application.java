package tillted.llc.springbootauthboilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tillted.llc.springbootauthboilerplate.security.annotation.EnableFirebaseAuth;

@EnableFirebaseAuth
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
