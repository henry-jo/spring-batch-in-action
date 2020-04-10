package com.henry.springbatch.job

import com.henry.springbatch.entity.User
import com.henry.springbatch.entity.UserDetail
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory

@Configuration
class JpaItemWriterJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory
) {
    private val chunkSize = 10

    @Bean
    fun jpaItemWriterJob() = jobBuilderFactory["jpaItemWriterJob"]
        .start(jpaItemWriterStep())
        .build()

    @Bean
    fun jpaItemWriterStep() = stepBuilderFactory["jpaItemWriterStep"]
        .chunk<User, UserDetail>(chunkSize)
        .reader(jpaItemWriterReader())
        .processor(jpaItemProcessor())
        .writer(jpaItemWriter())
        .build()

    @Bean
    fun jpaItemWriterReader() = JpaPagingItemReaderBuilder<User>()
        .name("jpaItemWriterReader")
        .entityManagerFactory(entityManagerFactory)
        .pageSize(chunkSize)
        .queryString("SELECT u FROM User u")
        .build()

    @Bean
    fun jpaItemProcessor(): ItemProcessor<User, UserDetail> {
        return ItemProcessor { user ->
            UserDetail(
                userId = user.id,
                address = "Anyang"
            )
        }
    }

    @Bean
    fun jpaItemWriter(): JpaItemWriter<UserDetail> {
        val jpaItemWriter: JpaItemWriter<UserDetail> = JpaItemWriter<UserDetail>()
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory) // Reader 뿐만 아니라 Writer에도 Entity Manager Factory 설정
        return jpaItemWriter
    }
}