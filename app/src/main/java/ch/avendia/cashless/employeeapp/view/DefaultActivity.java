package ch.avendia.cashless.employeeapp.view;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.NfcAdapter;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import ch.avendia.cashless.employeeapp.TrianglifyRandomSettings;
import ch.avendia.cashless.employeeapp.domain.CashlessSettings;
import ch.avendia.trianglify_lib.Trianglify;

/**
 * Created by Markus on 21.09.2015.
 */
public abstract class DefaultActivity extends AppCompatActivity {

    public static final String LOGIN_RESULT_BUNDLE = "Login_Result";

    protected DrawerLayout mDrawerLayout;
    protected NfcAdapter mNfcAdapter;
    protected NavigationView navigationView;
    protected static CashlessSettings cashlessSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getSerializable(LOGIN_RESULT_BUNDLE) != null) {
            cashlessSettings = (CashlessSettings) savedInstanceState.getSerializable(LOGIN_RESULT_BUNDLE);
        }
    }


    public void setupLayoutWithoutNavigationView(int layoutId) {
        setupLayoutInternal(layoutId, false);
    }

    public void setActiveMenuItem(int id) {
        navigationView.getMenu().getItem(id).setChecked(true);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        for(int i=0;i<menu.size();i++){
            menu.getItem(i).setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void setupLayout(int layoutId) {
        setupLayoutInternal(layoutId, true);
    }

    private void setupLayoutInternal(int layoutId, boolean menu) {
        setContentView(layoutId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set the transparent color of the status bar, 20% darker
        tintManager.setTintColor(Color.parseColor("#20FFFFFF"));
        if (!menu) {
            //toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }

        if (menu) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, toolbar,
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

                    Intent intent = null;
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_item_security:
                            intent = new Intent(getApplication(), SecurityActivity.class);
                            break;
                        case R.id.navigation_item_admin:
                            intent = new Intent(getApplication(), AdminActivity.class);
                            break;
                        case R.id.navigation_item_pay_payout:
                            intent = new Intent(getApplication(), PayActivity.class);
                            break;
                        case R.id.navigation_item_entrance:
                            intent = new Intent(getApplication(), EntranceActivity.class);
                            break;
                        case R.id.navigation_item_logout:
                            cashlessSettings = null;
                            intent = new Intent(getApplication(), LoginActivity.class);
                            break;
                    }

                    if (intent != null) {
                        startActivity(intent);
                    }

                    return true;
                }
            });

            updateHeaderView();
        }


    }

    public void setupNfc() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    public void setupUserSettings(CashlessSettings cashlessSettings) {
        DefaultActivity.cashlessSettings = cashlessSettings;
        updateHeaderView();
    }

    private void updateHeaderView() {
        if (cashlessSettings != null && cashlessSettings.getEmployee() != null && navigationView != null) {

            View headerView = navigationView.findViewById(R.id.drawer_header_root);
            TextView headerText = (TextView) headerView.findViewById(R.id.drawerHeader);
            headerText.setText(cashlessSettings.getEmployee().getFirstName() + " " + cashlessSettings.getEmployee().getName());

            Trianglify trianglify = new Trianglify(new TrianglifyRandomSettings());
            Bitmap background = trianglify.draw(500, 500);
            headerView.setBackgroundDrawable(new BitmapDrawable(background));

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        if (mNfcAdapter != null) {
            setupForegroundDispatch(this, mNfcAdapter);
        }
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        if (mNfcAdapter != null) {
            stopForegroundDispatch(this, mNfcAdapter);
        }

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

    protected abstract void handleIntent(Intent intent);


    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{new String[]{MifareUltralight.class.getName()}};

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
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }


    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable(LOGIN_RESULT_BUNDLE, cashlessSettings);
    }


    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
