package com.markusschaden.gadgeothek;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.Access;
import ch.avendia.cashless.employeeapp.nfc.AccessCalculator;
import ch.avendia.cashless.employeeapp.nfc.dispatcher.CardHandler;

/**
 * A placeholder fragment containing a simple view.
 */
public class AdminFragment extends CashlessNfcCardFragment {

    private ProgressDialog dialog;
    private AsyncTask<Tag, Void, Boolean> currentAsyncTask;

    public AdminFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin, container, false);

        Button button = (Button) rootView.findViewById(R.id.writeEmployeeTag);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentAsyncTask = new MifareEmployeeWriter();
                showWaitingDialog();

            }
        });

        return rootView;
    }



    private void showWaitingDialog() {

        dialog = ProgressDialog.show(getActivity(), "Please wait...",
                "Tab NFC Tag", true);

    }


    private class MifareEmployeeWriter extends AsyncTask<Tag, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Writing to Tag...");

        }

        @Override
        protected Boolean doInBackground(Tag... params) {
            Tag tag = params[0];

            try {
                CardHandler cardHandler = new CardHandler(tag);
                cardHandler.reset();
                List<Access> accessList = new ArrayList<>();
                accessList.add(new Access(1, "Main"));
                accessList.add(new Access(2, "Backstage"));

                AccessCalculator accessCalculator = new AccessCalculator();
                int access = accessCalculator.getAccessValue(accessList);

                cardHandler.writeEmployeeCard("0ce28b7e-1ac6-49f7-9d78-c257a7b682ce", 4567, access, "190bf7c4-f729-4b3c-8c22-eecc57a03b68", 9999);


            } catch (IOException e) {
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            dialog.dismiss();
        }

    }


    @Override
    public void cashlessCardDetected(Tag tag) {
        if(currentAsyncTask != null) {
            currentAsyncTask.execute(tag);
        }
    }
}
