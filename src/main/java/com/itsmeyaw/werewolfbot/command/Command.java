package com.itsmeyaw.werewolfbot.command;

import discord4j.core.object.entity.Message;

public interface Command{
    String getCommand();
    void execute(Message m);
}
