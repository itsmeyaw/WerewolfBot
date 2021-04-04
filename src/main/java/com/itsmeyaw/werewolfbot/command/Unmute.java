package com.itsmeyaw.werewolfbot.command;

import discord4j.common.util.Snowflake;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Component
public class Unmute implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(Mute.class);

    @Override
    public String getCommand() {
        return "unmute";
    }

    @Override
    public void execute(Message m) {
        m.getAuthorAsMember().map(Member::getDisplayName).subscribe(name -> LOGGER.info(String.format("Unmute received! [From:%s]", name)));

        Optional<Snowflake> channel = m.getAuthorAsMember()
                .flatMap(Member::getVoiceState)
                .map(VoiceState::getChannelId)
                .block();

        if (channel != null) {
            long snowflake = channel.get().asLong();
            Flux.from(m.getGuild())
                    .flatMap(Guild::getMembers)
                    .filter(me -> me.getVoiceState()
                            .flatMap(VoiceState::getChannel)
                            .map(VoiceChannel::getId)
                            .map(e -> e.asLong() == snowflake)
                            .blockOptional()
                            .orElse(false))
                    .flatMap(me -> me.edit(g -> g.setMute(false)))
                    .subscribe();
        } else {
            m.getChannel()
                    .flatMap(c -> c.createMessage("You have to be connected to a voice channel!"))
                    .subscribe();
        }
    }
}
