package de.philipp.views.test;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;

@NpmPackage(value = "@mui/material", version = "5.16.5")
@NpmPackage(value = "@emotion/react", version = "11.13.0")
@NpmPackage(value = "@emotion/styled", version = "11.13.0")
@JsModule("./react/mui-card.tsx")
@Tag("mui-card")

public class MuiCard extends ReactAdapterComponent {
	
	public MuiCard() {
	}
}
