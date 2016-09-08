package com.github.javon.labassistant.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.javon.labassistant.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginAttemptedListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASSWORD = "password";

    private String mUsername;
    private String mPassword;

    private OnLoginAttemptedListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param username Username.
     * @param password Password.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(String username, String password) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    public static LoginFragment newInstance()
    {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, "");
        args.putString(ARG_PASSWORD, "");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString(ARG_USERNAME);
            mPassword = getArguments().getString(ARG_PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText usernameField = (EditText) view.findViewById(R.id.username);
        final EditText passwordField = (EditText) view.findViewById(R.id.password);
        Button loginButton = (Button) view.findViewById(R.id.loginButton);

        usernameField.setText(mUsername);
        passwordField.setText(mPassword);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                if(username.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getActivity(),"Both username and password must be entered",Toast.LENGTH_LONG).show();
                }
                else
                {
                    mListener.onLoginAttempted(username,password);
                }

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginAttemptedListener) {
            mListener = (OnLoginAttemptedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginAttemptedListener");
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
    public interface OnLoginAttemptedListener {
        void onLoginAttempted(String username, String password);
    }
}
