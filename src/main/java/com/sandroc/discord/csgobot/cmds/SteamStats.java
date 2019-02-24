package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.sandroc.discord.csgobot.utils.MessageUtils;
import com.sandroc.discord.csgobot.utils.Methods;
import net.dv8tion.jda.core.Permission;

@CommandInfo(
        name = { "CSGO Stats" },
        description = "grabs csgo data from steam"
)
@Author("SandroC")
public class SteamStats extends Command {

    public SteamStats() {
        this.name = "csgostats";
        this.help = "grabs csgo data from steam";
        this.cooldown = 60;
        this.cooldownScope = CooldownScope.GUILD;
        this.arguments = "<steamProfileId/steamProfileName>";
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = false;
    }

    @Override
    public void execute(CommandEvent event) {
        MessageUtils.sendMessage(event, Methods.buildSteamInfo(event, event.getArgs().split("\\s+")[0]));
    }
}
