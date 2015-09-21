package ch.avendia.cashless.employeeapp.nfc;

/**
 * Created by Markus on 16.09.2015.
 */
public enum CashlessCardType {

    CLIENT (0, "Client"),
    EMPLOYEE (1, "Employee");


    int value;
    String name;

    CashlessCardType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public CashlessCardType getCashlessCardType(int value) {

        switch (value) {
            case 0: return CLIENT;
            case 1: return EMPLOYEE;
            default: return null;
        }
    }
}
