package com.github.javon.labassistant.classes.helpers.barcode;

import android.content.Context;
import android.util.Log;

import com.github.javon.labassistant.fragments.IDNumberFragment;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 9/14/15.
 *
 *
 * Factory for creating a tracker and associated graphic to be associated with a new barcode.  The
 * multi-processor uses this factory to create barcode trackers as needed -- one for each barcode.
 */
public class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private GraphicOverlay mGraphicOverlay;
    private IDNumberFragment mFragment;

    public BarcodeTrackerFactory(IDNumberFragment idNumberFragment,GraphicOverlay graphicOverlay) {
        mGraphicOverlay = graphicOverlay;
        mFragment = idNumberFragment;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay);
        Log.d("barcode",barcode.displayValue);
        return new CustomTracker(mFragment);
    }
}

