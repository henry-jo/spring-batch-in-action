package com.henry.springbatch.job

import mu.KLogging
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration // Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용
class SimpleJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    companion object : KLogging()

    @Bean
    fun simpleJob() = jobBuilderFactory.get("simpleJob")
        .start(simpleStep1())
        .build()

    @Bean
    fun simpleStep1() = stepBuilderFactory.get("simpleStep1")
        .tasklet { stepContribution: StepContribution, chunkContext: ChunkContext ->
            logger.info(">>>>>>>>>This is Step1")
            return@tasklet RepeatStatus.FINISHED
        }.build()
}