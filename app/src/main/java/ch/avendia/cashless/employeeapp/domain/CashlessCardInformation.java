package ch.avendia.cashless.employeeapp.domain;

import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.CashlessCard;
import ch.avendia.cashless.employeeapp.nfc.CashlessCardType;

/**
 * Created by Markus on 17.09.2015.
 */
public class CashlessCardInformation {

    private String uid;
    private CashlessCard cashlessCard;

    private List<Payable> payableList;
}
