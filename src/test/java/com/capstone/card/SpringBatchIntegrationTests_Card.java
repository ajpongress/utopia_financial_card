package com.capstone.card;

import com.capstone.card.Configurations.BatchConfigCard;
import com.capstone.card.Models.CardModel;
import com.capstone.card.Processors.CardProcessor;
import com.capstone.card.Readers.CardReaderCSV;
import com.capstone.card.TaskExecutors.TaskExecutor;
import com.capstone.card.Writers.CardWriterXML;
import org.aspectj.util.FileUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.io.File;

// ********************************************************************************
//                          Test Card Operations
// ********************************************************************************

@SpringBatchTest
@SpringJUnitConfig(classes = {
        BatchConfigCard.class,
        CardModel.class,
        CardReaderCSV.class,
        CardProcessor.class,
        CardWriterXML.class,
        TaskExecutor.class
})
@EnableAutoConfiguration

public class SpringBatchIntegrationTests_Card {

    // ----------------------------------------------------------------------------------
    // --                                  SETUP                                       --
    // ----------------------------------------------------------------------------------

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    private String INPUT = "src/test/resources/input/test_input.csv";
    private String EXPECTED_OUTPUT = "src/test/resources/output/expected_output_Card.xml";
    private String ACTUAL_OUTPUT = "src/test/resources/output/";

    @AfterEach
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    private JobParameters testJobParameters_Card() {

        return new JobParametersBuilder()
                .addString("file.input", INPUT)
                .addString("outputPath_param", ACTUAL_OUTPUT)
                .toJobParameters();
    }



    // ----------------------------------------------------------------------------------
    // --                                 TESTS                                        --
    // ----------------------------------------------------------------------------------

    @Test
    public void testBatchProcessFor_Card() throws Exception {

        // Load job parameters and launch job through test suite
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(testJobParameters_Card());
        JobInstance actualJobInstance = jobExecution.getJobInstance();
        ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

        // ----- Assertions -----
        File testInputFile = new File(INPUT);
        File testOutputFileExpected = new File(EXPECTED_OUTPUT);
        File testOutputFileActual = new File(ACTUAL_OUTPUT + "card_list.xml");

        // Match job names
        Assertions.assertEquals("generateCardsJob", actualJobInstance.getJobName());

        // Match job exit status to "COMPLETED"
        Assertions.assertEquals("COMPLETED", actualJobExitStatus.getExitCode());

        // Verify input file is valid and can be read
        Assertions.assertTrue(FileUtil.canReadFile(testInputFile));

        // Verify output (expected) file is valid and can be read
        Assertions.assertTrue(FileUtil.canReadFile(testOutputFileExpected));

        // Verify output (actual) file is valid and can be read
        Assertions.assertTrue(FileUtil.canReadFile(testOutputFileActual));


    }
}
