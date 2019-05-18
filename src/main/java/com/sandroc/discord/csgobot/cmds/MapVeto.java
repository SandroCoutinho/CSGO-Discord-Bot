package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.data.Constants;
import com.sandroc.discord.csgobot.utils.FileUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.Arrays;

@CommandInfo(
        name = { "Coinflip" },
        description = "CSGO Map Veto System"
)
@Author("SandroC")
public class MapVeto extends Command {
    private final ILanding landing;

    public MapVeto(ILanding landing) {
        this.name = "start";
        this.help = "starts a new veto";
        this.cooldown = 30;
        this.cooldownScope = CooldownScope.GUILD;
        this.arguments = "<boX> <@captainOne> <@captainTwo>";
        this.userPermissions = new Permission[]{ Permission.MANAGE_CHANNEL };
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = true;
        this.landing = landing;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] items        = event.getArgs().split("\\s+");
        String[] requiredArgs = this.getArguments().split("\\s+");
        String[] bestOfValues = { "1", "2", "3", "5" };
        if (!this.landing.getMethods().isMatchingNumberOfArgs(requiredArgs, items)) {
            this.landing.getMessageUtils().sendMessage(event, "```diff\n- [ERROR] This command requires " +
                    requiredArgs.length + " " + (requiredArgs.length == 1 ? "argument" : "arguments") + ".\n\nExample: !" + this.getName() + " " + this.getArguments() + "```");
            return;
        }
        if (!this.landing.getMethods().containsValue(bestOfValues, items[0].substring("bo".length()))
                || items[0].length() > 3) {
            this.landing.getMessageUtils().sendMessage(event, "```diff\n- [ERROR] The first argument must be BO1, BO2, BO3 or BO5.```");
            return;
        }
        if (items[1].equalsIgnoreCase(items[2])) {
            this.landing.getMessageUtils().sendMessage(event, "```diff\n- [ERROR] The captains can't be the same person. ```");
            return;
        }
        if (FileUtils.configFileExists(event.getGuild().getId())) {
            if (Boolean.parseBoolean(FileUtils.getProperty(event.getGuild().getId(), "vetoInProgress"))) {
                this.landing.getMessageUtils().sendMessage(event, "```diff\n- [ERROR] A Veto is already in Progress```");
                return;
            }
        }

        System.out.println(Arrays.toString(items));

        FileUtils.writeProperty(event.getGuild().getId(), "vetoInProgress", String.valueOf(true));
        FileUtils.writeProperty(event.getGuild().getId(), "maps", Arrays.toString(Constants.ACTIVE_MAP_POOL));
        FileUtils.writeProperty(event.getGuild().getId(), "bestOf", items[0].substring("bo".length()));
        FileUtils.writeProperty(event.getGuild().getId(), "captainOne", items[1].replaceAll("!", ""));
        FileUtils.writeProperty(event.getGuild().getId(), "captainTwo", items[2].replaceAll("!", ""));
        FileUtils.writeProperty(event.getGuild().getId(), "lastTurn", items[1]);
        FileUtils.writeProperty(event.getGuild().getId(), "vetoIndex", String.valueOf(0));
        FileUtils.writeProperty(event.getGuild().getId(), "pickedMaps", String.valueOf((String[]) null));

        this.landing.getMessageUtils().sendMessage(event, this.landing.getMethods().getFileForVeto(true), new EmbedBuilder()
                .setTitle("Map Veto Has Started!")
                .setDescription("Type !veto [mapname] to ban any of the following maps:")
                .addField("Maps", FileUtils.getProperty(event.getGuild().getId(), "maps"), true)
                .addField("Turn", FileUtils.getProperty(event.getGuild().getId(), "captainTwo"), true)
                .setThumbnail("attachment://ban.png"));
    }
}
