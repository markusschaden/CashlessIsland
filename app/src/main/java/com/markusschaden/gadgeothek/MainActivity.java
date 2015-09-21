package com.markusschaden.gadgeothek;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.support.design.widget.NavigationView;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import ch.avendia.cashless.employeeapp.TrianglifyRandomSettings;
import ch.avendia.cashless.employeeapp.domain.CashlessSettings;
import ch.avendia.trianglify_lib.Trianglify;

public class MainActivity extends AppCompatActivity {

	public static final int LOGIN_RESULT = 1;
	public static final String LOGIN_RESULT_BUNDLE = "Login_Result";

	private DrawerLayout mDrawerLayout;

	private CashlessSettings cashlessSettings;
	private NavigationView navigationView;

    private Map<String, CashlessNfcCardFragment> menuFragments;

    CashlessNfcCardFragment current;
    private NfcAdapter mNfcAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
				this,  mDrawerLayout, toolbar,
				R.string.navigation_drawer_open, R.string.navigation_drawer_close
		);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();

		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				menuItem.setChecked(true);
				mDrawerLayout.closeDrawers();
				Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();

                if(menuFragments.containsKey(menuItem.getTitle())) {

                    /*current = menuFragments.get(menuItem.getTitle());
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, current).commit();*/

					Intent intent = new Intent(getApplication(), AdminActivity.class);
					startActivity(intent);
                }


				return true;
			}
		});

		if(savedInstanceState != null && savedInstanceState.getSerializable(LOGIN_RESULT_BUNDLE) != null) {
			cashlessSettings = (CashlessSettings)savedInstanceState.getSerializable(LOGIN_RESULT_BUNDLE);
			updateHeaderView();
		} else {
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivityForResult(loginIntent, LOGIN_RESULT);
		}


        menuFragments = new ArrayMap<>();
        menuFragments.put(getString(R.string.nav_item_admin),new MainActivityFragment());
        menuFragments.put(getString(R.string.nav_item_entrance),new AdminFragment());

        current = menuFragments.get(getString(R.string.nav_item_admin));

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

	private void updateHeaderView() {
		if(cashlessSettings != null && cashlessSettings.getEmployee() != null) {
			//LayoutInflater factory = LayoutInflater.from(this);
			/*View headerView = navigationView.inflateHeaderView(R.layout.drawer_header);
			TextView headerText = (TextView) headerView.findViewById(R.id.drawerHeader);
			LinearLayout linearLayout = (LinearLayout)headerView.findViewById(R.id.)
			headerText.setText(cashlessSettings.getEmployee().getFirstName() + " " + cashlessSettings.getEmployee().getName());

			navigationView.addHeaderView(headerView);
*/

			/*TextView headerView = (TextView) LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
			headerView.setText("Your_thoght");*/

			View headerView = navigationView.findViewById(R.id.drawer_header_root);
			TextView headerText = (TextView) headerView.findViewById(R.id.drawerHeader);
			headerText.setText(cashlessSettings.getEmployee().getFirstName() + " " + cashlessSettings.getEmployee().getName());

			Trianglify trianglify = new Trianglify(new TrianglifyRandomSettings());
			Bitmap background = trianglify.draw(500, 500);
			headerView.setBackgroundDrawable(new BitmapDrawable(background));

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


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
			case LOGIN_RESULT:
				if (resultCode == RESULT_OK) {
					Bundle res = data.getExtras();
					CashlessSettings result = (CashlessSettings)res.get(LOGIN_RESULT_BUNDLE);
					cashlessSettings = result;
					updateHeaderView();
				}
				break;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		state.putSerializable(LOGIN_RESULT_BUNDLE, cashlessSettings);
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

            current.cashlessCardDetected(tag);

            //new NFCLoginReaderTask().execute(tag);
            //new NdefWriterTask().execute(tag);
        }
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

