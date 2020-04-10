package com.henry.springbatch.job

import com.henry.springbatch.entity.User
import mu.KLogging
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory

@Configuration
class JpaPagingItemReaderJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory
) {
    companion object : KLogging()

    private val chunkSize = 10

    @Bean
    fun jpaPagingItemReaderJob() = jobBuilderFactory["jpaPagingItemReaderJob"]
        .start(jpaPagingItemReaderStep())
        .build()

    @Bean
    fun jpaPagingItemReaderStep() = stepBuilderFactory["jpaPagingItemReaderStep"]
        // 첫번째 User는 Reader에서 반환될 타입, 두번째 User는 Writer에 파라미터로 넘어올 타입
        // chunkSize 만큼 Reader & Writer의 트랜잭션 범위
        .chunk<User, User>(chunkSize)
        .reader(jpaPagingItemReader())
        .writer(jpaPagingItemWriter())
        .build()

    @Bean
    fun jpaPagingItemReader() = JpaPagingItemReaderBuilder<User>()
        .name("jpaPagingItemReader")
        .entityManagerFactory(entityManagerFactory) // JPA이므로 EntityManagerFactory 설정
        .pageSize(chunkSize)
        .queryString("SELECT u FROM User u")
        .build()

    private fun jpaPagingItemWriter() = ItemWriter<User> { list ->
        for (user in list) {
            logger.info("User Info = {}", user)
        }
    }
}