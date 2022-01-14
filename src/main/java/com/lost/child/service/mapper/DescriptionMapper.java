package com.lost.child.service.mapper;

import com.lost.child.domain.Description;
import com.lost.child.service.dto.DescriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Description} and its DTO {@link DescriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DescriptionMapper extends EntityMapper<DescriptionDTO, Description> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DescriptionDTO toDtoId(Description description);
}
