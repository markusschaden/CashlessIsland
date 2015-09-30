package ch.avendia.cashless.employeeapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ch.avendia.cashless.employeeapp.domain.CashlessSettings;
import ch.avendia.cashless.employeeapp.nfc.CashlessCard;
import ch.avendia.cashless.employeeapp.nfc.dispatcher.CardHandler;
import ch.avendia.cashless.employeeapp.service.UserService;
import ch.avendia.trianglify_lib.Trianglify;
import ch.avendia.trianglify_lib.TrianglifyRandomSettings;

public class LoginActivity extends DefaultActivity {

    private Button loginButton;
    private TextView usernameText;
    private TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayoutWithoutNavigationView(R.layout.activity_login);
        setupNfc();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_dark));
        }

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


        usernameText = (TextView) findViewById(R.id.username);
        passwordText = (TextView) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameText.getText().toString();
                String password = passwordText.getText().toString();

                new CredentialsLoginTask().execute(username, password);
            }
        });

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
            if (result != null) {

                startMainActivity(result);
            }

            dialog.dismiss();
        }
    }


    private class CredentialsLoginTask extends AsyncTask<String, Void, CashlessSettings> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(LoginActivity.this, "Login",
                    "Loading. Please wait...", true);

        }

        @Override
        protected CashlessSettings doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            UserService userService = new UserService();
            return userService.authenticate(username, password);
        }


        @Override
        protected void onPostExecute(CashlessSettings result) {
            if (result != null) {

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
