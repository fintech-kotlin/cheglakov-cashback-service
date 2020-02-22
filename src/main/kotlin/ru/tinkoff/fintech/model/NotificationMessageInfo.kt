package ru.tinkoff.fintech.model

import java.time.LocalDateTime

data class NotificationMessageInfo(
    val name: String = "",
    val cardNumber: String = "",
    val cashback: Double = 0.0,
    val transactionSum: Double = 0.0,
    val category: String = "",
    val transactionDate: LocalDateTime = LocalDateTime.now()
)