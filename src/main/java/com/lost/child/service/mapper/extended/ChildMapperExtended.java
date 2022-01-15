package com.lost.child.service.mapper.extended;

import com.lost.child.domain.Child;
import com.lost.child.service.dto.ChildDTO;
import com.lost.child.service.dto.extended.ChildDTOExtended;
import com.lost.child.service.mapper.*;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Child} and its DTO {@link ChildDTO}.
 */
@Mapper(componentModel = "spring", uses = { LastSeensMapperExtended.class })
public interface ChildMapperExtended extends EntityMapper<ChildDTOExtended, Child> {}
