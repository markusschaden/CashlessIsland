package ch.avendia.cashless.employeeapp.view;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ch.avendia.cashless.employeeapp.nfc.Access;

public class SecurityActivity extends DefaultActivity implements SecurityActivityFragment.OnAccessSelected {

    private Fragment accessChooser = new SecurityActivityFragment();
    private SecurityScanNFCFragment securityNFCScan = new SecurityScanNFCFragment();
    private int accessCode = -1;
    private FragmentManager fragmentManager;
    public static final String ACCESS_CODE_BUNDLE = "Access_Code_Bundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_security);
        setupNfc();

        if (savedInstanceState != null) {
            accessCode = savedInstanceState.getInt(ACCESS_CODE_BUNDLE, -1);
        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (accessCode == -1) {
            fragmentTransaction.add(R.id.fragment_container, accessChooser).addToBackStack(null);
        } else {
            fragmentTransaction.add(R.id.fragment_container, securityNFCScan).addToBackStack(null);
            securityNFCScan.setAccessCode(accessCode);
        }
        fragmentTransaction.commit();

    }


    protected void handleIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


            securityNFCScan.cashlessCardDetected(tag);
        }
    }


    @Override
    public void onAccessSelected(Access acccess) {
        accessCode = acccess.getAccessId();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, securityNFCScan).addToBackStack(null);
        securityNFCScan.setAccessCode(accessCode);
        fragmentTransaction.commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable(ACCESS_CODE_BUNDLE, accessCode);
    }
}
