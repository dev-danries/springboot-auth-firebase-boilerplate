package tillted.llc.springbootauthboilerplate.security.annotation;

import org.springframework.context.annotation.Import;
import tillted.llc.springbootauthboilerplate.security.FirebaseRunnerAfterInit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({FirebaseRunnerAfterInit.class})
public @interface EnableFirebaseAuth {
}
