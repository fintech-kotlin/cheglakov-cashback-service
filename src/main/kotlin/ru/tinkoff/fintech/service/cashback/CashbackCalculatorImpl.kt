package ru.tinkoff.fintech.service.cashback

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
internal const val MAX_CASH_BACK = 3000.0
internal const val MCC_SOFTWARE = 5734
internal const val MCC_BEER = 5921

private val COMMON_RULES = arrayOf<CashbackCalculator>(SixRule())

class CashbackCalculatorImpl : CashbackCalculator {

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {

        var cashBackAmount = 0.0
        when (transactionInfo.loyaltyProgramName) {
            LOYALTY_PROGRAM_BLACK ->
                cashBackAmount = BlackCashbackCalculatorImpl()
                    .calculateCashback(transactionInfo)
            LOYALTY_PROGRAM_ALL ->
                cashBackAmount = AllCashbackCalculatorImpl()
                    .calculateCashback(transactionInfo)
            LOYALTY_PROGRAM_BEER ->
                cashBackAmount = BeerCashbackCalculatorImpl()
                    .calculateCashback(transactionInfo)
        }

        COMMON_RULES.iterator().forEach {
            cashBackAmount += it.calculateCashback(transactionInfo)
        }

        cashBackAmount = BigDecimal(cashBackAmount).setScale(2, RoundingMode.HALF_UP).toDouble()

        return minOf(cashBackAmount, MAX_CASH_BACK - transactionInfo.cashbackTotalValue)

    }

}