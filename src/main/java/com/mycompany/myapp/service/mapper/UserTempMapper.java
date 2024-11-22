package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Program;
import com.mycompany.myapp.domain.UserTemp;
import com.mycompany.myapp.service.dto.ProgramDTO;
import com.mycompany.myapp.service.dto.UserTempDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserTemp} and its DTO {@link UserTempDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserTempMapper extends EntityMapper<UserTempDTO, UserTemp> {
    @Mapping(target = "programs", source = "programs", qualifiedByName = "programIdSet")
    UserTempDTO toDto(UserTemp s);

    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "removeProgram", ignore = true)
    UserTemp toEntity(UserTempDTO userTempDTO);

    @Named("programId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProgramDTO toDtoProgramId(Program program);

    @Named("programIdSet")
    default Set<ProgramDTO> toDtoProgramIdSet(Set<Program> program) {
        return program.stream().map(this::toDtoProgramId).collect(Collectors.toSet());
    }
}
