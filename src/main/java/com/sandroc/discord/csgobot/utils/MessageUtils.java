package com.sandroc.discord.csgobot.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.sandroc.discord.csgobot.ILanding;
import net.dv8tion.jda.core.EmbedBuilder;

import java.io.File;

public class MessageUtils {
    private ILanding landing;

    public MessageUtils(ILanding landing) {
        this.landing = landing;
    }

    public void sendMessage(CommandEvent event, String message) {
        event.getChannel().sendMessage(this.landing.getMethods().capitalizeSentence(message)).queue();
    }

    public void sendMessage(CommandEvent event, EmbedBuilder embedBuilder) {
        if (embedBuilder != null) {
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }

    public void sendMessage(CommandEvent event, File file, EmbedBuilder embedBuilder) {
        event.getChannel().sendFile(file).embed(embedBuilder.build()).queue();
    }
}
