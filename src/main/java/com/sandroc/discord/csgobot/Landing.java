package com.sandroc.discord.csgobot;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sandroc.discord.csgobot.cmds.*;
import com.sandroc.discord.csgobot.utils.FileUtils;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Landing {

    public static void main(String[] args) throws Exception {
        if (!Files.exists(Paths.get("configs/default.settings"))) {
            System.out.println("Active?");
            FileUtils.writeDefaultConfigs();
        }

        if (!Boolean.parseBoolean(FileUtils.getProperty("default", "botEnabled"))) {
            System.out.println("The bot is disabled. Please enable it in DefaultSetting.");
            return;
        }

        EventWaiter          eventWaiter = new EventWaiter();
        CommandClientBuilder client      = new CommandClientBuilder();

        client.setGame(Game.playing("Counter-Strike Global Offensive"));

        client.setOwnerId(FileUtils.getProperty("default", "botOwnerId"));
        client.setPrefix(FileUtils.getProperty("default", "botCommandPrefix"));
        client.setStatus(OnlineStatus.DO_NOT_DISTURB);

        client.addCommands(
                // INFORMATION COMMANDS //
                new SteamStats(),

                // CHANCE COMMANDS //
                new Coinflip(),
                new RandomMap(),

                // VETO COMMANDS //
                new MapVeto(),
                new Veto(),
                new StopVeto()
        );

        new JDABuilder(AccountType.BOT)
                .setToken(FileUtils.getProperty("default", "botToken"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListener(eventWaiter)
                .addEventListener(client.build())
                .buildAsync();
    }
}
