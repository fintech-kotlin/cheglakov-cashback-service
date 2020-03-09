package ru.tinkoff.fintech.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.client.CardServiceClient
import ru.tinkoff.fintech.client.ClientService
import ru.tinkoff.fintech.client.LoyaltyServiceClient
import ru.tinkoff.fintech.model.*
import ru.tinkoff.fintech.service.cashback.CashbackCalculator
import ru.tinkoff.fintech.service.notification.NotificationService

@Service
class ProcessTransactionService {

    @Autowired
    lateinit var cardServiceClient: CardServiceClient

    @Autowired
    lateinit var clientService: ClientService

    @Autowired
    lateinit var loyaltyServiceClient: LoyaltyServiceClient

    @Autowired
    lateinit var notificationService: NotificationService

    @Autowired
    lateinit var loyaltyPaymentService: LoyaltyPaymentService

    @Autowired
    lateinit var cashbackCalculator: CashbackCalculator

    fun processTransaction(transaction: Transaction) {
        val card = cardServiceClient.getCard(transaction.cardNumber)

        // TODO getClient and getLoyaltyProgram must be parallel
        val client = clientService.getClient(card.client)

        val loyaltyProgram = loyaltyServiceClient.getLoyaltyProgram(card.loyaltyProgram)

        val cashbackAmount = cashbackCalculator.calculateCashback(fillTransactionInfo(client, transaction, card, loyaltyProgram))

        notificationService.sendNotification(client.id, fillNotificationMessageInfo(cashbackAmount, transaction, client, loyaltyProgram))

        loyaltyPaymentService.saveLoyaltyPayment(card.id, transaction.transactionId, cashbackAmount)

    }

    private fun fillTransactionInfo(client: Client, transaction: Transaction, card: Card,
                                    loyaltyProgram: LoyaltyProgram) : TransactionInfo {
        return TransactionInfo(
            loyaltyProgramName = loyaltyProgram.name,
            transactionSum = transaction.value,
            cashbackTotalValue = loyaltyPaymentService.calculateTotalAmount(card.id),
            mccCode = transaction.mccCode,
            clientBirthDate = client.birthDate,
            firstName = client.firstName,
            middleName = client.middleName,
            lastName = client.lastName
        )
    }

    private fun fillNotificationMessageInfo(cashbackAmount: Double, transaction: Transaction,
                                            client: Client, loyaltyProgram: LoyaltyProgram) : NotificationMessageInfo {
        return NotificationMessageInfo(
            cashback = cashbackAmount,
            cardNumber = transaction.cardNumber,
            name = client.firstName,
            transactionSum = transaction.value,
            transactionDate = transaction.time,
            category = loyaltyProgram.name
        )
    }

}