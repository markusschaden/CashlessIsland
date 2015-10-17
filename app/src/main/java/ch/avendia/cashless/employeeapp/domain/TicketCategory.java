package ch.avendia.cashless.employeeapp.domain;

import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.Access;
import lombok.Data;
import lombok.ToString;

/**
 * Created by Markus on 17.09.2015.
 */
@Data
@ToString(callSuper = true)
public class TicketCategory implements Payable {

    private String name;
    private List<Access> accessList;
    private double price;


    public TicketCategory(String name, double price, List<Access> accesses) {
        this.name = name;
        this.accessList = accesses;
        this.price = price;
    }
}
