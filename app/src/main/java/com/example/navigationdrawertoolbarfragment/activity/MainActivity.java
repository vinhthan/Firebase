package com.example.navigationdrawertoolbarfragment.activity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.navigationdrawertoolbarfragment.R;
import com.example.navigationdrawertoolbarfragment.fragment.ChangePasswordFragment;
import com.example.navigationdrawertoolbarfragment.fragment.FavoriteFragment;
import com.example.navigationdrawertoolbarfragment.fragment.HistoryFragment;
import com.example.navigationdrawertoolbarfragment.fragment.HomeFragment;
import com.example.navigationdrawertoolbarfragment.fragment.FirebaseFragment;
import com.example.navigationdrawertoolbarfragment.fragment.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int MY_REQUEST_CODE = 12;

    public static final int FRAGMENT_HOME = 0;
    public static final int FRAGMENT_FAVORITE = 1;
    public static final int FRAGMENT_HISTORY = 2;
    public static final int FRAGMENT_PROFILE = 3;
    public static final int FRAGMENT_CHANGE_PASSWORD = 4;
    public static final int FRAGMENT_LOGOUT = 5;
    public static final int FRAGMENT_FIREBASE = 6;
    private int currentFragment = FRAGMENT_FIREBASE;

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private CircleImageView imgAvatar;
    private TextView tvName, tvEmail;
    final private ProfileFragment profileFragment = new ProfileFragment();

    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent == null) {
                    return;
                }
                Uri uri = intent.getData();
                profileFragment.setUri(uri);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    profileFragment.setBitmapImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragment(new FirebaseFragment());
        navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);

        showUserInformation();
    }

    private void initUi() {
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        imgAvatar = navigationView.getHeaderView(0).findViewById(R.id.img_avatar);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
        tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_home:
                if (currentFragment != FRAGMENT_HOME) {
                    replaceFragment(new HomeFragment());
                    currentFragment = FRAGMENT_HOME;
                }
                break;
            case R.id.menu_favorite:
                if (currentFragment != FRAGMENT_FAVORITE) {
                    replaceFragment(new FavoriteFragment());
                    currentFragment = FRAGMENT_FAVORITE;
                }
                break;
            case R.id.menu_history:
                if (currentFragment != FRAGMENT_HISTORY) {
                    replaceFragment(new HistoryFragment());
                    currentFragment = FRAGMENT_HISTORY;
                }
                break;
            case R.id.menu_profile:
                if (currentFragment != FRAGMENT_PROFILE) {
                    replaceFragment(profileFragment);
                    currentFragment = FRAGMENT_PROFILE;
                }
                break;
            case R.id.menu_change_pass:
                if (currentFragment != FRAGMENT_CHANGE_PASSWORD) {
                    replaceFragment(new ChangePasswordFragment());
                    currentFragment = FRAGMENT_CHANGE_PASSWORD;
                }
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.menu_database:
                if (currentFragment != FRAGMENT_FIREBASE) {
                    replaceFragment(new FirebaseFragment());
                    currentFragment = FRAGMENT_FIREBASE;
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    public void showUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            if (name == null) {
                tvName.setVisibility(View.GONE);
            } else {
                tvName.setVisibility(View.VISIBLE);
                tvName.setText(name);
            }
            tvEmail.setText(email);
            Glide.with(this).load(photoUrl).error(R.drawable.avatar).into(imgAvatar);
        }
    }

    //permission


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select picture"));


    }
}