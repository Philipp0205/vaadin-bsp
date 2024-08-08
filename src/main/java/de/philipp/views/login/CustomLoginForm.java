package de.philipp.views.login;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.login.LoginForm;

import de.philipp.data.user.BspUser;

public class CustomLoginForm extends FormLayout {
	private LoginForm loginForm;
	private Button registerButton = new Button("Register");

	public CustomLoginForm() {
		loginForm = new LoginForm();
		
		registerButton.addClickListener(event -> UI.getCurrent().navigate(RegisterView.class));
		
		add(loginForm, registerButton);
	}
	
	public LoginForm getLoginForm() {
		return loginForm;
	}
	
	public static abstract class CustomLoginFormEvent extends ComponentEvent<CustomLoginForm> {
		private BspUser user;

		protected CustomLoginFormEvent(CustomLoginForm source, BspUser user) {
			super(source, false);
			this.user = user;
		}

		public BspUser getUser() {
			return user;
		}
	}
	
	public static class RegisterEvent extends CustomLoginFormEvent {
		RegisterEvent(CustomLoginForm source, BspUser user) {
			super(source, user);
		}
	}

}
