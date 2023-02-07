package com.capstone.card.Writers;

import com.capstone.card.Models.CardModel;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.support.SynchronizedItemStreamWriter;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;

import java.util.Collections;

@StepScope
@Component
public class CardWriterXML {

    // ----------------------------------------------------------------------------------
    // --                                  SETUP                                       --
    // ----------------------------------------------------------------------------------

    // Destination path for export file
    @Value("#{jobParameters['outputPath_param']}")
    private String outputPath;

    // Complete path for file export
    //File file = new File(outputPath + "\\" + "card_list.xml");
    //File file = new File(outputPath + "/card_list.xml");

    // ----------------------------------------------------------------------------------
    // --                                METHODS                                       --
    // ----------------------------------------------------------------------------------

    // XML Writer - generate list of card numbers
    @StepScope
    @Bean("writer_Card")
    public SynchronizedItemStreamWriter<CardModel> xmlWriter() {

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(Collections.singletonMap("card", CardModel.class));

        StaxEventItemWriter<CardModel> writer = new StaxEventItemWriterBuilder<CardModel>()
                .name("userXmlWriter")
                .resource(new FileSystemResource(outputPath + "/card_list.xml"))
                .marshaller(marshaller)
                .rootTagName("cards")
                .build();

        // Make XML writer thread-safe
        SynchronizedItemStreamWriter<CardModel> synchronizedItemStreamWriter =
                new SynchronizedItemStreamWriterBuilder<CardModel>()
                        .delegate(writer)
                        .build();
        return synchronizedItemStreamWriter;
    }




}
