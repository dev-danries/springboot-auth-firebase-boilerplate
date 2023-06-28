package tillted.llc.springbootauthboilerplate.security.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FirebaseUtils {

    private static final String AUTH_HEADER = "Authorization";
    private static final String ENV_FIREBASE_CONFIG_PATH = "FIREBASE_CONFIG_PATH";
    private static final String ENV_FIREBASE_DATABASE_URL = "FIREBASE_DATABASE_URL";

    private static String GOOGLE_APP_CREDENTIALS = null;
    static {
        try {
            GOOGLE_APP_CREDENTIALS = ResourceUtils.getFile(System.getenv(ENV_FIREBASE_CONFIG_PATH)).getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static final String DATABASE_URL = System.getenv(ENV_FIREBASE_DATABASE_URL);

    public static void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream(GOOGLE_APP_CREDENTIALS);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            log.info("Firebase Initialized");
        } catch (IOException ex) {
            log.error("Error Trying To Initialize Firebase", ex);
        }
    }

    public static String getBearerToken(HttpServletRequest request) {
        String bearerToken = null;
        String authorization = request.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            bearerToken = authorization.substring(7);
        }
        return bearerToken;
    }

}
