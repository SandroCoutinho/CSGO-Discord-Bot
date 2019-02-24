package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.sandroc.discord.csgobot.utils.FileUtils;
import com.sandroc.discord.csgobot.utils.MessageUtils;
import net.dv8tion.jda.core.Permission;

@CommandInfo(
        name = { "StopVeto" },
        description = "Stops the current Map Veto"
)
@Author("SandroC")
public class StopVeto extends Command {

    public StopVeto() {
        this.name = "stopveto";
        this.help = "Stops the current veto";
        this.cooldown = 60;
        this.cooldownScope = CooldownScope.GUILD;
        this.userPermissions = new Permission[]{ Permission.KICK_MEMBERS };
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = true;
    }

    @Override
    public void execute(CommandEvent event) {
        if (!Boolean.parseBoolean(FileUtils.getProperty(event.getGuild().getId(), "vetoInProgress"))) {
            MessageUtils.sendMessage(event, "There's currently no VETO in progress.");
            return;
        }

        FileUtils.changeProperty(event.getGuild().getId(), "vetoInProgress", String.valueOf(false));
        MessageUtils.sendMessage(event, "Stopped the current veto, requested by " + event.getAuthor().getAsMention() + ".");
    }
}
