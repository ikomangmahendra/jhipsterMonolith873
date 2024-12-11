package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserTemp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserTemp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserTempRepository extends JpaRepository<UserTemp, Long> {}
