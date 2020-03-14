package com.sandroc.discord.csgobot;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sandroc.discord.csgobot.cmds.*;
import com.sandroc.discord.csgobot.faceit.GetFaceItInfo;
import com.sandroc.discord.csgobot.hltv.GetHLTVInfo;
import com.sandroc.discord.csgobot.steam.GetSteamInfo;
import com.sandroc.discord.csgobot.utils.FileUtils;
import com.sandroc.discord.csgobot.utils.MessageUtils;
import com.sandroc.discord.csgobot.utils.Methods;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Landing implements ILanding {
    private static Landing       instance;
    private final  Methods       methods;
    private final  MessageUtils  messageUtils;
    private final  GetSteamInfo  steamInfo;
    private final  GetHLTVInfo   hltvInfo;
    private final  GetFaceItInfo faceItInfo;

    public Landing() {
        this.methods = new Methods(this);
        this.messageUtils = new MessageUtils(this);
        this.steamInfo = new GetSteamInfo(this);
        this.hltvInfo = new GetHLTVInfo(this);
        this.faceItInfo = new GetFaceItInfo(this);
    }

    public static void main(String[] args) throws Exception {
        if (!Files.exists(Paths.get("configs/default.settings"))) {
            FileUtils.writeDefaultConfigs();
        }

        FileUtils.newVersion();

        if (!Boolean.parseBoolean(FileUtils.getProperty("default", "botEnabled"))) {
            System.out.println("The bot is disabled. Please enable it in `default.settings`.");
            return;
        }

        EventWaiter          eventWaiter = new EventWaiter();
        CommandClientBuilder client      = new CommandClientBuilder();

        client.setGame(Game.playing("Counter-Strike Global Offensive"));

        client.setOwnerId("466822971856650260");
        client.setPrefix("!");
        client.setStatus(OnlineStatus.DO_NOT_DISTURB);

        client.addCommands(
                // INFORMATION COMMANDS //
                new SteamStats(getInstance()),
                new Hltv(getInstance()),

                // CHANCE COMMANDS //
                new Coinflip(getInstance()),
                new RandomMap(getInstance()),

                // VETO COMMANDS //
                new MapVeto(getInstance()),
                new Veto(getInstance()),
                new StopVeto(getInstance())
        );

        new JDABuilder(AccountType.BOT)
                .setToken(FileUtils.getProperty("default", "botToken"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListener(eventWaiter)
                .addEventListener(client.build())

                .build();
    }

    public static synchronized Landing getInstance() {
        if (instance == null) {
            instance = new Landing();
        }

        return instance;
    }

    @Override
    public Methods getMethods() {
        return this.methods;
    }

    @Override
    public GetSteamInfo getSteamInfo() {
        return steamInfo;
    }

    @Override
    public MessageUtils getMessageUtils() {
        return this.messageUtils;
    }

    @Override
    public GetHLTVInfo getHLTVInfo() {
        return hltvInfo;
    }

    @Override
    public GetFaceItInfo getFaceItInfo() {
        return faceItInfo;
    }
}
