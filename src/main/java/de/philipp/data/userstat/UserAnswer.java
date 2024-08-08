package de.philipp.data.userstat;

import de.philipp.data.question.Question;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sessionId")
    private QuizSession session;

    @ManyToOne
    @JoinColumn(name = "questionId")
    private Question question;

    private String selectedAnswer;
    private boolean correct;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public QuizSession getSession() {
		return session;
	}
	public void setSession(QuizSession session) {
		this.session = session;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public String getSelectedAnswer() {
		return selectedAnswer;
	}
	public void setSelectedAnswer(String selectedAnswer) {
		this.selectedAnswer = selectedAnswer;
	}
	public boolean isCorrect() {
		return correct;
	}
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
