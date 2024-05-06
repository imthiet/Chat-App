package com.example.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatapp.Class.User;
import com.example.chatapp.R;

import java.util.List;

public class Chat_list_adapter extends ArrayAdapter<User> {

    Context context;
    List<User> arrayListUsers;

    public Chat_list_adapter(@NonNull Context context, List<User> arrayListUsers) {
        super(context, R.layout.custom_list_view,arrayListUsers);
        this.context =context;
        this.arrayListUsers = arrayListUsers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_view,null,true);
        TextView txt_fn = view.findViewById(R.id.chatBox_item);
        txt_fn.setText(arrayListUsers.get(position).getFn());
        
        return view;
    }
}
