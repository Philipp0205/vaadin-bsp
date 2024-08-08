package de.philipp.views.quiz;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


@StyleSheet("./css/result-badge.css")
public class ResultBadge extends VerticalLayout {
	Icon icon; 
	String title;
	String value;
	String color; 


	public ResultBadge(BadgeType badgeType) {
		this(badgeType.getIcon().create(), badgeType.getLabel(), badgeType.getPercentage(), badgeType.getColor());
	}
	
	public ResultBadge(Icon icon, String title, String value, String color) {
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);
		setWidth("200px");

		this.icon = icon;
		this.title = title;
		this.value = value;
		this.color = color;

		createBadge();

	}

	private void createBadge() {
		Div valueLabel = new Div();
		valueLabel.setText(value);
		valueLabel.addClassName("result-value");

		Div titleLabel = new Div();
		titleLabel.setText(title);
		titleLabel.addClassName("result-title");

		add(icon, valueLabel, titleLabel);
		addClassName("result-badge");
		getStyle().set("background-color", color);
	}
}

enum BadgeType {
    FANTASTISCH("Fantastisch", "100%", "green", VaadinIcon.SMILEY_O),
    GUT("Gut", "75%", "blue", VaadinIcon.THUMBS_UP),
    BEFRIEDIGEND("Befriedigend", "50%", "orange", VaadinIcon.THUMBS_DOWN),
    AUSREICHEND("Ausreichend", "25%", "red", VaadinIcon.MINUS);
	

    private final String label;
    private final String percentage;
    private final String color;
    private final VaadinIcon icon;

    BadgeType(String label, String percentage, String color, VaadinIcon icon) {
        this.label = label;
        this.percentage = percentage;
        this.color = color;
        this.icon = icon;
    }
      
    public String getLabel() {
        return label;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getColor() {
        return color;
    }

    public VaadinIcon getIcon() {
        return icon;
    }

	public static BadgeType getByScore(int score) {
		if (score >= 75) {
			return FANTASTISCH;
		} else if (score >= 50) {
			return GUT;
		} else if (score >= 25) {
			return BEFRIEDIGEND;
		} else {
			return AUSREICHEND;
		}
	}
}