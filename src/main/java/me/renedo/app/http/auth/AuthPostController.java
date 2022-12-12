package me.renedo.app.http.auth;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import me.renedo.app.http.V1Controller;
import me.renedo.config.security.util.TokenValidator;

@RestController
public class AuthPostController extends V1Controller {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final TokenValidator jwtTokenUtil;

    public AuthPostController(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, TokenValidator jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/auth/login")
    public AuthResponse loginUser(@RequestBody AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.user(), request.password()));
        if (auth.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.user());
            return new AuthResponse(jwtTokenUtil.generateToken(userDetails));
        } else {
            throw new AccessDeniedException("user or password incorrect");
        }
    }

    record AuthResponse(String token) {
    }


    record AuthRequest(String user, String password) {
    }
}
