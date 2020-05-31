package pacApp.pacSecurity;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pacApp.pacData.UserRepository;
import pacApp.pacModel.User;

@Service
public class JwtAuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationService.class);
    private UserRepository userRepository;
    private JwtTokenAuthorizationService tokenService;
    private PasswordEncoder passwordEncoder;

    public JwtAuthenticationService(UserRepository userRepository, JwtTokenAuthorizationService tokenService, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }	

    public String generateJwtToken(String email, String password){
        log.info("generateJwtToken");
        log.info(password);

        Optional<User> optionalUser = this.userRepository.findOneByEmail(email);

        if (!optionalUser.isPresent()) {
            throw new JwtAuthenticationException("incorrect user");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new JwtAuthenticationException("incorrect password");
        }

        String token = tokenService.generateToken(email);

        return token;
    }
}
