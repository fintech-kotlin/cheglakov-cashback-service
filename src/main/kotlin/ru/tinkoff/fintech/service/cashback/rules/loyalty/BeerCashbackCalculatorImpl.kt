package ru.tinkoff.fintech.service.cashback.rules.loyalty

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.cashback.MCC_BEER
import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.*

private const val FIRST_NAME = "Олег"
private const val LAST_NAME = "Олегов"
private const val FIRST_NAME_MATCH_PERCENT = 0.07
private const val FIRST_AND_LAST_NAME_MATCH_PERCENT = 0.1
private const val CURRENT_MONTH_PERCENT = 0.05
private const val PREVIOUS_OR_NEXT_MONTH_PERCENT = 0.03
private const val DEFAULT_PERCENT = 0.02

class BeerCashbackCalculatorImpl : CashbackCalculator {

    private fun getMonthName(month: Int): String {
        val symbols = DateFormatSymbols(Locale("ru"))
        return symbols.months[month - 1]
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {

        if (MCC_BEER != transactionInfo.mccCode) {
            return 0.0
        }

        return maxOf(calculateByName(transactionInfo), calculateByDate(transactionInfo),
            DEFAULT_PERCENT * transactionInfo.transactionSum)

    }

    private fun calculateByName(transactionInfo: TransactionInfo): Double {
        return if (FIRST_NAME.equals(transactionInfo.firstName, true)) {

            if (LAST_NAME.equals(transactionInfo.lastName, true)) {
                FIRST_AND_LAST_NAME_MATCH_PERCENT * transactionInfo.transactionSum
            } else {
                FIRST_NAME_MATCH_PERCENT * transactionInfo.transactionSum
            }

        } else {
            0.0
        }
    }

    private fun calculateByDate(transactionInfo: TransactionInfo): Double {
        if (transactionInfo.firstName[0].equals(getMonthName(LocalDate.now().month.value)[0], true)) {
            return CURRENT_MONTH_PERCENT * transactionInfo.transactionSum
        }

        if (transactionInfo.firstName[0].equals(getMonthName(LocalDate.now().minusMonths(1).monthValue)[0], true)
            || transactionInfo.firstName[0].equals(getMonthName(LocalDate.now().plusMonths(1).monthValue)[0], true)
        ) {
            return PREVIOUS_OR_NEXT_MONTH_PERCENT * transactionInfo.transactionSum
        }

        return 0.0

    }

}