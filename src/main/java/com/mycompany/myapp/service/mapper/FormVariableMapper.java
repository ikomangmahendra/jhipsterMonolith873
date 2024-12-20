package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.FormVariable;
import com.mycompany.myapp.service.dto.FormVariableDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FormVariable} and its DTO {@link FormVariableDTO}.
 */
@Mapper(componentModel = "spring")
public interface FormVariableMapper extends EntityMapper<FormVariableDTO, FormVariable> {}
