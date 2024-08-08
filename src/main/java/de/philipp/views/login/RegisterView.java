package de.philipp.views.login;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import de.philipp.data.user.BspUser;
import de.philipp.data.user.BspUserService;
import de.philipp.views.MainLayout;
import de.philipp.views.login.RegisterForm.SaveEvent;

@Route(value = "register", layout = MainLayout.class)
@AnonymousAllowed
public class RegisterView extends VerticalLayout {
	
	BspUserService userService;
		RegisterForm form; 

	public RegisterView(BspUserService userService) {
		this.userService = userService;
		constructUI();
	}

	private void constructUI() {
		setSizeFull();
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		
		configureForm();
		
		VerticalLayout formLayout = new VerticalLayout();
		formLayout.setWidth("300px");
		formLayout.add(form);
		add(formLayout);
	}
	
	private void configureForm() {
		form = new RegisterForm();
		form.addSaveListener(this::registerUser);
		
	}

	private void registerUser(SaveEvent event) {
		BspUser user = event.getUser();
		boolean saveUser = userService.saveUser(user);
	}
}
