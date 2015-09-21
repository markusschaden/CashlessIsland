package com.markusschaden.gadgeothek;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class LoginActivity extends AppCompatActivity {

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

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


    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

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
                dialog.dismiss();
                returnResult(result);
            }
        }
    }


    private void returnResult(CashlessSettings result) {
        Bundle conData = new Bundle();
        conData.putSerializable(MainActivity.LOGIN_RESULT_BUNDLE, result);
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }



    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{new String[] {MifareUltralight.class.getName()}};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        /*filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }*/

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }




}
