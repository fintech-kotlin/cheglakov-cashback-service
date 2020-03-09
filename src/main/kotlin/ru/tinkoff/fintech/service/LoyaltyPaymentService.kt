package ru.tinkoff.fintech.service

interface LoyaltyPaymentService {
    fun calculateTotalAmount(cardId: String): Double
    fun saveLoyaltyPayment(cardId: String, transactionId: String, cashbackAmount: Double)
}