package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.AdditionalVariable;
import com.mycompany.myapp.domain.ProgramVersion;
import com.mycompany.myapp.domain.SiteOrgTemp;
import com.mycompany.myapp.service.dto.AdditionalVariableDTO;
import com.mycompany.myapp.service.dto.ProgramVersionDTO;
import com.mycompany.myapp.service.dto.SiteOrgTempDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AdditionalVariable} and its DTO {@link AdditionalVariableDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdditionalVariableMapper extends EntityMapper<AdditionalVariableDTO, AdditionalVariable> {
    @Mapping(target = "version", source = "version", qualifiedByName = "programVersionId")
    @Mapping(target = "site", source = "site", qualifiedByName = "siteOrgTempId")
    AdditionalVariableDTO toDto(AdditionalVariable s);

    @Named("programVersionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProgramVersionDTO toDtoProgramVersionId(ProgramVersion programVersion);

    @Named("siteOrgTempId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SiteOrgTempDTO toDtoSiteOrgTempId(SiteOrgTemp siteOrgTemp);
}
