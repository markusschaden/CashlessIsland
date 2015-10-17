package ch.avendia.cashless.employeeapp.domain;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Markus on 17.09.2015.
 */
@Data
@ToString(callSuper = true)
public class Ticket implements Payable {

    private TicketCategory ticketCategory;
    private String uid;
    private boolean active;

}
