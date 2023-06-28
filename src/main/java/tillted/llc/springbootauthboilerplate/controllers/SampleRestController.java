package tillted.llc.springbootauthboilerplate.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SampleRestController {

    @GetMapping("/")
    public Principal helloWorld(Principal principal) {
        return principal;
    }

}
