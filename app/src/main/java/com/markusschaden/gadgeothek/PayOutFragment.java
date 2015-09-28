package com.markusschaden.gadgeothek;

import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import ch.avendia.cashless.employeeapp.nfc.dispatcher.CardHandler;

/**
 * A placeholder fragment containing a simple view.
 */
public class PayOutFragment extends CashlessNfcCardFragment {

    private ProgressDialog dialog;
    private AsyncTask<Tag, Void, Integer> cashReaderAsyncTask;
    private AsyncTask<Tag, Void, Boolean> cashWriterAsyncTask;
    private Button payoutButton;
    private TextView nfcInfo;
    private boolean payout = false;
    private Tag tag;
    private final String nfcInfoText = "Tab NFC Tag";

    public PayOutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pay_out, container, false);

        nfcInfo = (TextView) rootView.findViewById(R.id.nfcInfo);

        payoutButton = (Button) rootView.findViewById(R.id.payout);

        payoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showWaitingDialog();
                payout = true;
            }
        });

        return rootView;
    }


    private void showWaitingDialog() {

        dialog = ProgressDialog.show(getActivity(), "Please wait...",
                "Tab NFC Tag", true);

    }


    private class CashReader extends AsyncTask<Tag, Void, Integer> {

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "Please wait...",
                    "Reading from Tag...", true);
        }

        @Override
        protected Integer doInBackground(Tag... params) {
            Tag tag = params[0];
            int cash = 0;
            try {
                CardHandler cardHandler = new CardHandler(tag);

                cash = cardHandler.getCash();

            } catch (IOException e) {
                e.printStackTrace();

            }


            return cash;
        }

        @Override
        protected void onPostExecute(Integer cash) {
            nfcInfo.setText("Cash: " + cash + "\n");

            dialog.dismiss();

            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
            fadeIn.setDuration(500);

            payoutButton.startAnimation(fadeIn);
            payoutButton.setVisibility(View.VISIBLE);
        }

    }


    private class CashWriter extends AsyncTask<Tag, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Writing to Tag...");

        }

        @Override
        protected Boolean doInBackground(Tag... params) {
            Tag tag = params[0];

            try {
                CardHandler cardHandler = new CardHandler(tag);

                int cash = cardHandler.getCash();
                Log.i("Cash Pay", "Old cash:" + cash + ", newCash: " + 0);
                cash = 0;
                cardHandler.writeCash(cash);

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            if (aVoid) {
                nfcInfo.setText(nfcInfoText);
            } else {
                nfcInfo.setText(nfcInfoText + "\n" + "Writing error");
            }
            dialog.dismiss();

            Animation fadeOut = new AlphaAnimation(1, 0);
            fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
            fadeOut.setDuration(500);

            payoutButton.startAnimation(fadeOut);
            payoutButton.setVisibility(View.GONE);
        }

    }


    @Override
    public void cashlessCardDetected(Tag tag) {
        this.tag = tag;
        if (!payout) {
            cashReaderAsyncTask = new CashReader();
            cashReaderAsyncTask.execute(tag);
        } else {
            payout = false;
            cashWriterAsyncTask = new CashWriter();
            cashWriterAsyncTask.execute(tag);
        }
    }
}
