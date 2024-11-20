package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Consumer;
import com.mycompany.myapp.service.dto.ConsumerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Consumer} and its DTO {@link ConsumerDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConsumerMapper extends EntityMapper<ConsumerDTO, Consumer> {}
