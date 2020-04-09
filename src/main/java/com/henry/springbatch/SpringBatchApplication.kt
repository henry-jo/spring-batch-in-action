package com.henry.springbatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@EnableBatchProcessing
class SpringBatchApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringBatchApplication::class.java, *args)
}