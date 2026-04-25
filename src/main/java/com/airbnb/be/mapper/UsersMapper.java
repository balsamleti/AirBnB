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

@Mapper(imports = Date.class, implementationPackage = "com.airbnb.be.mapper.impl")
public interface UsersMapper {

    UsersMapper USER_MAPPER = Mappers.getMapper(UsersMapper.class);

    @Mapping(target = "name", source = "p.user.name")
    @Mapping(target = "email", source = "p.user.email")
    @Mapping(target = "gender", source = "p.user.gender")
    @Mapping(target = "dateOfBirth", source = "p.user.dateOfBirth")
    @Mapping(target = "contactNumber", source = "p.user.contactNumber")
    @Mapping(target = "createdOn", expression = "java(new Date())")
    @Mapping(target = "createdBy", constant = APP_NAME)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    UsersDocument map(Payload p);

    @Mapping(target = "id", ignore = true)
    User map(UsersDocument usersDocument);

    @Mapping(target = "name", source = "p.user.name")
    @Mapping(target = "gender", source = "p.user.gender")
    @Mapping(target = "dateOfBirth", source = "p.user.dateOfBirth")
    @Mapping(target = "deleted", source = "p.user.deleted")
    UsersDocument forUpdate(Payload p);

}