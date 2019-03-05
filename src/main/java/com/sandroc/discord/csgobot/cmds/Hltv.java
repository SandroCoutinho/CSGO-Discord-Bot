package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.hltv.gson.Match;
import com.sandroc.discord.csgobot.hltv.gson.News;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.net.MalformedURLException;
import java.net.URL;

public class Hltv extends Command {
    private final ILanding landing;

    public Hltv(ILanding landing) {
        this.name = "hltv";
        this.help = "Grabs HLTV data from the API";
        this.arguments = "<news> <index>/ <results> <teamName>";
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = false;

        this.landing = landing;
    }

    @Override
    protected void execute(CommandEvent event) {
        try {
            String[] items = event.getArgs().split("\\s+");

            if (items[0].equalsIgnoreCase("news")) {
                News         news    = this.landing.getHLTVInfo().getNewsByIndex((items.length == 2 ? Integer.parseInt(items[1]) : 0));
                EmbedBuilder builder = new EmbedBuilder();

                builder.setTitle(news.title);
                builder.setDescription(news.description);
                builder.setAuthor("HLTV", String.valueOf(new URL(news.link)));
                builder.setFooter(news.date, "https://www.hltv.org/img/static/favicon/favicon-32x32.png");

                this.landing.getMessageUtils().sendMessage(event, builder);
            } else if (items[0].equalsIgnoreCase("results")) {
                Match match;

                if (items.length >= 2) {
                    match = this.landing.getHLTVInfo().getResultForTeam(items[1]);
                } else {
                    match = this.landing.getHLTVInfo().getResults()[0];
                }

                if (match != null) {
                    EmbedBuilder builder = new EmbedBuilder();

                    builder.setTitle(match.event, "https://hltv.org" + match.matchId);
                    builder.setDescription(match.maps);

                    builder.addField("Team Names", match.teamOne.name + "\n\n" + match.teamTwo.name, true);
                    builder.addField("Score", match.teamOne.result + "\n\n" + match.teamTwo.result, true);

                    this.landing.getMessageUtils().sendMessage(event, builder);
                } else {
                    this.landing.getMessageUtils().sendMessage(event, "Match not found!");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
