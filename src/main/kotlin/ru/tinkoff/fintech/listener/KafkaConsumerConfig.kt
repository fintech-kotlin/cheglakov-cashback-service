package ru.tinkoff.fintech.listener

import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import ru.tinkoff.fintech.commons.annotation.Utils.Factory.getObjectMapper
import ru.tinkoff.fintech.model.Transaction

@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Value("\${paimentprocessing.kafka.consumer.bootstrapServers}")
    private val bootsrap: String = ""

    @Value("\${paimentprocessing.kafka.consumer.groupId}")
    private val groupId: String = ""

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Transaction> {
        val factory =
            ConcurrentKafkaListenerContainerFactory<String, Transaction>()
        factory.consumerFactory = greetingConsumerFactory()
        return factory
    }

    @Bean
    fun greetingConsumerFactory(): ConsumerFactory<String?, Transaction?> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootsrap
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG] = 80000
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java

        val jsonDeserializer = JsonDeserializer(Transaction::class.java, getObjectMapper(),false)
        jsonDeserializer.addTrustedPackages("ru.tinkoff.bpm.kotlincoursepaymentprocessing.kafka")

        return DefaultKafkaConsumerFactory(
            props,
            org.apache.kafka.common.serialization.StringDeserializer(),
            jsonDeserializer
        )
    }

    @Bean
    fun greetingKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Transaction>? {
        val factory: ConcurrentKafkaListenerContainerFactory<String, Transaction> =
            ConcurrentKafkaListenerContainerFactory<String, Transaction>()
        factory.consumerFactory = greetingConsumerFactory()
        return factory
    }

}