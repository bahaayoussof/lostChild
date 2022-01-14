package com.lost.child.service.mapper;

import com.lost.child.domain.Child;
import com.lost.child.service.dto.ChildDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Child} and its DTO {@link ChildDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = { AddressMapper.class, DescriptionMapper.class, ContactInformationMapper.class, UserMapper.class }
)
public interface ChildMapper extends EntityMapper<ChildDTO, Child> {
    @Mapping(target = "address", source = "address", qualifiedByName = "id")
    @Mapping(target = "description", source = "description", qualifiedByName = "id")
    @Mapping(target = "contactInformation", source = "contactInformation", qualifiedByName = "id")
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    ChildDTO toDto(Child s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChildDTO toDtoId(Child child);
}
