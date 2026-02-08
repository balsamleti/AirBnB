package com.airbnb.be.modals;

import com.airbnb.be.generated.GenericResponse;
import com.airbnb.be.generated.users.Bookings;
import com.airbnb.be.generated.users.Guest;
import com.airbnb.be.generated.users.User;
import lombok.*;

@Data
@Builder
@Generated
@AllArgsConstructor
@NoArgsConstructor
public class Payload {

    private RequestParams params;
    private GenericResponse genericResponse;
    private User user;
    private Guest guest;
    private Bookings bookings;
}
