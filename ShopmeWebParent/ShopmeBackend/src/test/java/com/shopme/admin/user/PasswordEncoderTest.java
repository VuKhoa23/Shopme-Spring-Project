package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;


public class PasswordEncoderTest {
    @Test
    public void testEncodePassword(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPasword = "anhkhoa0123";
        String encodedPassword = passwordEncoder.encode(rawPasword);
        System.out.println("encoded password: " + encodedPassword);

        boolean result = passwordEncoder.matches(rawPasword, encodedPassword);
        assertThat(result).isTrue();
    }
}
