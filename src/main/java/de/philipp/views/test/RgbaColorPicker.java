package de.philipp.views.test;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.react.ReactAdapterComponent;
import com.vaadin.flow.function.SerializableConsumer;

@NpmPackage(value = "react-colorful", version = "5.6.1") 
@JsModule("./react/rgba-color-picker.tsx") 
@Tag("rgba-color-picker") 
public class RgbaColorPicker extends ReactAdapterComponent {
	
	public record RgbaColor(int r, int g, int b, double a) {

		@Override
		public String toString() {
			return "{ r: %s, g: %s, b: %s, a: %s }".formatted(r, g, b, a);
		}
	}
	
	public RgbaColorPicker() {
		setColor(new RgbaColor(255, 0, 0, 0.5));
	}

	public RgbaColor getColor() {
		return getState("color", RgbaColor.class);
	}

	public void setColor(RgbaColor color) {
		setState("color", color);
	}
	
	public void addColorChangeListener(SerializableConsumer<RgbaColor> listener) {
		addStateChangeListener("color", RgbaColor.class, listener);
	}
}