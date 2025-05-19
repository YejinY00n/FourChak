package org.example.fourchak.config.security;

import lombok.RequiredArgsConstructor;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;
import org.example.fourchak.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserPrincipal loadUserByUsername(String username)
        throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .map(CustomUserPrincipal::new)
            .orElseThrow(() -> new CustomRuntimeException(ExceptionCode.NOT_FOUND_USERNAME));
    }
}
