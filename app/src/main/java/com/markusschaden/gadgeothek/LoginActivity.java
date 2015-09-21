package com.markusschaden.gadgeothek;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.avendia.cashless.employeeapp.domain.CashlessSettings;
import ch.avendia.cashless.employeeapp.nfc.Access;
import ch.avendia.cashless.employeeapp.nfc.AccessCalculator;
import ch.avendia.cashless.employeeapp.nfc.CashlessCard;
import ch.avendia.cashless.employeeapp.nfc.dispatcher.CardHandler;
import ch.avendia.cashless.employeeapp.service.UserService;
import ch.avendia.trianglify_lib.Trianglify;
import ch.avendia.trianglify_lib.TrianglifyRandomSettings;

public class LoginActivity extends DefaultActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayoutWithoutNavigationView(R.layout.activity_login);
        setupNfc();

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            //mTextView.setText("NFC is disabled.");
        } else {
            //mTextView.setText(R.string.explanation);
        }

        handleIntent(getIntent());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View mainLoginView = findViewById(R.id.mainLoginView);

        Trianglify trianglify = new Trianglify(new TrianglifyRandomSettings());
        Bitmap background = trianglify.draw(mainLoginView.getWidth(), mainLoginView.getHeight());
        mainLoginView.setBackgroundDrawable(new BitmapDrawable(background));

    }

    protected void handleIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            new NFCLoginReaderTask().execute(tag);
            //new NdefWriterTask().execute(tag);
        }
    }

   private class NdefWriterTask extends AsyncTask<Tag, Void, String> {



       @Override
       protected String doInBackground(Tag... params) {
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
               e.printStackTrace();
           }

           return null;
       }
   }


    private class NFCLoginReaderTask extends AsyncTask<Tag, Void, CashlessSettings> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LoginActivity.this, "Scanning NFC Tag",
                    "Loading. Please wait...", true);

        }

        @Override
        protected CashlessSettings doInBackground(Tag... params) {
            Tag tag = params[0];

            try {
                CardHandler cardHandler = new CardHandler(tag);
                CashlessCard cashlessCard = cardHandler.readCard();

                UserService userService = new UserService();
                CashlessSettings cashlessSettings = userService.authenticate(cashlessCard.getUserId());


                Log.i("NFC read", cashlessCard.toString());
                return cashlessSettings;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(CashlessSettings result) {
            if(result != null) {

                startMainActivity(result);
            }

            dialog.dismiss();
        }
    }


    private void startMainActivity(CashlessSettings result) {
        /*Bundle conData = new Bundle();
        conData.putSerializable(MainActivity.LOGIN_RESULT_BUNDLE, result);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);*/


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.LOGIN_RESULT_BUNDLE, result);
        startActivity(intent);

        finish();
    }




}
