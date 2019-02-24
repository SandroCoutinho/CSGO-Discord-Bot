package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.sandroc.discord.csgobot.utils.MessageUtils;
import com.sandroc.discord.csgobot.utils.Methods;
import net.dv8tion.jda.core.Permission;

@CommandInfo(
        name = { "RandomMap" },
        description = "Selects a random map of the active duty pool"
)
@Author("SandroC")
public class RandomMap extends Command {

    public RandomMap() {
        this.name = "randommap";
        this.cooldown = 30;
        this.cooldownScope = CooldownScope.GUILD;
        this.help = "Selects a random map of the active duty pool";
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        MessageUtils.sendMessage(event, Methods.buildRandomMap());

        event.reactSuccess();
    }
}
