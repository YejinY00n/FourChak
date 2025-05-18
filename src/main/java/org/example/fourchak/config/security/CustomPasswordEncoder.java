package org.example.fourchak.config.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return BCrypt.withDefaults().hashToString(at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST, rawPassword.toString().toCharArray());
    }
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toString().toCharArray(), encodedPassword);
        return result.verified;
    }

}
