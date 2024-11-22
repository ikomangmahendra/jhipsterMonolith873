package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.SiteOrgTemp;
import com.mycompany.myapp.service.dto.SiteOrgTempDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SiteOrgTemp} and its DTO {@link SiteOrgTempDTO}.
 */
@Mapper(componentModel = "spring")
public interface SiteOrgTempMapper extends EntityMapper<SiteOrgTempDTO, SiteOrgTemp> {}
