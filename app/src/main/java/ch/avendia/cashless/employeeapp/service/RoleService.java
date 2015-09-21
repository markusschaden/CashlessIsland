package ch.avendia.cashless.employeeapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.avendia.cashless.employeeapp.domain.Role;
import ch.avendia.cashless.employeeapp.domain.RoleName;
import ch.avendia.cashless.employeeapp.nfc.Access;

/**
 * Created by Markus on 17.09.2015.
 */
public class RoleService {

    public List<Role> getRoles() {

        List<Role> roles = new ArrayList<>();
        roles.add(getAdminRole());
        roles.add(getSecurityRole());
        roles.add(getEntranceRole());

        return roles;
    }

    public static Role getAdminRole() {
        Role adminRole = new Role();
        adminRole.setName(RoleName.ADMIN);
        adminRole.setAccessList(Arrays.asList(AccessService.getMainArea(), AccessService.getBackstage(), AccessService.getMaintenance()));

        return adminRole;
    }

    public static Role getSecurityRole() {
        Role adminRole = new Role();
        adminRole.setName(RoleName.SECURITY);
        adminRole.setAccessList(Arrays.asList(AccessService.getMainArea(), AccessService.getBackstage()));

        return adminRole;
    }

    public static Role getEntranceRole() {
        Role adminRole = new Role();
        adminRole.setName(RoleName.ENTRANCE);
        adminRole.setAccessList(Arrays.asList(AccessService.getMainArea()));

        return adminRole;
    }

}
