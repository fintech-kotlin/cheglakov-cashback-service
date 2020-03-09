package ru.tinkoff.fintech.service.cashback

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.rules.commons.SixRule
import ru.tinkoff.fintech.service.cashback.rules.loyalty.AllCashbackCalculatorImpl
import ru.tinkoff.fintech.service.cashback.rules.loyalty.BeerCashbackCalculatorImpl
import ru.tinkoff.fintech.service.cashback.rules.loyalty.BlackCashbackCalculatorImpl
import java.math.BigDecimal
import java.math.RoundingMode

internal const val LOYALTY_PROGRAM_BLACK = "BLACK"
internal const val LOYALTY_PROGRAM_ALL = "ALL"
internal const val LOYALTY_PROGRAM_BEER = "BEER"
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

private val COMMON_RULES = arrayOf<CashbackCalculator>(SixRule())

@Service
class CashbackCalculatorImpl : CashbackCalculator {

    @Value("\${paimentprocessing.cashback-max}")
    private val maxCashBack: Double = 3000.0

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {

        var cashBackAmount = when (transactionInfo.loyaltyProgramName) {
            LOYALTY_PROGRAM_BLACK ->
                BlackCashbackCalculatorImpl().calculateCashback(transactionInfo)
            LOYALTY_PROGRAM_ALL ->
                AllCashbackCalculatorImpl().calculateCashback(transactionInfo)
            LOYALTY_PROGRAM_BEER ->
                BeerCashbackCalculatorImpl().calculateCashback(transactionInfo)
            else -> 0.0
        }

        COMMON_RULES.forEach {
            cashBackAmount += it.calculateCashback(transactionInfo)
        }

        cashBackAmount = BigDecimal(cashBackAmount).setScale(2, RoundingMode.HALF_UP).toDouble()

        return minOf(cashBackAmount, maxCashBack - transactionInfo.cashbackTotalValue)

    }

}