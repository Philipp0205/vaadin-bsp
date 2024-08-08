package de.philipp.views.login;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.shared.Registration;

import de.philipp.data.user.BspUser;

public class RegisterForm extends FormLayout {
	private Binder<BspUser> binder = new Binder<>(BspUser.class);
	
	private H1 title = new H1("Register");
	private TextField email = new TextField("Email");
	private PasswordField password = new PasswordField("Password");
	private PasswordField confirmPassword = new PasswordField("Confirm Password");
	private Button save = new Button("Save");
	private Button cancel = new Button("Cancel");
	
	    public RegisterForm() {
	    	binder.bindInstanceFields(this);
	    	
	    	binder.forField(email)
	    	.asRequired("Email is required")
            .withValidator(new EmailValidator("Please enter a valid email address"))
            .bind(BspUser::getEmail, BspUser::setEmail);
	    	
			binder.forField(password)
			.asRequired("Password is required")
			.withValidator(new StringLengthValidator("Password must be at least 5 characters", 5, null))
			.bind(BspUser::getPassword, BspUser::setPassword);
	    	
	    	
	    	add(title, email, password, confirmPassword, createButtonsLayout());
	    }
	    
		private FormLayout createButtonsLayout() {
			save.addClickListener(event -> validateAndSave());
			cancel.addClickListener(event -> UI.getCurrent().navigate(LoginView.class));

			email.setRequired(true);
			password.setRequired(true);
			confirmPassword.setRequired(true);

			return new FormLayout(save, cancel);
		}

		private void validateAndSave() {
			System.out.println("validateAndSave");
			if (!password.getValue().equals(confirmPassword.getValue())) {
				confirmPassword.setInvalid(true);
				password.setErrorMessage("Passwords do not match");
			}

			BspUser user = new BspUser(); // Assuming you have a default constructor
			user.setEmail(email.getValue());
			user.setPassword(password.getValue());
			binder.setBean(user); // Bind the user to the form's fields
			
			System.out.println("email: " + email.getValue());

			if (binder.validate().isOk()) {
				fireEvent(new SaveEvent(this, binder.getBean())); // Pass the updated user
			} else {
				Notification.show("Please check your input", 3000, Position.MIDDLE);
			}
		}
		
		public void setUser(BspUser user) {
			binder.setBean(user);
		}
		
		// Custom events for save and close actions
		public static abstract class RegisterFormEvent extends ComponentEvent<RegisterForm> {
			private BspUser user;

			protected RegisterFormEvent(RegisterForm source, BspUser user) {
				super(source, false);
				this.user = user;
			}

			public BspUser getUser() {
				return user;
			}
		}
		
		public static class SaveEvent extends RegisterFormEvent {
			SaveEvent(RegisterForm source, BspUser user) {
				super(source, user);
			}
		}
		
		public static class CloseEvent extends RegisterFormEvent {
			CloseEvent(RegisterForm source) {
				super(source, null);
			}
		}
		
		public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
			return addListener(SaveEvent.class, listener);
		}
		
		public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
			return addListener(CloseEvent.class, listener);
		}
}
