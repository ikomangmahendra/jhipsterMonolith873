package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AccuteProgramVariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AccuteProgramVariable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccuteProgramVariableRepository extends JpaRepository<AccuteProgramVariable, Long> {}
