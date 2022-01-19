package com.lost.child.service.mapper.extended;

import com.lost.child.domain.LastSeen;
import com.lost.child.service.dto.LastSeenDTO;
import com.lost.child.service.mapper.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link LastSeen} and its DTO {@link LastSeenDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LastSeensMapperExtended extends EntityMapper<LastSeenDTO, LastSeen> {
    @Override
    @Mapping(target = "child", ignore = true)
    LastSeenDTO toDto(LastSeen s);
}
