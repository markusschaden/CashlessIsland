package com.markusschaden.gadgeothek;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.MenuItem;
import android.widget.Toast;

import ch.avendia.cashless.employeeapp.domain.CashlessSettings;

public class MainActivity extends DefaultActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_main);
		setupNfc();

        CashlessSettings cashlessSettings;
		if(getIntent() != null && getIntent().getSerializableExtra(LOGIN_RESULT_BUNDLE) != null) {
            Intent i= getIntent();
            cashlessSettings = (CashlessSettings)i.getSerializableExtra(LOGIN_RESULT_BUNDLE);
            setupUserSettings(cashlessSettings);
        }
    }



    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
			case R.id.action_settings:
				return true;
		}

		return super.onOptionsItemSelected(item);
	}


	protected void handleIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            Toast.makeText(this, "Card detected: " + tag.getId(), Toast.LENGTH_SHORT);
        }
    }




}

