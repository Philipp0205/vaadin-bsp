package de.philipp.data.userstat;

import org.springframework.stereotype.Service;

@Service
public class UserStatisticsService {
	
	private final UserStatisticsRepository userStatisticsRepository;
	
	public UserStatisticsService(UserStatisticsRepository userStatisticsRepository) {
		this.userStatisticsRepository = userStatisticsRepository;
	}
	
	public UserStatistics findUserStatisticsByUserId(Long userId) {
		return userStatisticsRepository.findByUserId(userId);
	}
}
