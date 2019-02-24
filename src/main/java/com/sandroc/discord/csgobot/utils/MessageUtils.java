package com.sandroc.discord.csgobot.utils;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;

public class MessageUtils {

    public static void sendMessage(CommandEvent event, String message) {
        event.getChannel().sendMessage(Methods.capitalizeSentence(message)).queue();
    }

    public static void sendMessage(CommandEvent event, EmbedBuilder embedBuilder) {
        if (embedBuilder != null) {
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
    }
}
