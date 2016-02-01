package com.github.javon.labassistant.classes.helpers.barcode;

import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

//import com.google.android.gms.vision.CameraSource;

import com.google.android.gms.vision.CameraSource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

/**
 * Class to help camera to focus when scanning ID
 * @author Javon Davis
 *         Created by Javon Davis on 9/15/15.
 */
public class VisionApiFocusFix {
    /*
 * IF YOU WANT TO JUST ACCESS THE CAMERA INSTANCE SO THAT YOU CAN SET ANY OF THE PARAMETERS, VISIT THE FOLLOWING LINK:
 * https://gist.github.com/Gericop/364dd12b105fdc28a0b6
 */

    /**
     * Custom annotation to allow only valid focus modes.
     */
    @StringDef({
            Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,
            Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO,
            Camera.Parameters.FOCUS_MODE_AUTO,
            Camera.Parameters.FOCUS_MODE_EDOF,
            Camera.Parameters.FOCUS_MODE_FIXED,
            Camera.Parameters.FOCUS_MODE_INFINITY,
            Camera.Parameters.FOCUS_MODE_MACRO
    })
    @Retention(RetentionPolicy.SOURCE)
    private @interface FocusMode {}

}
