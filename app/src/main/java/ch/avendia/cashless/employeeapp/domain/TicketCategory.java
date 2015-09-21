package ch.avendia.cashless.employeeapp.domain;

import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.Access;

/**
 * Created by Markus on 17.09.2015.
 */
public class TicketCategory implements Payable {

    private String name;
    private List<Access> accessList;
    private double price;


}
