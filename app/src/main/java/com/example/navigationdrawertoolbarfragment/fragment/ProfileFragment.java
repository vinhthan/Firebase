package com.example.navigationdrawertoolbarfragment.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import com.bumptech.glide.Glide;
import com.example.navigationdrawertoolbarfragment.R;
import com.example.navigationdrawertoolbarfragment.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.navigationdrawertoolbarfragment.activity.MainActivity.MY_REQUEST_CODE;

public class ProfileFragment extends Fragment {
    private View view;

    private ProgressDialog progressDialog;
    private CircleImageView imgAvatar;
    private EditText edtEmail, edtFullName;
    private Button btnUpdateProfile;
    private MainActivity mainActivity;
    private Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        initUi();
        mainActivity = (MainActivity) getActivity();
        showUserInformation();
        initListener();


        return view;
    }

    private void initUi() {
        progressDialog = new ProgressDialog(getContext());
        imgAvatar = view.findViewById(R.id.img_avatar);
        edtEmail = view.findViewById(R.id.edt_email);
        edtFullName = view.findViewById(R.id.edt_full_name);
        btnUpdateProfile = view.findViewById(R.id.btn_update_profile);

    }

    private void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            edtEmail.setText(user.getEmail());
            edtFullName.setText(user.getDisplayName());
            Glide.with(this).load(user.getPhotoUrl()).error(R.drawable.avatar).into(imgAvatar);
        }
    }

    private void initListener() {
        imgAvatar.setOnClickListener(v -> {
            onClickRequestPermission();
        });
        btnUpdateProfile.setOnClickListener(v -> {
            onClickUpdateProfile();
            updateEmail();
        });
    }

    private void onClickRequestPermission() {
        if (mainActivity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {//android 6 api 23
            mainActivity.openGallery();
            return;
        }

        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mainActivity.openGallery();
        } else {
            String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }

    public void setBitmapImage(Bitmap bitmap) {
        imgAvatar.setImageBitmap(bitmap);
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        progressDialog.show();
        String name = edtFullName.getText().toString().trim();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(uri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.d("123123", "User profile updated.");
                            Toast.makeText(getActivity(), "Update profile success", Toast.LENGTH_SHORT).show();
                            mainActivity.showUserInformation();
                        }
                    }
                });
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    private void updateEmail() {
        String newEmail = edtEmail.getText().toString().trim();
        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        user.updateEmail(newEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.d("123123", "User email address updated.");
                            mainActivity.showUserInformation();
                        } else {
                            //re-authenticate
                            //dang nhap lai voi email va password
                            //click button login thi goi reAuthenticate()

                        }
                    }
                });
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
                            updateEmail();
                        } else {
                            Toast.makeText(getActivity(), "Please enter email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}