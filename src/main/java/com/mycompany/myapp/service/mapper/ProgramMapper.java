package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Program;
import com.mycompany.myapp.domain.UserTemp;
import com.mycompany.myapp.service.dto.ProgramDTO;
import com.mycompany.myapp.service.dto.UserTempDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Program} and its DTO {@link ProgramDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProgramMapper extends EntityMapper<ProgramDTO, Program> {
    @Mapping(target = "coordinators", source = "coordinators", qualifiedByName = "userTempIdSet")
    ProgramDTO toDto(Program s);

    @Mapping(target = "removeCoordinators", ignore = true)
    Program toEntity(ProgramDTO programDTO);

    @Named("userTempId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserTempDTO toDtoUserTempId(UserTemp userTemp);

    @Named("userTempIdSet")
    default Set<UserTempDTO> toDtoUserTempIdSet(Set<UserTemp> userTemp) {
        return userTemp.stream().map(this::toDtoUserTempId).collect(Collectors.toSet());
    }
}
