package de.philipp.views.quiz;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import de.philipp.data.category.Category;
import de.philipp.data.category.CategoryService;
import de.philipp.data.question.Question;
import de.philipp.data.user.BspUser;
import de.philipp.data.user.BspUserService;
import de.philipp.data.userstat.QuizSession;
import de.philipp.data.userstat.QuizSessionService;
import de.philipp.util.UserUtils;
import de.philipp.views.MainLayout;

@Route(value = "quiz", layout = MainLayout.class)
@PageTitle("Quiz")
@StyleSheet("./css/style.css")
@AnonymousAllowed 
public class QuizView extends VerticalLayout implements HasUrlParameter<String> {
	private static final Logger LOGGER = LogManager.getLogger(QuizView.class);
	
	private final CategoryService service;
	private final QuizSessionService sessionService;
	private final BspUserService userService;
	private final UserUtils userUtils;
	private final BspUser currentAuthenticatedUser;

	private Category category;
	private List<Question> questions;
	private Map<Integer, Boolean> results = new java.util.HashMap<Integer, Boolean>(); 
	TabSheet tabSheet; 
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private QuizSession currentSession;
	

	public QuizView(CategoryService service, QuizSessionService quizSessionService, BspUserService userService, UserUtils userUtils) {
		this.service = service;
		this.sessionService = quizSessionService;
		this.userService = userService;
		this.userUtils = userUtils;
		
		BspUser currentAuthenticatedUser = userUtils.getCurrentAuthenticatedUser();
		if (currentAuthenticatedUser != null) {
			this.currentAuthenticatedUser = currentAuthenticatedUser;
		} else {
			this.currentAuthenticatedUser = userUtils.getAnonymousUser();
		}
	}

	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		Long categoryId = Long.parseLong(parameter);
		category = service.findCategoryById(categoryId);
		questions = service.getQuestions(category);

