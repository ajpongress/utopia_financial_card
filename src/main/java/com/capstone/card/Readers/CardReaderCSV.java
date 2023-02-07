package com.capstone.card.Readers;

import com.capstone.card.Models.CardModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CardReaderCSV {

    // FlatFileItemReader
    @StepScope
    @Bean("reader_Card")
    public SynchronizedItemStreamReader<CardModel> synchronizedItemStreamReader(
            @Value("#{jobParameters['file.input']}")
            String source_input
    ) throws UnexpectedInputException,
            NonTransientResourceException, ParseException {

        FlatFileItemReader<CardModel> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource(source_input));
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper((line, lineNumber) -> {
            String[] fields = line.split(",");
            CardModel cardModel = new CardModel();

            cardModel.setUserID(Long.parseLong(fields[0]));
            cardModel.setCardID(Long.parseLong(fields[1]));
            return cardModel;
        });

        // Make FlatFileItemReader thread-safe
        return new SynchronizedItemStreamReaderBuilder<CardModel>()
                .delegate(itemReader)
                .build();
    }
}
