package de.philipp.data.userstat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
	
	@Query("SELECT u FROM QuizSession u WHERE u.user.id = :userId")
	QuizSession findByUserId(@Param("userId") Long userId);
}