		constructUI();
		startSession();
	}

	private void constructUI() {
		addClassNames("quiz-view");
		add(buildTitleAndImage(), buildQuestions());
	}
	
	private VerticalLayout buildTitleAndImage() {
		H2 title = new H2(category.getName());
		Image image = createImage(category.getImgUrl(), category.getImgAlt()); 
		image.setHeight("75px");
		image.setWidth("75px");

		VerticalLayout layout = new VerticalLayout(image, title);
		layout.setAlignItems(Alignment.CENTER); 
		layout.setJustifyContentMode(JustifyContentMode.START); 
		layout.setSpacing(true); 

		return layout; 
	}
	
	private TabSheet buildQuestions() {
		tabSheet = buildTabSheet();
		
		for (Question question : questions) {
			VerticalLayout questionLayout = buildQuestionLayout(question);
			buildAnswerButtons(questionLayout, question);
			buildButtonLayout(questionLayout, question);

			int index = questions.indexOf(question);
			tabSheet.add(String.valueOf(index + 1), questionLayout);
			tabSheet.getTabAt(index).setEnabled(false);
			
			// add result tab 
			if (index == questions.size() - 1) {
                VerticalLayout resultLayout = buildEmptyResultLayout();
                tabSheet.add("🏆", resultLayout);
                tabSheet.getTabAt(index + 1).setEnabled(false);
			}
		}		
		
		tabSheet.getTabAt(0).setEnabled(true);
		return tabSheet;
	}
	
	private void buildAnswerButtons(VerticalLayout questionLayout, Question question) {
		List<String> answers = new ArrayList<>(
				List.of(question.getCorrectAnswer(), question.getWrongAnswer1(), question.getWrongAnswer2()));
		Collections.shuffle(answers);
		
		List<Button> answerButtons = new ArrayList<Button>();

		for (String answer : answers) {
			Button answerButton = new Button(answer);
			answerButton.addClassName("answer-button");
			answerButton.setWidthFull();

			answerButton.addClickListener(e -> {
				if (answer.equals(question.getCorrectAnswer())) {
					answerButton.addClassName("correct-answer");
					tabSheet.getTabAt(tabSheet.getSelectedIndex()).addClassName("correct-tab");
					results.put(questions.indexOf(question), true);
				} else {
					answerButton.addClassName("wrong-answer");
					tabSheet.getTabAt(tabSheet.getSelectedIndex()).addClassName("wrong-tab");
					results.put(questions.indexOf(question), false);
				}
				disableOtherButtons(answerButton, answerButtons);
				moveToNextTab();
			});
			answerButtons.add(answerButton);
		}
		questionLayout.add(answerButtons.toArray(new Component[0]));
	}

	private void moveToNextTab() {
		int currentIndex = tabSheet.getSelectedIndex();
		int nextIndex = currentIndex + 1;
		tabSheet.getTabAt(nextIndex).setEnabled(true);
		
		if (currentIndex < questions.size() - 1) {
			switchTab(nextIndex);
		} else {
			stopSession();
			updateResultLayout();
			switchTab(nextIndex);
		}
	}
	
	private void switchTab(int index) {
			getUI().ifPresent(ui -> ui.access(() -> {
				ui.setPollInterval(500); // Polling interval set to match animation duration
				ui.addPollListener(e -> {
					ui.access(() -> {
						tabSheet.setSelectedIndex(index);
						ui.setPollInterval(-1); // Stop polling after update
					});
				});
			}));
	}

	private void disableOtherButtons(Button answerButton, List<Button> answerButtons) {
		for (Button button : answerButtons) {
			if (!button.equals(answerButton)) {
				button.setEnabled(false);
			}
		}
	}

	private VerticalLayout buildQuestionLayout(Question question) {
		VerticalLayout layout = new VerticalLayout();
		H2 questionText = new H2(question.getQuestionText());
		layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		layout.add(questionText);
		return layout;
	}

	private TabSheet buildTabSheet() {
		tabSheet = new TabSheet();
		tabSheet.getStyle().set("margin", "auto");
		tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_CENTERED);
		tabSheet.setWidthFull();
		return tabSheet;
	}

	private void buildButtonLayout(VerticalLayout questionLayout, Question question) {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidthFull();
		buttonLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

		Button previous = new Button(new Icon(VaadinIcon.ARROW_BACKWARD));
		previous.addClickListener(e -> {
			int selectedIndex = tabSheet.getSelectedIndex();
			if (selectedIndex > 0) {
				tabSheet.setSelectedIndex(selectedIndex - 1);
			}
		});

		Button next = new Button(new Icon(VaadinIcon.ARROW_FORWARD));
		next.addClickListener(e -> {
			int selectedIndex = tabSheet.getSelectedIndex();
			if (selectedIndex < questions.size() - 1) {
				tabSheet.setSelectedIndex(selectedIndex + 1);
			}
		});
		
		buttonLayout.add(previous, next);
		questionLayout.add(buttonLayout);
	}

	private Image createImage(String url, String alt) {
		Image image = new Image(url, alt);
		image.setHeight("300px");
		image.getStyle().set("object-fit", "cover");
		image.getStyle().set("width", "auto");
		image.getStyle().set("border-radius", "10px");
		image.setWidthFull();
		return image;
	}
	
	private VerticalLayout buildEmptyResultLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth("33%");
		layout.add(new H2("Keine Fragen gefunden"));
		return layout;
	}
	
	private VerticalLayout buildResultLayout() {
		VerticalLayout verticalLayout = new VerticalLayout();

		H2 title = new H2("Glückwunsch!");
		int percentageOfCorretAnswers = (int) ((results.values().stream().filter(Boolean::booleanValue).count() * 100.0) / questions.size());
		
		
		verticalLayout.setWidth("33%");
		ResultBadge resultBadge = new ResultBadge(BadgeType.getByScore(percentageOfCorretAnswers));
		ResultBadge timeBadge = new ResultBadge(VaadinIcon.CLOCK.create(), "Dauer", getTimeTaken(), "blue");
		
		HorizontalLayout badgeLayout = new HorizontalLayout(resultBadge, timeBadge);
		
		Button restartButton = new Button("Neustart");
		restartButton.addClickListener(e -> {
			restart();
		});
		
		Button finishButton = new Button("Fertig");
		finishButton.addClickListener(e -> {
			UI.getCurrent().navigate(CategoryView.class);
		});
		
		HorizontalLayout buttonLayout = new HorizontalLayout(restartButton, finishButton);
		buttonLayout.setJustifyContentMode(JustifyContentMode.EVENLY);

		verticalLayout.add(title, badgeLayout, buttonLayout);
		return verticalLayout;
	}

	private String getTimeTaken() {
		Duration duration = sessionService.getDuration(currentSession);
		String time = String.format("%d:%02d", duration.toMinutesPart(), duration.toSecondsPart());
		return time;
	}
	
	private void updateResultLayout() {
		VerticalLayout layout = buildResultLayout();
		layout.setWidthFull();
		layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

		// Replace the last tab's content with the updated result layout
		int resultTabIndex = questions.size();

		// add result tab
		tabSheet.remove(resultTabIndex);
		tabSheet.add("🏆", layout);
	}

	private void restart() {
		if (currentSession != null && currentSession.getEndTime() == null) {
			stopSession(); // End the current session before restarting
		}
		
		startSession();
		remove(tabSheet);
		this.tabSheet = buildTabSheet();
		add(buildQuestions());
	}
	

	
	private void startSession() {
		assertUserIsAuthenticated();
		currentSession = new QuizSession();
		currentSession.setCategory(category);
		currentSession.setStartTime(java.time.LocalDateTime.now());
		currentSession.setUser(currentAuthenticatedUser);
		
		sessionService.save(currentSession);
	}
	
	private void stopSession() {
		assertUserIsAuthenticated();
		currentSession.setEndTime(java.time.LocalDateTime.now());
		currentSession.setScore((int) ((results.values().stream().filter(Boolean::booleanValue).count() * 100.0) / questions.size()));
		sessionService.save(currentSession);
	}
	
	private void assertUserIsAuthenticated() {
		if (currentAuthenticatedUser == null) {
			return;
		}
		LOGGER.info("User is authenticated: " + currentAuthenticatedUser.getEmail());
	}
}
	

