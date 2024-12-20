package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FormVariable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FormVariable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormVariableRepository extends JpaRepository<FormVariable, Long> {}
