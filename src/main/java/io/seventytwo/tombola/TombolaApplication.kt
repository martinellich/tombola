package io.seventytwo.tombola

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class TombolaApplication

fun main(args: Array<String>) {
    runApplication<TombolaApplication>(*args)
}
