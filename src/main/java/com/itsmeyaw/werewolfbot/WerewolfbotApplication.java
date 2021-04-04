package com.itsmeyaw.werewolfbot;

import com.itsmeyaw.werewolfbot.command.Command;
import com.itsmeyaw.werewolfbot.data.server.DiscordServerEntity;
import com.itsmeyaw.werewolfbot.data.server.ServerRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.guild.GuildDeleteEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.TextChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@ComponentScan(basePackages = {"com.itsmeyaw.werewolfbot.command", "com.itsmeyaw.werewolfbot.data.server"})
public class WerewolfbotApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(WerewolfbotApplication.class);
    private static final String DEFAULT_PREFIX = "!";

    public static String loginAnnounce(ReadyEvent event) {
        StringBuilder sb = new StringBuilder("\nLogged in!\n");
        sb.append("Username: ").append(event.getSelf().getUsername()).append("#").append(event.getSelf().getDiscriminator()).append("\n");
        sb.append("Total server: ").append(event.getSelf().getClient().getGuilds().reduce(0, (acc, ignored) -> acc++).block()).append("\n");
        return sb.toString();
    }

    @Autowired
    public WerewolfbotApplication(ServerRepository serverRepository, List<Command> commandList) {
        GatewayDiscordClient client = DiscordClientBuilder.create(System.getenv("DISCORD_TOKEN"))
                .build()
                .login()
                .block();

        if (null == client) throw new IllegalStateException("Client is null!");

        client.getEventDispatcher().on(ReadyEvent.class)
                .map(WerewolfbotApplication::loginAnnounce)
                .subscribe(LOGGER::info);

        // Create new entry in database if the bot join a server
        client.getEventDispatcher().on(GuildCreateEvent.class)
                .map(GuildCreateEvent::getGuild)
                .map(g -> {
                    LOGGER.info(String.format("Guild Create Event invoked! (%d)", g.getId().asLong()));
                    if (!serverRepository.existsById(g.getId().asLong())) {
                        serverRepository.save(new DiscordServerEntity(g.getId().asLong(), DEFAULT_PREFIX));
                        LOGGER.info(String.format("Added Discord Server to Database with Name: %s", g.getName()));
                    }
                    return g;
                })
                .doOnError(throwable -> LOGGER.error(throwable.getMessage()))
                .subscribe();

        // Delete the entry of server in database if the bot leave a server
        client.getEventDispatcher().on(GuildDeleteEvent.class)
                .map(GuildDeleteEvent::getGuild)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(g -> {
                    LOGGER.info(String.format("Guild Delete Event invoked! (%d)", g.getId().asLong()));
                    serverRepository.deleteById(g.getId().asLong());
                    return g;
                })
                .subscribe();

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(m -> m.getAuthor().map(a -> !a.isBot()).orElse(false))
                .filter(m -> m.getChannel().map(c -> c.getClass() == TextChannel.class).blockOptional().orElse(false))
                .map(m -> {
                    final DiscordServerEntity server = serverRepository.findBySnowflake(m.getGuildId().orElse(Snowflake.of(0L)).asLong());
                    LOGGER.info(String.format("Server queried! Snowflake: %s", server));
                    if (null != server) {
                        final Command cmd = Flux.fromIterable(commandList).filter(command -> m.getContent().startsWith(server.getPrefix() + command.getCommand())).blockFirst();
                        if (cmd != null) {
                            cmd.execute(m);
                        }
                    }
                    return m;
                })
                .subscribe();

        client.onDisconnect().block();
    }

    public static void main(String[] args) {
        SpringApplication.run(WerewolfbotApplication.class, args);
    }

}
