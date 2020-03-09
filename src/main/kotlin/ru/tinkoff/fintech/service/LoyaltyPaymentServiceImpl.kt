package ru.tinkoff.fintech.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.db.entity.LoyaltyPaymentEntity
import ru.tinkoff.fintech.db.repository.LoyaltyPaymentRepository
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class LoyaltyPaymentServiceImpl  @Autowired constructor(
    private val loyaltyPaymentRepository: LoyaltyPaymentRepository
) : LoyaltyPaymentService {

    @Value("\${paimentprocessing.sign}")
    private val sign: String = ""

    @Value("\${paimentprocessing.cashback-period.month}")
    private val period: Long = 1

    override fun calculateTotalAmount(cardId: String): Double {
        return loyaltyPaymentRepository.findAllBySignAndCardIdAndDateTimeAfter(
            sign,
            cardId,
            LocalDate.now().minusMonths(period).atStartOfDay()
        ).map { loyaltyPayment -> loyaltyPayment.value }.sum()
    }

    override fun saveLoyaltyPayment(cardId: String, transactionId: String, cashbackAmount: Double) {
        val loyaltyPaymentEntity = LoyaltyPaymentEntity(
            sign = sign,
            value = cashbackAmount,
            cardId = cardId,
            dateTime = LocalDateTime.now(),
            transactionId = transactionId
        )
        loyaltyPaymentRepository.save(loyaltyPaymentEntity)
    }

}