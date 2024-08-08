package de.philipp.data.user;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BspUserService implements UserDetailsService {
	
	private final BspUserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public BspUserService(BspUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public BspUser findUserByUsername(String username) {
		return userRepository.findByEmail(username);
	}
	
	public boolean saveUser(BspUser user) {

		if (user == null) {
			return false;
		}
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		System.out.println("service: " + user.getEmail());

		BspUser existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser != null) {
			System.out.println("user already exists");
			return false;
		}

		userRepository.save(user);
		System.out.println("user saved");
		return true;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		BspUser user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
	}
}
