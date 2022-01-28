package com.lost.child.service.mapper;

import com.lost.child.domain.LastSeenAddress;
import com.lost.child.service.dto.LastSeenAddressDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LastSeenAddress} and its DTO {@link LastSeenAddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LastSeenAddressMapper extends EntityMapper<LastSeenAddressDTO, LastSeenAddress> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LastSeenAddressDTO toDtoId(LastSeenAddress lastSeenAddress);
}
