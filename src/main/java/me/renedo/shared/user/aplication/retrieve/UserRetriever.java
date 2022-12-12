package me.renedo.shared.user.aplication.retrieve;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import me.renedo.shared.Service;

@Service
public class UserRetriever implements UserDetailsService {
    private static final Logger logger = LogManager.getLogger();

    private final Map<String, String> users;

    private final List<GrantedAuthority> authorityList;

    public UserRetriever() {
        //TODO for the moment very simple, users in properties
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("users.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            users = prop.entrySet().stream()
                .filter(p->p.getKey() instanceof String && p.getValue() instanceof String)
                .collect(Collectors.toMap(p->(String)p.getKey(), p->(String)p.getValue()));
            authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("USER"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = users.get(username);
        if( password == null){
            logger.warn("Try to log: {}", username);
            throw new UsernameNotFoundException("Not found");
        }
        return new User(username, password, authorityList);
    }
}
