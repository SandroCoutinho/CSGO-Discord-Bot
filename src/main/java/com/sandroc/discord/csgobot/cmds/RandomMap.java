package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.data.Constants;
import net.dv8tion.jda.core.Permission;

@CommandInfo(
        name = { "RandomMap" },
        description = "Selects a random map of the active duty pool"
)
@Author("SandroC")
public class RandomMap extends Command {
    private ILanding landing;

    public RandomMap(ILanding landing) {
        this.name = "randommap";
        this.cooldown = 30;
        this.cooldownScope = CooldownScope.GUILD;
        this.help = "Selects a random map of the active duty pool";
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = false;
        this.landing = landing;
    }

    @Override
    protected void execute(CommandEvent event) {
        String map = Constants.ACTIVE_MAP_POOL[(int) Math.floor(Math.random() * Constants.ACTIVE_MAP_POOL.length)];
        this.landing.getMessageUtils().sendMessage(event, this.landing.getMethods().getFileForMap(map), this.landing.getMethods().buildRandomMap(map));
    }
}
