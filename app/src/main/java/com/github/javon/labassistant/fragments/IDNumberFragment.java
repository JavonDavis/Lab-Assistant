package com.github.javon.labassistant.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.classes.helpers.barcode.BarcodeTrackerFactory;
import com.github.javon.labassistant.classes.helpers.barcode.CameraSourcePreview;
import com.github.javon.labassistant.classes.helpers.barcode.GraphicOverlay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link onStudentFoundListener} interface
 * to handle interaction events.
 * Use the {@link IDNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IDNumberFragment extends Fragment {

    private static final String ARG_COURSE = "course";
    private static final String ARG_LAB_COUNT = "lab count";
    private static final String ARG_GRADE_TABLE = "table name";

    private String mCourse;
    private String mGradeTableName;
    private int mLabCount;

    private EditText idNumberField;

    private onStudentFoundListener mListener;

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private CameraSource mCameraSource = null;

    public IDNumberFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param course The name of the course.
     * @param labCount Number of labs for the course.
     * @param tableName The name of the remote table name where data is stored.
     * @return A new instance of fragment IDNumberFragment.
     */
    public static IDNumberFragment newInstance(String course, int labCount, String tableName) {
        IDNumberFragment fragment = new IDNumberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COURSE, course);
        args.putString(ARG_GRADE_TABLE, tableName);
        args.putInt(ARG_LAB_COUNT, labCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCourse = getArguments().getString(ARG_COURSE);
            mGradeTableName = getArguments().getString(ARG_GRADE_TABLE);
            mLabCount = getArguments().getInt(ARG_LAB_COUNT);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_idnumber, container, false);

        idNumberField = (EditText) view.findViewById(R.id.id_number);

        TextView title = (TextView) view.findViewById(R.id.course_title);
        title.setText(mCourse);

        Button manualButton = (Button) view.findViewById(R.id.manual);
        manualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idNumber = idNumberField.getText().toString();
                checkID(idNumber);

            }
        });

        mPreview = (CameraSourcePreview) view.findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) view.findViewById(R.id.overlay);

        createCameraSource();

        return view;
    }

    public void setIDField(final String idNumber)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                idNumberField.setText(idNumber);
            }
        });
    }

    private void checkID(final String idNumber)
    {
        //lookup logic here
        ParseQuery<ParseObject> query = ParseQuery.getQuery(mGradeTableName);
        query.whereEqualTo("id_number",idNumber);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e == null)
                {
                    mListener.onStudentFound(mCourse,parseObject,mLabCount);
                }
                else
                {
                    if(e.getCode() == ParseException.OBJECT_NOT_FOUND)
                    {
                        Log.d("Student not found",idNumber+" not found in "+mGradeTableName+" table");
                        mListener.onStudentNotFound(mCourse,idNumber,mGradeTableName);
                    }
                    else
                    {
                        //Some other error
                        Log.d("Parse error","Code:"+e.getCode());
                        Toast.makeText(getActivity(),"Internal error",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onStudentFoundListener) {
            mListener = (onStudentFoundListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onStudentFoundListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onStudentFoundListener {
        void onStudentFound(String courseName, ParseObject object, int labCount);
        void onStudentNotFound(String courseName, String id, String tableName);
    }

    /**
     * Restarts the camera.
     */
    @Override
    public void onResume() {
        super.onResume();

        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    public void onPause() {
        super.onPause();
        if(mPreview!=null)
            mPreview.stop();
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    private void createCameraSource() {


        Context context = getActivity().getApplicationContext();



        // A barcode detector is created to track barcodes.  An associated multi-processor instance
        // is set to receive the barcode detection results, track the barcodes, and maintain
        // graphics for each barcode on screen.  The factory is used by the multi-processor to
        // create a separate tracker instance for each barcode.
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(context).build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(this,mGraphicOverlay);
        barcodeDetector.setProcessor(
                new MultiProcessor.Builder<>(barcodeFactory).build());

        // A multi-detector groups the two detectors together as one detector.  All images received
        // by this detector from the camera will be sent to each of the underlying detectors, which
        // will each do face and barcode detection, respectively.  The detection results from each
        // are then sent to associated tracker instances which maintain per-item graphics on the
        // screen.
//        MultiDetector multiDetector = new MultiDetector.Builder()
//                .add(barcodeDetector)
//                .build();

        /*if (!multiDetector.isOperational()) {
            // Note: The first time that an app using the barcode or face API is installed on a
            // device, GMS will download a native libraries to the device in order to do detection.
            // Usually this completes before the app is run for the first time.  But if that
            // download has not yet completed, then the above call will not detect any barcodes
            // and/or faces.
            //
            // isOperational() can be used to check if the required native libraries are currently
            // available.  The detectors will automatically become operational once the library
            // downloads complete on device.
            Log.w("Detector", "Detector dependencies are not yet available.");
        }*/

        // Creates and starts the camera.  Note that this uses a higher resolution in comparison
        // to other detection examples to enable the barcode detector to detect small barcodes
        // at long distances.
        mCameraSource = new CameraSource.Builder(context, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1600, 1024)
                .setRequestedFps(15.0f)
                .build();

        startCameraSource();

    }


    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getActivity().getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, 0);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource,mGraphicOverlay);
            } catch (IOException e) {
                Log.e("Camera source", "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }
}
