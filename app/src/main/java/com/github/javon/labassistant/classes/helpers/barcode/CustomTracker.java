package com.github.javon.labassistant.classes.helpers.barcode;

import android.text.TextUtils;

import com.github.javon.labassistant.fragments.IDNumberFragment;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 9/15/15.
 */
public class CustomTracker extends Tracker<Barcode> {
    private IDNumberFragment mFragment;

    public CustomTracker(IDNumberFragment fragment) {
        mFragment = fragment;
    }

    @Override
    public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode barcode) {
        // Access detected barcode values
        if(isValidBarcode(barcode))
            mFragment.setIDField(barcode.displayValue);
    }

    /*
    * Method to check if the barcode read is valid
    * 1. Length of barcode must be greater than 5
    * 2. Must be entirely numeric
     */
    public boolean isValidBarcode(Barcode barcode) {
        final String value = barcode.displayValue;

        return value.length() > 5 && TextUtils.isDigitsOnly(value);
    }
}
