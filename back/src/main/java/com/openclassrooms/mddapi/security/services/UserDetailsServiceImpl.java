package com.openclassrooms.mddapi.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		// On cherche d'abord par email, sinon par username
		User user = userRepository.findByEmail(usernameOrEmail)
				.orElseGet(() -> userRepository.findByUsername(usernameOrEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username/email: " + usernameOrEmail)));

		return UserDetailsImpl.build(user);
	}
}