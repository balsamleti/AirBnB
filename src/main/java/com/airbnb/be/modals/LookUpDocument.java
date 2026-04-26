package com.airbnb.be.modals;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@RequiredArgsConstructor
@Document(collection = "reference")
public class LookUpDocument {
    @Id
    private String type;
    private Map<Object, Object> data;
}