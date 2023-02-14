package com.capstone.card.Configurations;

import com.capstone.card.Models.CardModel;
import com.capstone.card.Processors.CardProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class BatchConfigCard {

    // ----------------------------------------------------------------------------------
    // --                                  SETUP                                       --
    // ----------------------------------------------------------------------------------

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    @Qualifier("reader_Card")
    private SynchronizedItemStreamReader<CardModel> synchronizedItemStreamReader;

    @Autowired
    private CardProcessor cardProcessor;

    @Autowired
    @Qualifier("writer_Card")
    private SynchronizedItemStreamWriter<CardModel> xmlWriter;

    @Autowired
    @Qualifier("taskExecutor_Card")
    private org.springframework.core.task.TaskExecutor asyncTaskExecutor;



    // ----------------------------------------------------------------------------------
    // --                             STEPS & JOBS                                     --
    // ----------------------------------------------------------------------------------

    // Step - card generation
    @Bean
    public Step step_generateCards() {

        return new StepBuilder("generateCardsStep", jobRepository)
                .<CardModel, CardModel> chunk(50000, transactionManager)
                .reader(synchronizedItemStreamReader)
                .processor(cardProcessor)
                .writer(xmlWriter)
                .listener(new StepExecutionListener() {
                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("------------------------------------------------------------------");
                        log.info(stepExecution.getSummary());
                        log.info("------------------------------------------------------------------");

                        cardProcessor.clearAllTrackersAndCounters();

                        return StepExecutionListener.super.afterStep(stepExecution);
                    }
                })
                .taskExecutor(asyncTaskExecutor)
                .build();
    }

    // Job - card generation
    @Bean
    public Job job_generateCards() {

        return new JobBuilder("generateCardsJob", jobRepository)
                .start(step_generateCards())
                .build();
    }
}
