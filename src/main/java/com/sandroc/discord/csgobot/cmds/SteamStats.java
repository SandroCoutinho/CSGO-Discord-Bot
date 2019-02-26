package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.sandroc.discord.csgobot.ILanding;
import net.dv8tion.jda.core.Permission;

@CommandInfo(
        name = { "CSGO Stats" },
        description = "grabs csgo data from steam"
)
@Author("SandroC")
public class SteamStats extends Command {
    private ILanding landing;

    public SteamStats(ILanding landing) {
        this.name = "csgostats";
        this.help = "Grabs CSGO data from Steam";
        this.cooldown = 60;
        this.cooldownScope = CooldownScope.GUILD;
        this.arguments = "<steamProfileId/steamProfileName>";
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = false;
        this.landing = landing;
    }

    @Override
    public void execute(CommandEvent event) {
        System.out.println(landing);
        this.landing.getMessageUtils().sendMessage(event, this.landing.getMethods().buildSteamInfo(event, event.getArgs().split("\\s+")[0]));
    }
}
