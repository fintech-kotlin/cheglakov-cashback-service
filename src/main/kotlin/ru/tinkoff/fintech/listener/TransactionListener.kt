package ru.tinkoff.fintech.listener

import mu.KLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.tinkoff.fintech.model.Transaction
import ru.tinkoff.fintech.service.ProcessTransactionService
import kotlin.Exception

@Component
class TransactionListener @Autowired constructor(
    private val processTransactionService: ProcessTransactionService
) {
    companion object: KLogging()

    @KafkaListener(
        topics = ["\${paimentprocessing.kafka.consumer.topic}"],
        groupId = "\${paimentprocessing.kafka.consumer.groupId}"
    )
    fun onMessage(transaction: Transaction) {

        try {
            processTransactionService.processTransaction(transaction)
        } catch (e: Exception) {
            logger.info("Transaction processing failed successfully" , e)
        }

    }

}