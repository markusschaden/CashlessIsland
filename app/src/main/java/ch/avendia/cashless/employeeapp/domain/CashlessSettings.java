package ch.avendia.cashless.employeeapp.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Markus on 17.09.2015.
 */
@Data
@ToString(callSuper = true)
public class CashlessSettings implements Serializable {

    private String nfcKey;
    private String session;
    private Employee employee;
}
