package com.example.chatapp.Service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.chatapp.Class.Chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatService {
    private final RequestQueue requestQueue;

    public ChatService(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void sendMessage(Chat chat, String otherName, final VolleyCallback callback) {
        String url = "http://10.0.2.2/API/add_mess.php"; // Change to your actual URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response
                        callback.onSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        callback.onError(error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("SenderName", chat.getUsername()); // Send sender's name
                params.put("ReceiverName", otherName); // Send receiver's name
                params.put("MessageText", chat.getText());
                params.put("MessageType", "text");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void getChatMessages(String userName, String otherName, final ChatMessagesCallback callback) {
        System.out.println("Chay get Mesage!");
        String url = "http://10.0.2.2/API/get_messages.php?userName=" + userName + "&otherName=" + otherName; // Change to your actual URL

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response);
                        try {
                            List<Chat> chatMessages = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject chatObject = response.getJSONObject(i);
                                Chat chat = new Chat(
                                        chatObject.getString("username"),
                                        chatObject.getString("text"),
                                        chatObject.getString("username").equals(userName)
                                );
                                chatMessages.add(chat);
                            }
                            callback.onSuccess(chatMessages);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Parsing error!");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onError("Network error!");
                        Log.d("Chátervice",error.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    public interface VolleyCallback {
        void onSuccess();
        void onError(String error);
    }

    public interface ChatMessagesCallback {
        void onSuccess(List<Chat> chatMessages);
        void onError(String error);
    }
}
