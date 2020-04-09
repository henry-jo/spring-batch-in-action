package com.henry.springbatch.job

import mu.KLogging
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StepNextJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    companion object : KLogging()

    // next()를 이용하여 step을 순차적으로 실행
    @Bean
    fun stepNextJob() = jobBuilderFactory.get("stepNextJob")
        .start(step1())
        .next(step2())
        .next(step3())
        .build()

    @Bean
    fun step1() = stepBuilderFactory.get("step1")
        .tasklet { _: StepContribution, _: ChunkContext ->
            logger.info(">>>>> This is Step 1")
            return@tasklet RepeatStatus.FINISHED
        }.build()

    @Bean
    fun step2() = stepBuilderFactory.get("step2")
        .tasklet { _: StepContribution, _: ChunkContext ->
            logger.info(">>>>> This is Step 2")
            return@tasklet RepeatStatus.FINISHED
        }.build()

    @Bean
    fun step3() = stepBuilderFactory.get("step3")
        .tasklet { _: StepContribution, _: ChunkContext ->
            logger.info(">>>>> This is Step 3")
            return@tasklet RepeatStatus.FINISHED
        }.build()
}