package tillted.llc.springbootauthboilerplate.security.utils;

import com.google.firebase.auth.FirebaseToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import tillted.llc.springbootauthboilerplate.security.models.FirebaseUser;

@Mapper
public interface FirebaseMapper {

    FirebaseMapper INSTANCE = Mappers.getMapper(FirebaseMapper.class);

    @Mapping(target = "role", expression = "java((String) token.getClaims().get(\"role\"))")
    FirebaseUser toFirebaseUser(FirebaseToken token);
}
