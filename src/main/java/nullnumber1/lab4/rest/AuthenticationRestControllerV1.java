package nullnumber1.lab4.rest;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nullnumber1.lab4.dto.AuthRequestDto;
import nullnumber1.lab4.model.User;
import nullnumber1.lab4.security.jwt.JwtTokenProvider;
import nullnumber1.lab4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/")
@Slf4j
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @SneakyThrows
    @PostMapping("register")
    @CrossOrigin
    public ResponseEntity<?> register(@Validated @RequestBody AuthRequestDto authRequestDto) {
        User user = new User();
        user.setPassword(authRequestDto.getPassword());
        user.setUsername(authRequestDto.getUsername());
        try {
            userService.register(user);
            return new ResponseEntity<>(user.getUsername(), HttpStatus.OK);
        } catch (DataAccessException e) {
            log.info("in register() – user wasn't unable to register with username: \"{}\" & password: \"{}\"", authRequestDto.getPassword(), authRequestDto.getUsername());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PostMapping("login")
    @CrossOrigin
    public ResponseEntity<?> login(@RequestBody AuthRequestDto authRequestDto) {
        try {
            String username = authRequestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authRequestDto.getPassword()));
            User user = userService.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }

            String token = jwtTokenProvider.createToken(user.getId(), username, user.getRoles());

            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            log.info("in login() – user wasn't unable to authenticate with username: \"{}\" & password: \"{}\"", authRequestDto.getPassword(), authRequestDto.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
