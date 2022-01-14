package com.lost.child.service.mapper;

import com.lost.child.domain.LastSeen;
import com.lost.child.service.dto.LastSeenDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LastSeen} and its DTO {@link LastSeenDTO}.
 */
@Mapper(componentModel = "spring", uses = { AddressMapper.class, ChildMapper.class })
public interface LastSeenMapper extends EntityMapper<LastSeenDTO, LastSeen> {
    @Mapping(target = "address", source = "address", qualifiedByName = "id")
    @Mapping(target = "child", source = "child", qualifiedByName = "id")
    LastSeenDTO toDto(LastSeen s);
}
