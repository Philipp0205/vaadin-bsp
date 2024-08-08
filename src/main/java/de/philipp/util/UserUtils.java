package de.philipp.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import de.philipp.data.user.BspUser;
import de.philipp.data.user.BspUserService;

@Component
public class UserUtils {

	private static BspUserService userService; // Service to fetch user details

	public UserUtils(BspUserService userService) {
		this.userService = userService;
	}

	// Static setter injection to set the UserService
	public static void setUserService(BspUserService service) {
		userService = service;
	}

	public static BspUser getCurrentAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !authentication.getPrincipal().equals("anonymousUser")) {
			String username = ((UserDetails) authentication.getPrincipal()).getUsername();
			return userService.findUserByUsername(username);
		}
		return null;
	}
	
	public static BspUser getAnonymousUser() {
		return userService.findUserByUsername("anonymus");
	}
}
