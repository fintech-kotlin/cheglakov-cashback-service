package ru.tinkoff.fintech.model

import ru.tinkoff.fintech.commons.annotation.NoArgAnnotation
import java.time.LocalDateTime

@NoArgAnnotation
data class Transaction(
    val transactionId: String,
    val time: LocalDateTime,
    val cardNumber: String,
    val operationType: Int,
    val value: Double,
    val currencyCode: String,
    val mccCode: Int?
)
