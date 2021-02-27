package com.krdevteam.buzzmap.entity

import java.util.*

data class ChatMessage(
    val text: String,
    val user: String,
    val timestamp: Date
)