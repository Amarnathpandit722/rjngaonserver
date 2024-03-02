package gov.municipal.suda.usermanagement.service;

import gov.municipal.suda.usermanagement.dao.NewUserDao;
import gov.municipal.suda.usermanagement.dao.UserDao;
import gov.municipal.suda.usermanagement.model.JwtRequest;
import gov.municipal.suda.usermanagement.model.JwtResponse;
import gov.municipal.suda.usermanagement.model.NewUser;
import gov.municipal.suda.usermanagement.model.NewUserDetails;
import gov.municipal.suda.usermanagement.model.User;
import gov.municipal.suda.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class JwtServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {

        String userName = jwtRequest.getUserName();
        String userPassword = jwtRequest.getUserPassword();
        log.info("userName  {},  UserPassword {} ",userName,userPassword);
        authenticate(userName, userPassword);
       // 	log.info("authenticate {} ",userName);
        final UserDetails userDetails= loadUserByUsername(userName);
        String newGeneratedToken=jwtUtil.generateToken(userDetails);
      //  log.info("NewGenereated Token {} ",newGeneratedToken);
        User user=userDao.findByUserName(userName).get();
       // log.info("User {} ",user );
    
        return new JwtResponse(user,newGeneratedToken);
    }

   
    
    
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    
        User user = userDao.findByUserName(username).get();
     
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(),getAuthorities(user));
        } else {
            throw new UsernameNotFoundException("Username is not valid");
        }
    }

    private Set getAuthorities(User user) {

        Set authorities = new HashSet();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("User is disabled");
        }
    }
    
    
    
    
    
    
    
    
}