package ch.avendia.cashless.employeeapp.nfc;

import java.util.List;

/**
 * Created by Markus on 16.09.2015.
 */
public class AccessCalculator {

    public int getAccessValue(List<Access> accesses) {

        int access = 0;

        for(Access a : accesses) {

            int value = (int)Math.pow(2, a.getAccessId());
            access = access | value;
        }

        return access;
    }


}
