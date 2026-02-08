package com.airbnb.be.mapper;

import com.airbnb.be.config.MapStructMapping;
import com.airbnb.be.generated.users.User;
import com.airbnb.be.modals.Payload;
import com.airbnb.be.modals.UsersDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Date;

import static com.airbnb.be.api.ApiConstants.APP_NAME;

@Mapper(config = MapStructMapping.class, imports = Date.class)
public interface UsersMapper {

    UsersMapper MAPPER = Mappers.getMapper(UsersMapper.class);

    @Mapping(target = "name", source = "p.user.name")
    @Mapping(target = "email", source = "p.user.email")
    @Mapping(target = "gender", source = "p.user.gender")
    @Mapping(target = "contactNumber", source = "p.user.contactNumber")
    @Mapping(target = "createdOn", expression = "java(new Date())")
    @Mapping(target = "isDeleted", expression = "java(false)")
    @Mapping(target = "createdBy", constant = APP_NAME)
    @Mapping(target = "username", ignore = true)
    UsersDocument map(Payload p);

    User map(UsersDocument usersDocument);

}