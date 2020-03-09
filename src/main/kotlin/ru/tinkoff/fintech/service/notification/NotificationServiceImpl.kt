package ru.tinkoff.fintech.service.notification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.tinkoff.fintech.client.NotificationServiceClient
import ru.tinkoff.fintech.model.NotificationMessageInfo

@Service
class NotificationServiceImpl @Autowired constructor(
    private val notificationServiceClient: NotificationServiceClient,
    private val notificationMessageGenerator: NotificationMessageGenerator
) : NotificationService {

    override fun sendNotification(clientId: String, notificationMessageInfo: NotificationMessageInfo) {

        notificationServiceClient.sendNotification(clientId, notificationMessageGenerator.generateMessage(notificationMessageInfo))

    }
}