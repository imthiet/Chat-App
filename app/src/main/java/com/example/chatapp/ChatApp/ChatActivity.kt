package com.example.chatapp.ChatApp


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.Adapter.ChatAdapter
import com.example.chatapp.Adapter.NotificationHelper
import com.example.chatapp.Class.Chat
import com.example.chatapp.R
import com.example.chatapp.Service.ChatService
import com.example.chatapp.SocketHandler.SocketHandler
import com.example.chatapp.databinding.ActivityMainBinding


class ChatActivity : AppCompatActivity() {
    private lateinit var socketHandler: SocketHandler
    private lateinit var binding: ActivityMainBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatService: ChatService
    private val chatList: MutableList<Chat> = ArrayList()
    private var userName: String = ""
    private var otherName: String = ""
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        socketHandler = SocketHandler()
        chatService = ChatService(this)


        exitButton = findViewById(com.example.chatapp.R.id.exit_btn)
        exitButton.setOnClickListener {

            val intent = Intent(this, ChatList::class.java)

            // Thực hiện mở Intent
            startActivity(intent)
        }

        // ten duoc tim kiem

        userName = intent.getStringExtra("username") ?: ""
        otherName = intent.getStringExtra("othername") ?: ""

        if (userName.isEmpty() || otherName.isEmpty()) {
            Toast.makeText(this, "Not found 2 users of chat!", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(this, "Chat Of $userName $otherName", Toast.LENGTH_LONG).show()

            chatAdapter = ChatAdapter()
            binding.rvChat.layoutManager = LinearLayoutManager(this)
            binding.rvChat.adapter = chatAdapter

            binding.btnSend.setOnClickListener {
                val message = binding.etMsg.text.toString()
                if (message.isNotEmpty()) {
                    val chat = Chat(
                        username = userName,
                        text = message,
                        isSelf = true
                    )
                    socketHandler.emitChat(chat)
                    chatService.sendMessage(chat, otherName, object : ChatService.VolleyCallback {
                        override fun onSuccess() {
                            binding.etMsg.setText("")
                            Log.d("ChatActivity", "Message sent successfully")
                        }

                        override fun onError(error: String) {
                            Toast.makeText(this@ChatActivity, "Failed to send message: $error", Toast.LENGTH_LONG).show()
                            Log.d("ChatActivity", error)
                        }
                    })
                }
            }

            socketHandler.onNewChat.observe(this) { chat ->
                playNotificationSound()
                chat.isSelf = chat.username == userName
                chatList.add(chat)
                chatAdapter.submitChat(chatList)
                binding.rvChat.scrollToPosition(chatList.size - 1)

            }

            loadInitialMessages()
        }
    }

    private fun loadInitialMessages() {
        chatService.getChatMessages(userName, otherName, object : ChatService.ChatMessagesCallback {
            override fun onSuccess(chatMessages: List<Chat>) {
                chatList.addAll(chatMessages)
                chatAdapter.submitChat(chatList)
                if (chatList.isNotEmpty()) {
                    binding.rvChat.scrollToPosition(chatList.size - 1)
                }
            }

            override fun onError(error: String) {
                Toast.makeText(this@ChatActivity, "Failed to load messages: $error", Toast.LENGTH_LONG).show()
                System.out.println("$error");
            }
        })
    }

    override fun onDestroy() {
        socketHandler.disconnectSocket()
        super.onDestroy()
    }

    companion object {
        const val USERNAME = "username"
        const val OTHERNAME = "othername"
    }

    private fun playNotificationSound() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Hiển thị thông báo
    private fun showNotification(context: Context, message: String) {
        val intent = Intent(context, ChatActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
                .setSmallIcon(com.example.chatapp.R.drawable.chat)
                .setContentTitle("New Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }

    // Gọi các hàm này khi nhận được tin nhắn mới
    private fun onNewMessageReceived(message: String) {
        playNotificationSound()
        showNotification(this, message)
    }
}
