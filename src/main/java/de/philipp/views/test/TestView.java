package de.philipp.views.test;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import de.philipp.views.MainLayout;

@Route(value = "test", layout = MainLayout.class)
@AnonymousAllowed
public class TestView extends VerticalLayout {

	public TestView() {
		H1 h1 = new H1("Test");
		
		MuiButton button = new MuiButton("Test");
		MuiCard card = new MuiCard();
		add(h1, button, card);
	}
}
