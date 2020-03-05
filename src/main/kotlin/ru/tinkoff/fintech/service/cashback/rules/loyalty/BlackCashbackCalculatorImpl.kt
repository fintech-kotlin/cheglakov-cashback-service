package ru.tinkoff.fintech.service.cashback.rules.loyalty

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.CashbackCalculator

class BlackCashbackCalculatorImpl : CashbackCalculator {

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        return 0.01 * transactionInfo.transactionSum
    }
}