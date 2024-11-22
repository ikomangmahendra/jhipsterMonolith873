package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Program;
import com.mycompany.myapp.domain.ProgramVersion;
import com.mycompany.myapp.service.dto.ProgramDTO;
import com.mycompany.myapp.service.dto.ProgramVersionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProgramVersion} and its DTO {@link ProgramVersionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgramVersionMapper extends EntityMapper<ProgramVersionDTO, ProgramVersion> {
    @Mapping(target = "program", source = "program", qualifiedByName = "programId")
    ProgramVersionDTO toDto(ProgramVersion s);

    @Named("programId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProgramDTO toDtoProgramId(Program program);
}
