package com.example.chatapp.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.ChatApp.ProFile;
import com.example.chatapp.Class.User;
import com.example.chatapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Chat_list_adapter extends ArrayAdapter<User> {

    Context context;
    List<User> arrayListUsers;

    public Chat_list_adapter(@NonNull Context context, List<User> arrayListUsers) {
        super(context, R.layout.custom_list_view,arrayListUsers);
        this.context =context;
        this.arrayListUsers = arrayListUsers;
    }
    private static class ViewHolder {
        ImageView ava;
        TextView tvName;

    }
    public void updateData(ArrayList<User> newList) {
        this.arrayListUsers.clear();
        this.arrayListUsers.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_view,null,true);
//        TextView item = view.findViewById(R.id.chatBox_item);
//        ImageView itAva = view.findViewById(R.id.avatar);
//        User user = arrayListUsers.get(position);
//        item.setText(user.getName());
//
//        item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ProFile.class);
//                intent.putExtra("usn", user.getName()); // Dữ liệu là một chuỗi
//                context.startActivity(intent);
//            }
//        });
//
//
//        return view;
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_view, parent, false);
            holder = new ViewHolder();
            holder.tvName = convertView.findViewById(R.id.chatBox_item);
            holder.ava = convertView.findViewById(R.id.avatar);
            // Set outline provider for circular border
            holder.ava.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int diameter = Math.min(view.getWidth(), view.getHeight());
                    outline.setOval(0, 0, diameter, diameter);
                }
            });
            holder.ava.setClipToOutline(true);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User currentProduct = arrayListUsers.get(position);


        holder.tvName.setText( currentProduct.getName());
        Picasso.get().load(currentProduct.getUrlImg()).into(holder.ava);

        return convertView;
    }
}
