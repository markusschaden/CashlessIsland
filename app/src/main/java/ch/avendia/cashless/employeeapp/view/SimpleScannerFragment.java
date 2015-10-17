package ch.avendia.cashless.employeeapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Markus on 30.09.2015.
 */
public class SimpleScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private BarcodeNotifier barcodeNotifier;
    private ZXingScannerView mScannerView;

    interface BarcodeNotifier {
        void onBarcode(String value);
    }

    public void setBarcodeNotifier(BarcodeNotifier barcodeNotifier) {
        this.barcodeNotifier = barcodeNotifier;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());

        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if(barcodeNotifier == null) {
            Toast.makeText(getActivity(), "Contents = " + rawResult.getText() +
                    ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();
            mScannerView.startCamera();
        } else {
            barcodeNotifier.onBarcode(rawResult.getText());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}