package ru.yandex.practicum;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.yandex.practicum.kafka_client.KafkaProperties;

@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties.class)
public class AnalyzerApp
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
