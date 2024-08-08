package de.philipp.data.userstat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long>{
	
	@Query("SELECT u FROM UserStatistics u WHERE u.user.id = :userId")
	UserStatistics findByUserId(@Param("userId") Long userId);
}
