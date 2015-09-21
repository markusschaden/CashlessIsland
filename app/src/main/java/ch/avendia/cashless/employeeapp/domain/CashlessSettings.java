package ch.avendia.cashless.employeeapp.domain;

import java.io.Serializable;

/**
 * Created by Markus on 17.09.2015.
 */
public class CashlessSettings implements Serializable {

    private String nfcKey;
    private String session;
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getNfcKey() {
        return nfcKey;
    }

    public void setNfcKey(String nfcKey) {
        this.nfcKey = nfcKey;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }
}
