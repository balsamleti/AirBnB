package com.airbnb.be.modals;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@RequiredArgsConstructor
@Document(collection = "users")
public class UsersDocument {

    @Id
    private String userId;
    private String username;
    private String name;
    private String email;
    private String contactNumber;
    private String dateOfBirth;
    private String gender;
    private Date createdOn;
    private String createdBy;
    private String updatedBy;
    private Date lastUpdate;
    private Boolean deleted;

}

