package de.philipp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import de.philipp.data.user.BspUserService;
import de.philipp.views.login.LoginView;

@EnableWebSecurity 
@Configuration
public class SecurityConfiguration
                extends VaadinWebSecurity { 

	@Autowired
	private BspUserService bpsUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Delegating the responsibility of general configurations
        // of http security to the super class. It's configuring
        // the followings: Vaadin's CSRF protection by ignoring
        // framework's internal requests, default request cache,
        // ignoring public views annotated with @AnonymousAllowed,
        // restricting access to other views/endpoints, and enabling
        // NavigationAccessControl authorization.
        // You can add any possible extra configurations of your own
        // here (the following is just an example):

        // http.rememberMe().alwaysRemember(false);

        // Configure your static resources with public access before calling
        // super.configure(HttpSecurity) as it adds final anyRequest matcher
        http.authorizeHttpRequests(auth -> 
            auth.requestMatchers("/css/**", "/js/**", "/images/**", "/public/**", "/css/**").permitAll())
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login").permitAll()
            )
            .logout(logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/").permitAll()
            )
            .rememberMe(rememberMe ->
                rememberMe
                    .userDetailsService(bpsUserService)
                    .tokenRepository(persistentTokenRepository())
            )
            .csrf(csrf -> csrf.disable()); // Enable this if necessary

        super.configure(http); 

        // This is important to register your login view to the
        // navigation access control mechanism:
        setLoginView(http, LoginView.class); 
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
	}
    
	@Bean
	public InMemoryTokenRepositoryImpl persistentTokenRepository() {
		return new InMemoryTokenRepositoryImpl();
	}

    @Bean
    public PersistentTokenBasedRememberMeServices rememberMeServices() {
        PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
                "uniqueKey", bpsUserService, persistentTokenRepository());
        rememberMeServices.setAlwaysRemember(true); // Ensure remember me is always enabled
        return rememberMeServices;
    }
}