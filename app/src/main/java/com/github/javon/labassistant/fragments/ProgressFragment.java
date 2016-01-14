package com.github.javon.labassistant.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.javon.labassistant.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginProgressListener} interface
 * to handle interaction events.
 */
public class ProgressFragment extends Fragment {

    private OnLoginProgressListener mListener;

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        attemptLogin();
        return view;
    }

    private void attemptLogin() {
        if(true)
        {
            mListener.onLoginSuccessful();
        }
        else
        {
            mListener.onLoginFailed();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginProgressListener) {
            mListener = (OnLoginProgressListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginProgressListener");
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
    public interface OnLoginProgressListener {
        void onLoginSuccessful();
        void onLoginFailed();
    }
}
