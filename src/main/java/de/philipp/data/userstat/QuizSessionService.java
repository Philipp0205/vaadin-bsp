package de.philipp.data.userstat;

import java.time.Duration;

import org.springframework.stereotype.Service;

@Service
public class QuizSessionService {
	
	private final QuizSessionRepository quizSessionRepository;

	public QuizSessionService(QuizSessionRepository quizSessionRepository) {
		this.quizSessionRepository = quizSessionRepository;
	}

	public QuizSession findQuizSessionByUserId(Long userId) {
		return quizSessionRepository.findByUserId(userId);
	}
	
	public QuizSession save(QuizSession quizSession) {
		return quizSessionRepository.save(quizSession);
	}
	
	public Duration getDuration(QuizSession quizSession) {
		return Duration.between(quizSession.getStartTime(), quizSession.getEndTime());
	}
}
