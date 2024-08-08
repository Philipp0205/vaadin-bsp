package de.philipp.data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BspUserRepository extends JpaRepository<BspUser, Long> {

	@Query("SELECT u FROM BspUser u WHERE lower(u.email) = lower(:email)")
	BspUser findByEmail(@Param("email") String email);
}
