package ru.tinkoff.fintech.service.notification

class CardNumberMaskerImpl: CardNumberMasker {

    override fun mask(cardNumber: String, maskChar: Char, start: Int, end: Int): String {
        if (cardNumber.isEmpty()) return ""
        if (start > end) throw Exception("Start index cannot be greater than end index")
        if (cardNumber.length < end) return "################"
        return cardNumber.replaceRange(start, end, maskChar.toString().repeat(end - start))
    }
}