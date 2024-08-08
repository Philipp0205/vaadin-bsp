package de.philipp.views.quiz;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import de.philipp.data.category.CategoryService;
import de.philipp.views.MainLayout;

@PageTitle("Event Overview")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed 
public class CategoryView extends Main implements HasComponents, HasStyle {

	private CategoryService categoryService;
	private FlexLayout flexLayout;
	
	public CategoryView(CategoryService categoryService) {
		this.categoryService = categoryService;
		constructUI();
	}
	
	private void constructUI() {
		addClassNames("event-overview-view");
		addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

		add(buildHeader(), buildCategories());
	}

	private HorizontalLayout buildHeader() {
		HorizontalLayout container = new HorizontalLayout();
		container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

		VerticalLayout headerContainer = new VerticalLayout();
		H2 header = new H2("Kategorien");
		header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
		headerContainer.add(header);

		container.add(headerContainer);
		return container;
	}

	private FlexLayout buildCategories() {
		flexLayout = new FlexLayout();
		addImageContainerToLayout();
		flexLayout.setFlexWrap(FlexWrap.WRAP);
		flexLayout.addClassNames(Gap.MEDIUM);
		flexLayout.setWidthFull(); // Ensure the layout takes full width
		flexLayout.getStyle().set("display", "grid");
		flexLayout.getStyle().set("grid-template-columns", "repeat(auto-fill, minmax(300px, 1fr))");
		
		return flexLayout;
	}

	private void addImageContainerToLayout() {
		categoryService.findAllCategories("").forEach(category -> {
			CategoryCard card = new CategoryCard(category, categoryService);
			card.addClickListener(e -> {
				card.getUI().ifPresent(ui -> ui.navigate(QuizView.class, category.getId().toString()));
			});
			flexLayout.add(card);
		});
	}
}
