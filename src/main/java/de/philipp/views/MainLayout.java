package de.philipp.views;

import org.springframework.security.core.userdetails.UserDetails;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;

import de.philipp.views.login.LoginView;

@StyleSheet("./css/style.css")
public class MainLayout extends AppLayout {

    ContextMenu contextMenu;
    private final transient AuthenticationContext authContext;
	
	public MainLayout(AuthenticationContext authContext) {
		this.authContext = authContext;
		setPrimarySection(Section.DRAWER);
		addHeaderContent();
	}

	private void addHeaderContent() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.addClassName("header");
		layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		layout.setSizeFull();
		
		H2 title = new H2("BSP Quiz");
		layout.add(title, profilePicture());

		addToNavbar(true, layout);
	}

	private Button profilePicture() {
		Button button = new Button(new Icon(VaadinIcon.USER));
		button.addClassName("profile-button");
		contextMenu = new ContextMenu(button);
		contextMenu.setOpenOnClick(true); // This will make the menu open on a left-click

		contextMenu.addItem("Profile", e -> {
			// Handle the click on the "Profile" item
		});

		contextMenu.addItem("Settings", e -> {
			// Handle the click on the "Settings" item
		});

		updateLoginOrLogoutMenuItem();
		
		return button;
	}

    private void updateLoginOrLogoutMenuItem() {
        contextMenu.removeAll(); // Clear existing items

        boolean isLoggedIn = authContext.getAuthenticatedUser(UserDetails.class).isPresent();

        if (isLoggedIn) {
            contextMenu.addItem("Logout", e -> {
                authContext.logout();
                Notification.show("User erfolgreich ausgeloggt");
                updateLoginOrLogoutMenuItem(); // Update menu items after logout
            });
        } else {
            contextMenu.addItem("Login", e -> {
                UI.getCurrent().navigate(LoginView.class);
            });
        }
    }
	public void updateProfileButton() {
		profilePicture();
	}
}
