package com.airbnb.be.mapper;

import com.airbnb.be.config.MapStructMapping;
import com.airbnb.be.generated.GenericResponse;
import com.airbnb.be.modals.UsersDocument;
import com.airbnb.be.utils.ABUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static com.airbnb.be.api.ApiConstants.*;

@Mapper(config = MapStructMapping.class, imports = ABUtils.class)
public interface GenericResponseMapper {

    GenericResponseMapper MAPPER = Mappers.getMapper(GenericResponseMapper.class);

    @Mapping(target = "id", source = "doc.id")
    @Mapping(target = "idType",  constant = USER_ID)
    @Mapping(target = "responseDetails.responseCode", constant = SUCCESS_VALUE)
    @Mapping(target = "responseDetails.dateTime", expression = "java(ABUtils.getSysTime())")
    @Mapping(target = "responseDetails.responseMessage", constant = SUCCESS)
    GenericResponse map(UsersDocument doc);

}