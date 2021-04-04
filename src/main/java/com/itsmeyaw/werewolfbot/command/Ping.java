package com.itsmeyaw.werewolfbot.command;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.gateway.GatewayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class Ping implements Command {

    private final Logger LOGGER = LoggerFactory.getLogger(Ping.class);

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public void execute(Message m) {
        m.getAuthorAsMember().map(Member::getDisplayName).subscribe(name -> LOGGER.info(String.format("Ping received! [From:%s]", name)));
        m.getChannel().flatMap(mc -> mc.createMessage("Pong!\nMy Latency is " +
                m.getClient()
                        .getGatewayClient(0)
                        .map(GatewayClient::getResponseTime)
                        .map(Duration::toMillis)
                        .map(Object::toString)
                        .orElse("<Error fetching latency>")
                + " ms"))
                .subscribe();
    }
}
