package ch.avendia.cashless.employeeapp.view;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import ch.avendia.cashless.employeeapp.nfc.Access;
import ch.avendia.cashless.employeeapp.nfc.AccessCalculator;
import ch.avendia.cashless.employeeapp.nfc.CashlessCard;
import ch.avendia.cashless.employeeapp.nfc.dispatcher.CardHandler;

/**
 * A placeholder fragment containing a simple view.
 */
public class SecurityScanNFCFragment extends CashlessNfcCardFragment {

    private List<Access> items;
    private Access selectedAccess;
    private TextView mTextView;
    private LinearLayout mLinearLayout;
    private int accessCode = -1;

    public SecurityScanNFCFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_security_scannfc, container, false);

        mTextView = (TextView) rootView.findViewById(R.id.securityNFCTextView);
        mLinearLayout = (LinearLayout) rootView.findViewById(R.id.securityBorder);
        showDefaultView();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDefaultView();
            }
        });

        return rootView;
    }

    private void showDefaultView() {

        if (mTextView != null) {
            mTextView.setText("Scan NFC Tag \n(Access: " + accessCode + ")");
        }
        if (mLinearLayout != null) {
            mLinearLayout.setBackgroundColor(getResources().getColor(R.color.background));
        }
    }

    public void setAccessCode(int accessCode) {
        this.accessCode = accessCode;
        showDefaultView();
    }


    @Override
    public void cashlessCardDetected(Tag tag) {
        new NFCLoginReaderTask().execute(tag);
    }


    private class NFCLoginReaderTask extends AsyncTask<Tag, Void, CashlessCard> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "Scanning NFC Tag",
                    "Loading. Please wait...", true);

        }

        @Override
        protected CashlessCard doInBackground(Tag... params) {
            Tag tag = params[0];

            try {
                CardHandler cardHandler = new CardHandler(tag);
                CashlessCard cashlessCard = cardHandler.readCard();


                Log.i("NFC read", cashlessCard.toString());
                return cashlessCard;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(CashlessCard result) {
            if (result != null && isAdded()) {

                if (validateAccess(accessCode, result.getAccess())) {
                    mLinearLayout.setBackgroundColor(getResources().getColor(R.color.green));
                    mTextView.setText("OK");
                } else {
                    mLinearLayout.setBackgroundColor(getResources().getColor(R.color.red));
                    mTextView.setText("NOT");
                }

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showDefaultView();
                    }
                }, 1000);

            }

            dialog.dismiss();
        }
    }

    public boolean validateAccess(int accessCode, int customerAccess) {

        return AccessCalculator.isAuthorized(accessCode, customerAccess);
    }


}
