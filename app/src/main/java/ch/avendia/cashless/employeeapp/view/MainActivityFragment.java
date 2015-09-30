package ch.avendia.cashless.employeeapp.view;

import android.nfc.Tag;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends CashlessNfcCardFragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        return rootView;
    }


    @Override
    public void cashlessCardDetected(Tag tag) {

    }
}
