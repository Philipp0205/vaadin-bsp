package de.philipp.views.login;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.login.AbstractLogin.LoginEvent;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import de.philipp.security.SecurityService;
import de.philipp.views.MainLayout;

@Route(value = "login", layout = MainLayout.class)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

	@Autowired
	private SecurityService securityService;

	private LoginForm login = new LoginForm();
	private Button registerButton = new Button("Register");

	public LoginView() {
		constructUi();
	}

	private void constructUi() {
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

		registerButton.addClickListener(event -> UI.getCurrent().navigate(RegisterView.class));

		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setWidth("300px");
		login.addLoginListener(this::handleLogin);
		add(login, registerButton);
	}

	private void handleLogin(LoginEvent e) {
		boolean isAuthenticated = securityService.login(e.getUsername(), e.getPassword());
		if (isAuthenticated) {
			UI.getCurrent().navigate("");
			// Get reference to MainLayout and call the afterLogin method
			MainLayout mainLayout = (MainLayout) UI.getCurrent().getChildren()
					.filter(component -> component instanceof MainLayout).findFirst().orElse(null);

			if (mainLayout != null) {
				mainLayout.updateProfileButton();
			}
		} else {
			login.setError(true);
		}
	}
}
