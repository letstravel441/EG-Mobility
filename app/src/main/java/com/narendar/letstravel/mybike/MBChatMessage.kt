package com.narendar.letstravel.mybike

class MBChatMessage(
    val id: String ?= null,
    val text: String ?= null,
    val fromId: String ?= null,
    val fromUsername: String ?= null,
    val toId: String ?= null,
    val toUsername: String ?= null,
    val timestamp: Long ?= -1
)