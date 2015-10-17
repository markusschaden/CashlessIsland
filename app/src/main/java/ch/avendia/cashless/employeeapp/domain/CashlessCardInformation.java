package ch.avendia.cashless.employeeapp.domain;

import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.CashlessCard;
import ch.avendia.cashless.employeeapp.nfc.CashlessCardType;
import lombok.Data;
import lombok.ToString;

/**
 * Created by Markus on 17.09.2015.
 */
@Data
@ToString(callSuper = true)
public class CashlessCardInformation {

    private String uid;
    private CashlessCard cashlessCard;

    private List<Payable> payableList;
}
