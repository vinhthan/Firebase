package com.example.navigationdrawertoolbarfragment.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.navigationdrawertoolbarfragment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ChangePasswordFragment extends Fragment {
    private View view;

    private EditText edtNewPassword;
    private EditText edtConfirmNewPassword;
    private Button btnUpdatePassword;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        initUi();
        initListener();

        return view;
    }

    private void initUi() {
        progressDialog = new ProgressDialog(getContext());
        edtConfirmNewPassword = view.findViewById(R.id.edt_new_password);
        edtNewPassword = view.findViewById(R.id.edt_old_password);
        btnUpdatePassword = view.findViewById(R.id.btn_update_password);
    }

    private void initListener() {
        btnUpdatePassword.setOnClickListener(v -> {
            onClickUpdatePassword();
        });
    }

    private void onClickUpdatePassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        String newPassword = edtNewPassword.getText().toString().trim();
        String confirmNewPassword = edtConfirmNewPassword.getText().toString().trim();
        if (!confirmNewPassword.equals(newPassword)) {
            Toast.makeText(getContext(), "Incorrect password", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Log.d("123123", "User password updated.");
                                Toast.makeText(getActivity(), "User password updated", Toast.LENGTH_SHORT).show();
                            } else {
                                //re-authenticate
                                //dang nhap lai voi email va password
                                //click button login thi goi reAuthenticate()

                            }
                        }
                    });
        }
    }

    private void reAuthenticate() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            onClickUpdatePassword();
                        } else {
                            Toast.makeText(getActivity(), "Please enter email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}