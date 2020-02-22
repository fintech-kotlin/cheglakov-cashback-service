package ru.tinkoff.fintech.service.notification

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

private const val CARD_NUMBER = "4155373308145350"

class CardNumberMaskerImplTest {

    private val observable = CardNumberMaskerImpl()

    @Test
    fun testMaskingEmptyCardNumber() {
        val maskedCardNumber = observable.mask("")
        assertEquals("", maskedCardNumber)
    }

    @Test
    fun testMaskingFirstFiveDigits() {
        val maskedCardNumber = observable.mask(CARD_NUMBER)
        assertEquals("#####73308145350", maskedCardNumber)
    }

    @Test
    fun testMaskingFiveDigitsInTheMiddle() {
        val maskedCardNumber = observable.mask(CARD_NUMBER, start = 5, end = 10)
        assertEquals("41553#####145350", maskedCardNumber)
    }

    @Test
    fun testMaskingLastFiveDigits() {
        val maskedCardNumber = observable.mask(CARD_NUMBER, start = 11, end = 16)
        assertEquals("41553733081#####", maskedCardNumber)
    }

    @Test
    fun testMaskingWithLastIndexGreaterThenCardNumber() {
        val maskedCardNumber = observable.mask(cardNumber = CARD_NUMBER, end = 100)
        assertEquals("################", maskedCardNumber)
    }

    @Test
    fun testMaskingWithEndIndexBiggerThanStartIndex() {
        assertThrows(java.lang.Exception::class.java,
            {
                observable.mask(
                    cardNumber = CARD_NUMBER,
                    start = 100,
                    end = 4
                )
            },
            "Start index cannot be greater than end index"
        )
    }

    @Test
    fun testMaskingLengthEqualsZero() {
        val maskedCardNumber = observable.mask(cardNumber = CARD_NUMBER, start = 4, end = 4)
        assertEquals(CARD_NUMBER, maskedCardNumber)
    }

    @Test
    fun testMaskingNonDefaultMaskChar() {
        assertEquals("41553733081$$$$$", observable.mask(CARD_NUMBER, '$', start = 11, end = 16))
    }

}