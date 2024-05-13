package com.example.chatapp.ChatApp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.Adapter.ChatAdapter
import com.example.chatapp.Class.Chat
import com.example.chatapp.R
import com.example.chatapp.SocketHandler.SocketHandler
import com.example.chatapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ChatActivity : AppCompatActivity() {


    private lateinit var socketHandler: SocketHandler
    private lateinit var binding: ActivityMainBinding
    private lateinit var chatAdapter: ChatAdapter

    private val chatList = mutableListOf<Chat>()

    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        socketHandler = SocketHandler()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)


        bottomNavigationView.selectedItemId = R.id.act

        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.chat -> return@setOnItemSelectedListener true
                R.id.act -> {
                    startActivity(Intent(applicationContext, ProFile::class.java))
                    overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left)
                    finish()
                    return@setOnItemSelectedListener true
                }

                R.id.other -> {
                    startActivity(Intent(applicationContext, ProFile::class.java))
                    overridePendingTransition(R.anim.slide_in_rigth, R.anim.slide_out_left)
                    finish()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

        userName = intent.getStringExtra("username") ?: "quang" // ?: "" là giá trị mặc định nếu không có "username" trong intent

        if (userName.isEmpty()) {
            finish()
        } else {
            socketHandler = SocketHandler()

            chatAdapter = ChatAdapter()

            binding.rvChat.apply {
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = chatAdapter
            }

            binding.btnSend.setOnClickListener {
                val message = binding.etMsg.text.toString()
                if (message.isNotEmpty()) {
                    val chat = Chat(
                        username = userName,
                        text = message
                    )
                    socketHandler.emitChat(chat)
                    binding.etMsg.setText("")
                }
            }

            socketHandler.onNewChat.observe(this) {
                val chat = it.copy(isSelf = it.username == userName)
                chatList.add(chat)
                chatAdapter.submitChat(chatList)
                binding.rvChat.scrollToPosition(chatList.size - 1)
            }
        }


    }


    override fun onDestroy() {
        socketHandler.disconnectSocket()
        super.onDestroy()
    }

    companion object{
        const val USERNAME = "username"
    }

}