package com.itsmeyaw.werewolfbot.command;

import com.itsmeyaw.werewolfbot.data.quote.QuoteEntity;
import com.itsmeyaw.werewolfbot.data.quote.QuoteRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.itsmeyaw.werewolfbot.data.quote"})
public class QuoteList implements Command{

    private final Logger LOGGER = LoggerFactory.getLogger(QuoteList.class);
    private final QuoteRepository qr;

    @Autowired
    public QuoteList(QuoteRepository qr) {
        this.qr = qr;
    }

    @Override
    public String getCommand() {
        return "quote list";
    }

    @Override
    public void execute(Message m) {
        long guildId = m.getGuildId().map(Snowflake::asLong).orElse(0L);

        LOGGER.info(String.format("Quote list is called on server: %s", guildId));

        List<QuoteEntity> quotes = qr.findAllBySnowflake(guildId);

        m.getChannel()
                .flatMap(mc -> {
                    StringBuilder sb = new StringBuilder("Quote(s) in this server:\n");
                    quotes.forEach(q -> sb.append(q.getQuote()).append(" -").append(q.getQuoteMaker()).append("\n"));
                    return mc.createMessage(sb.toString());
                })
                .subscribe();
    }
}
