package recipes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.model.User;
import recipes.persistent.UserRepository;
import recipes.security.UserDetailsImpl;

@Service
public class UserDetailsServiceIml implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);

        if(user == null) {
            throw new UsernameNotFoundException("Not found" + username);
        }

        return new UserDetailsImpl(user);
    }
}
