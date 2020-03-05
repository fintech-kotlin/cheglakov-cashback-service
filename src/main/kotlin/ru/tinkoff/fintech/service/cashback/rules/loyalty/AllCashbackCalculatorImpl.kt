package ru.tinkoff.fintech.service.cashback.rules.loyalty

import ru.tinkoff.fintech.model.TransactionInfo
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.cashback.MCC_SOFTWARE

class AllCashbackCalculatorImpl : CashbackCalculator {

    private fun isPalindromeWithOneMiss(number: Int): Boolean {
        val stringAmount = number.toString()
        var countMissed = 0
        for (i in 0..stringAmount.length / 2) {
            if (stringAmount[i] != stringAmount[stringAmount.length - i - 1]) countMissed++
        }
        return countMissed < 2
    }

    private tailrec fun gcd(firstNumber: Int, secondNumber: Int): Int {
        return if (firstNumber == 0) secondNumber else gcd(secondNumber % firstNumber, firstNumber)
    }

    private fun lsd(firstNumber: Int, secondNumber: Int): Double {
        return (firstNumber * secondNumber / gcd(firstNumber, secondNumber)).toDouble()
    }

    override fun calculateCashback(transactionInfo: TransactionInfo): Double {
        return if (MCC_SOFTWARE == transactionInfo.mccCode && isPalindromeWithOneMiss((transactionInfo.transactionSum * 100).toInt())) {
            transactionInfo.transactionSum * (lsd(
                transactionInfo.firstName.length,
                transactionInfo.lastName.length
            ) / 100000.0)
        } else {
            0.0
        }
    }
}
