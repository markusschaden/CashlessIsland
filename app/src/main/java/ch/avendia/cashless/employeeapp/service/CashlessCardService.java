package ch.avendia.cashless.employeeapp.service;

import ch.avendia.cashless.employeeapp.domain.CashlessCardInformation;

/**
 * Created by Markus on 17.09.2015.
 */
public class CashlessCardService {

    public String generateUID(String nfcCardId) {
        return java.util.UUID.randomUUID().toString();
    }

    public CashlessCardInformation getInformation(String uid) {



        return null;
    }


}
