package ru.tinkoff.fintech.service.cashback.rules.commons

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.CashbackCalculator

private const val TRANSACTION_DENOMINATOR = 666

class SixRule : CashbackCalculator {

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        return if (transactionInfo.transactionSum % TRANSACTION_DENOMINATOR == 0.0) 6.66 else 0.0
    }

}