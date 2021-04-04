package com.itsmeyaw.werewolfbot.command;

import com.itsmeyaw.werewolfbot.data.quote.QuoteRepository;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.itsmeyaw.werewolfbot.data.quote"})
public class Quote implements Command{

    private final Logger LOGGER = LoggerFactory.getLogger(Quote.class);
    private final QuoteRepository qr;

    @Autowired
    public Quote(QuoteRepository qr) {
        this.qr = qr;
    }

    @Override
    public String getCommand() {
        return "quote";
    }

    @Override
    public void execute(Message m) {

    }
}
