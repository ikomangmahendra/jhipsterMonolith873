package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AccuteProgramVariable;
import com.mycompany.myapp.domain.ProgramVersion;
import com.mycompany.myapp.service.dto.AccuteProgramVariableDTO;
import com.mycompany.myapp.service.dto.ProgramVersionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccuteProgramVariable} and its DTO {@link AccuteProgramVariableDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccuteProgramVariableMapper extends EntityMapper<AccuteProgramVariableDTO, AccuteProgramVariable> {
    @Mapping(target = "version", source = "version", qualifiedByName = "programVersionId")
    AccuteProgramVariableDTO toDto(AccuteProgramVariable s);

    @Named("programVersionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProgramVersionDTO toDtoProgramVersionId(ProgramVersion programVersion);
}
