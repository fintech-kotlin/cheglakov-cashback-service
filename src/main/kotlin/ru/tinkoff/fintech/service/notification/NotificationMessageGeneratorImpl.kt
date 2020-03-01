package ru.tinkoff.fintech.service.notification

import freemarker.template.Configuration
import freemarker.template.Template
import ru.tinkoff.fintech.model.NotificationMessageInfo
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.io.Writer
import java.util.Locale
import kotlin.collections.HashMap

private const val TEMPLATES_PATH = "/templates/"
private const val CASH_TEMPLATE_FILENAME = "cashback.txt.ftl"
private const val TEMPLATE_PARAMETER_KEY_CARD_NUMBER = "maskCardNumber"
private const val TEMPLATE_PARAMETER_KEY_NOTIFICATION_INFO = "notificationMessageInfo"

class NotificationMessageGeneratorImpl(
    private val cardNumberMasker: CardNumberMasker
) : NotificationMessageGenerator {

    override fun generateMessage(notificationMessageInfo: NotificationMessageInfo): String {
        val cfg = Configuration(Configuration.VERSION_2_3_0)
        cfg.setClassForTemplateLoading(NotificationMessageGeneratorImpl::class.java, TEMPLATES_PATH)
        cfg.setEncoding(Locale.getDefault(), Charsets.UTF_8.toString())
        val template: Template = cfg.getTemplate(CASH_TEMPLATE_FILENAME)
        val input: MutableMap<String, Any> = HashMap()
        input[TEMPLATE_PARAMETER_KEY_NOTIFICATION_INFO] = notificationMessageInfo
        input[TEMPLATE_PARAMETER_KEY_CARD_NUMBER] = cardNumberMasker.mask(notificationMessageInfo.cardNumber)

        val mergedTemplate = ByteArrayOutputStream()
        val output: Writer = OutputStreamWriter(mergedTemplate)
        template.process(input, output)
        return mergedTemplate.toString().trimIndent()
    }
}