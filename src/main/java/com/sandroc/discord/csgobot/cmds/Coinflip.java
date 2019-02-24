package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.sandroc.discord.csgobot.utils.MessageUtils;
import com.sandroc.discord.csgobot.utils.Methods;
import net.dv8tion.jda.core.Permission;

@CommandInfo(
        name = { "Coinflip" },
        description = "CT/T Side Selector"
)
@Author("SandroC")
public class Coinflip extends Command {

    public Coinflip() {
        this.name = "coinflip";
        this.help = "CT/T Side Selector";
        this.cooldown = 30;
        this.cooldownScope = CooldownScope.GUILD;
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        MessageUtils.sendMessage(event, Methods.buildCoinflip());
    }
}
