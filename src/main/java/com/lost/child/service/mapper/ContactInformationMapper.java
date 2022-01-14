package com.lost.child.service.mapper;

import com.lost.child.domain.ContactInformation;
import com.lost.child.service.dto.ContactInformationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactInformation} and its DTO {@link ContactInformationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ContactInformationMapper extends EntityMapper<ContactInformationDTO, ContactInformation> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactInformationDTO toDtoId(ContactInformation contactInformation);
}
