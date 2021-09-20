package com.example.navigationdrawertoolbarfragment.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationdrawertoolbarfragment.R;
import com.example.navigationdrawertoolbarfragment.adapter.UserAdapter;
import com.example.navigationdrawertoolbarfragment.model.Job;
import com.example.navigationdrawertoolbarfragment.model.User;
import com.example.navigationdrawertoolbarfragment.model.UserList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FirebaseFragment extends Fragment {
    private View view;

    private EditText edtData, edtId, edtName;
    private Button btnPushData, btnPushObjectData, btnPushMapData, btnUpdateData,
            btnUpdateMapData, btnDeleteData, btnAddListData;
    private TextView tvGetData, tvGetObjectData, tvGetMapData;

    private RecyclerView rcyListUser;
    private UserAdapter mUserAdapter;
    private List<UserList> mList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_firebase, container, false);

        initUi();
        initListener();

        getDataFirebase();
        getObjectDataFirebase();
        getMapDataFirebase();
        getListUserFromRealtimeDatabase();

        return view;
    }

    private void initUi() {
        edtData = view.findViewById(R.id.edt_data);
        edtId = view.findViewById(R.id.edt_id);
        edtName = view.findViewById(R.id.edt_name);
        btnPushData = view.findViewById(R.id.btn_push_data);
        btnPushObjectData = view.findViewById(R.id.btn_push_object_data);
        btnPushMapData = view.findViewById(R.id.btn_push_map_data);
        btnUpdateData = view.findViewById(R.id.btn_update_data);
        btnUpdateMapData = view.findViewById(R.id.btn_update_map_data);
        btnDeleteData = view.findViewById(R.id.btn_delete_data);
        btnAddListData = view.findViewById(R.id.btn_add_list_data);
        tvGetData = view.findViewById(R.id.tv_get_data);
        tvGetObjectData = view.findViewById(R.id.tv_get_object_data);
        tvGetMapData = view.findViewById(R.id.tv_get_map_data);

        //
        rcyListUser = view.findViewById(R.id.rcy_list_user);
        showListUse();
    }

    private void initListener() {
        btnPushData.setOnClickListener(v -> {
            onClickPushData();
        });
        btnPushObjectData.setOnClickListener(v -> {
            onClickPushObjectData();
        });
        btnPushMapData.setOnClickListener(v -> {
            onClickPushMapData();
        });
        btnUpdateData.setOnClickListener(v -> {
            onClickUpdateData();
        });
        btnUpdateMapData.setOnClickListener(v -> {
            onClickUpdateMapData();
        });
        btnDeleteData.setOnClickListener(v -> {
            onClickDeleteData();
        });
        btnAddListData.setOnClickListener(v -> {
            //add 1 ptu
            /*int id = Integer.parseInt(edtId.getText().toString().trim());
            String name = edtName.getText().toString().trim();
            UserList userList = new UserList(id, name);
            onClickAddListData(userList);*/

            //add list ptu
            addAllUser();
        });
    }

    private void onClickPushData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Ten");
        String strValue = edtData.getText().toString().trim();
        reference.setValue(strValue, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Push data success", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference referenceJob = database.getReference("job");
        referenceJob.setValue("android");
        DatabaseReference referenceSex = database.getReference("sex");
        referenceSex.setValue("man");
        DatabaseReference referenceCheck = database.getReference("check");
        referenceCheck.setValue(true);
        DatabaseReference referenceTest = database.getReference("test/test_one");
        referenceTest.setValue("test 1");
    }

    private void getDataFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Ten");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = snapshot.getValue(String.class);
                tvGetData.setText(value);
                Log.d("123123", "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                // Failed to read value
                Log.w("123123", "Failed to read value.", error.toException());
            }
        });
    }

    private void onClickPushObjectData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user_info");

        User user = new User(1, "name 1", new Job(1, "job 1"));
        user.setAddress("address 1");
        reference.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Push data success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getObjectDataFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user_info");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //String value = snapshot.getValue(String.class);
                User user = snapshot.getValue(User.class);
                if (user == null) {
                    return;
                }
                tvGetObjectData.setText(user.toString());
                Log.d("123123", "Value is: " + user.toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.w("123123", "Failed to read value.", error.toException());
            }
        });
    }

    private void onClickPushMapData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("map_data");

        Map<String, Boolean> map = new HashMap<>();
        map.put("1", true);
        map.put("2", false);
        map.put("3", true);
        map.put("4", false);
        reference.setValue(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "push map data success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMapDataFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("map_data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Map<String, Boolean> mapResult = new HashMap<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    String key = snapshot1.getKey();
                    Boolean value = snapshot1.getValue(Boolean.class);
                    mapResult.put(key, value);

                }
                tvGetMapData.setText(mapResult.toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void onClickUpdateData() {
        //Cach 1
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user_info");

        User user = new User(2, "name 2", new Job(2, "job 2"));
        user.setAddress("address 2");
        reference.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Update data success", Toast.LENGTH_SHORT).show();
            }
        });*/

        //Cach 2 update 1 truong
        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user_info/job/name");
        reference.setValue("job 1", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Update data success", Toast.LENGTH_SHORT).show();
            }
        });*/

        //cach 3 update nhieu truong
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("user_info");
        /*Map<String, Object> map = new HashMap<>();
        map.put("address", "Ha Noi");
        map.put("name", "name name");
        map.put("job/name", "job job");
        reference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Update data success", Toast.LENGTH_SHORT).show();
            }
        });*/
        User user = new User("name 123", "address 123", new Job("job 123"));
        reference.updateChildren(user.toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Update data success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickUpdateMapData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("map_data");
        Map<String, Object> map = new HashMap<>();
        map.put("1", true);
        map.put("4", true);
        reference.updateChildren(map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Update map data success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickDeleteData() {
        new AlertDialog.Builder(getContext())
                .setTitle("Xoa ban ghi")
                .setMessage("Ban co chac chan muon xoa ban ghi?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("user_info/job/name");
                        reference.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                                Toast.makeText(getContext(), "Delete data success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void onClickAddListData(UserList userList) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("list_user");

        String pathObject = String.valueOf(userList.getId());
        reference.child(pathObject).setValue(userList, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Add user success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAllUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("list_user");

        List<UserList> userLists = new ArrayList<>();
        userLists.add(new UserList(0, "name 0"));
        userLists.add(new UserList(1, "name 1"));
        userLists.add(new UserList(2, "name 2"));
        userLists.add(new UserList(3, "name 3"));
        userLists.add(new UserList(4, "name 4"));

        reference.setValue(userLists, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Add all user success", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showListUse() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcyListUser.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rcyListUser.addItemDecoration(dividerItemDecoration);
        mList = new ArrayList<>();
        mUserAdapter = new UserAdapter(mList, new UserAdapter.IClickListener() {
            @Override
            public void onClickUpdateItem(UserList userList) {
                openDialogUpdateItem(userList);
            }

            @Override
            public void onClickDeleteItem(UserList userList) {
                deleteItemUser(userList);
            }
        });
        rcyListUser.setAdapter(mUserAdapter);
    }

    private void getListUserFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("list_user");

        //Cach 1 chua toi uu
        /*reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (mList != null) {
                    mList.clear();
                }
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserList userList = snapshot1.getValue(UserList.class);
                    mList.add(userList);
                }
                mUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), "get list user fail", Toast.LENGTH_SHORT).show();
            }
        });*/

        //Cach 2 toi uu
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                UserList userList = snapshot.getValue(UserList.class);
                if (userList !=null) {
                    mList.add(userList);
                    mUserAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                UserList userList = snapshot.getValue(UserList.class);
                if (userList == null || mList == null || mList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < mList.size(); i++) {
                    if (userList.getId() == mList.get(i).getId()) {
                        mList.set(i, userList);
                        break;
                    }
                }
                mUserAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                UserList userList = snapshot.getValue(UserList.class);
                if (userList == null || mList == null || mList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < mList.size(); i++) {
                    if (userList.getId() == mList.get(i).getId()) {
                        mList.remove(mList.get(i));
                        break;
                    }
                }
                mUserAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void openDialogUpdateItem(UserList userList) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_update);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtUpdateName = dialog.findViewById(R.id.edt_update_name);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnUpdateName = dialog.findViewById(R.id.btn_update_name);

        if (userList == null) {
            return;
        }
        edtUpdateName.setText(userList.getName());
        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        btnUpdateName.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("list_user");

            String newMane = edtUpdateName.getText().toString().trim();
            userList.setName(newMane);

            reference.child(String.valueOf(userList.getId())).updateChildren(userList.toMap(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                    Toast.makeText(getContext(), "Update name success", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }

    private void deleteItemUser(UserList userList) {
        new AlertDialog.Builder(getContext())
                .setTitle("Xoa ban ghi")
                .setMessage("Ban co chac chan muon xoa user?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("list_user");
                        reference.child(String.valueOf(userList.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable @org.jetbrains.annotations.Nullable DatabaseError error, @NonNull @NotNull DatabaseReference ref) {
                                Toast.makeText(getContext(), "Delete user success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}