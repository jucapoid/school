package com.xhamster.services;

import com.xhamster.repos.UtilizadorRepo;
import com.xhamster.models.Utilizador;
import com.xhamster.MyUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
@Autowired
private UtilizadorRepo userRepository;
     
	@Override
	public UserDetails loadUserByUsername(String username)
		throws UsernameNotFoundException {
      
	        Utilizador user = userRepository.findOneByUsername(username);
               
	        if (user == null) {
	        	throw new UsernameNotFoundException("Could not find user");
		}
         
	        return new MyUserDetails(user);
	}
 
}
