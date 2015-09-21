package com.markusschaden.gadgeothek;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class PayActivity extends DefaultActivity {


    private CashlessNfcCardFragment[] fragements = new CashlessNfcCardFragment[] {new PayInFragment()};
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout(R.layout.activity_pay);
		setupNfc();

		AdminTabsAdapter adapter = new AdminTabsAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.viewpager);
		viewPager.setAdapter(adapter);
		TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
		tabLayout.setupWithViewPager(viewPager);

	}

	class AdminTabsAdapter extends FragmentStatePagerAdapter {

		public AdminTabsAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
            if(position >= fragements.length) {
                throw new RuntimeException("Item "+ position + " does not exists");
            }

            return fragements[position];
		}

		@Override
		public int getCount() {
			return fragements.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Tab " + position;
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

            int index = viewPager.getCurrentItem();

            fragements[index].cashlessCardDetected(tag);
        }
    }

}

