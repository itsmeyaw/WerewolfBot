package com.itsmeyaw.werewolfbot.command;

import com.itsmeyaw.werewolfbot.data.quote.QuoteEntity;
import com.itsmeyaw.werewolfbot.data.quote.QuoteRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.itsmeyaw.werewolfbot.data.quote"})
public class QuoteAdd implements Command {

    private final Logger LOGGER = LoggerFactory.getLogger(QuoteAdd.class);
    private final QuoteRepository qr;

    @Autowired
    public QuoteAdd(QuoteRepository qr) {
        this.qr = qr;
    }

    @Override
    public String getCommand() {
        return "quote add";
    }

    @Override
    public void execute(Message m) {
        String content = m.getContent();

        if (!content.contains("-by")) {
            m.addReaction(ReactionEmoji.unicode("❌")).subscribe();
            m.getChannel().flatMap(c -> c.createMessage("Syntax is not fulfilled!\nSyntax: <prefix>quote add \"<quote>\" -by <name>")).subscribe();
            LOGGER.info("Failed to add quote");
            return;
        }

        String quote = content.replaceFirst("(.*)quote add(\\ *)\"", "").replaceAll("\"(\\ +)", "").replaceAll("-by(.*)", "");
        String maker = content.replaceFirst("(.*)-by(\\ *)", "");

        Snowflake snowflake = m.getGuildId().orElse(null);

        LOGGER.info(String.format("Adding quote in: %s, quote: [%s], maker: [%s]", snowflake, quote, maker));

        if (snowflake != null && !quote.isBlank() && !maker.isBlank()) {
            qr.save(new QuoteEntity(snowflake.asLong(), quote, maker));
            m.addReaction(ReactionEmoji.unicode("✅")).subscribe();
            LOGGER.info("Successfully added quote");
        } else {
            m.addReaction(ReactionEmoji.unicode("❌")).subscribe();
            LOGGER.info("Failed to add quote");
        }
    }
}
