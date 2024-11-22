package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AdditionalVariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AdditionalVariable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdditionalVariableRepository extends JpaRepository<AdditionalVariable, Long> {}
