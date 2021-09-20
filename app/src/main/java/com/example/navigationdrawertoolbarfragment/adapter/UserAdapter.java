package com.example.navigationdrawertoolbarfragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.navigationdrawertoolbarfragment.R;
import com.example.navigationdrawertoolbarfragment.model.UserList;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private List<UserList> mList;
    private IClickListener mIClickListener;

    public interface IClickListener {
        void onClickUpdateItem(UserList userList);
        void onClickDeleteItem(UserList userList);
    }

    public UserAdapter(List<UserList> mList, IClickListener mIClickListener) {
        this.mList = mList;
        this.mIClickListener = mIClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        UserList userList = mList.get(position);
        if (userList == null) {
            return;
        }
        holder.tvIdUser.setText(String.valueOf(userList.getId()));
        holder.tvNameUser.setText(userList.getName());
        holder.imgUpdateUser.setOnClickListener(v -> {
            mIClickListener.onClickUpdateItem(userList);
        });
        holder.imgClearUser.setOnClickListener(v -> {
            mIClickListener.onClickDeleteItem(userList);
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdUser, tvNameUser;
        ImageView imgUpdateUser, imgClearUser;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvIdUser = itemView.findViewById(R.id.tv_id_user);
            tvNameUser = itemView.findViewById(R.id.tv_name_user);
            imgUpdateUser = itemView.findViewById(R.id.img_update_user);
            imgClearUser = itemView.findViewById(R.id.img_clear_user);
        }
    }
}
