package com.lost.child.service.mapper;

import com.lost.child.domain.LastSeen;
import com.lost.child.service.dto.LastSeenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LastSeen} and its DTO {@link LastSeenDTO}.
 */
@Mapper(componentModel = "spring", uses = { LastSeenAddressMapper.class, ChildMapper.class })
public interface LastSeenMapper extends EntityMapper<LastSeenDTO, LastSeen> {
    @Mapping(target = "lastSeenAddress", source = "lastSeenAddress", qualifiedByName = "id")
    @Mapping(target = "child", source = "child", qualifiedByName = "id")
    LastSeenDTO toDto(LastSeen s);
}
