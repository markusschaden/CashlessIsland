package com.markusschaden.gadgeothek;

import android.nfc.Tag;
import android.support.v4.app.Fragment;

/**
 * Created by Markus on 17.09.2015.
 */
abstract class CashlessNfcCardFragment extends Fragment {

    public abstract void cashlessCardDetected(Tag tag);

}
