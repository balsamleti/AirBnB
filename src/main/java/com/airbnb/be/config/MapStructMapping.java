package com.airbnb.be.config;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;

@MapperConfig(
        componentModel = "spring",
        injectionStrategy = CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        implementationPackage = "com.airbnb.be.mapper.impl"
)
public interface MapStructMapping {
}