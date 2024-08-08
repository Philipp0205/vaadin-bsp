package de.philipp.views.quiz;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

import de.philipp.data.category.Category;
import de.philipp.data.category.CategoryService;

public class CategoryCard extends ListItem {
	Image image;
	Span header;
	CategoryService service;

	public CategoryCard(Category category, CategoryService service) {
		this.service = service;
		addClassNames("category-card", Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START, Padding.MEDIUM,
				BorderRadius.LARGE);

		Div div = new Div();
		div.addClassNames(Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
				Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
		div.setHeight("160px");

		image = new Image(category.getImgUrl(), category.getImgAlt());
		image.setWidth("30%");
		
		div.add(image);
		header = new Span();
		header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
		header.setText(category.getName());

		add(div, header);
	}
}
