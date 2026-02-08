package com.airbnb.be.modals;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@RequiredArgsConstructor
@Document(collection = "users")
public class UsersDocument {

    @Id
    private String id;
    private String username;
    private String name;
    private String email;
    private String contactNumber;
    private String gender;
    private String createdOn;
    private String createdBy;
    private String lastUpdate;
    private Boolean isDeleted;

}

