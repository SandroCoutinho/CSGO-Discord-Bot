package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.data.BestOf;
import com.sandroc.discord.csgobot.utils.FileUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.*;

@CommandInfo(
        name = { "Veto" },
        description = "Pick/Ban a map"
)
@Author("SandroC")
public class Veto extends Command {
    private ILanding landing;

    public Veto(ILanding landing) {
        this.name = "veto";
        this.help = "pick/ban a map";
        this.arguments = "<mapName>";
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = true;

        this.landing = landing;
    }

    @Override
    public void execute(CommandEvent event) {
        try {
            String[] mapArray     = FileUtils.getProperty(event.getGuild().getId(), "maps").split(", ");
            String[] pickedArray  = FileUtils.getProperty(event.getGuild().getId(), "pickedMaps").split(", ");
            String[] items        = event.getArgs().split("\\s+");
            String[] requiredArgs = this.getArguments().split("\\s+");
            String   selected     = items[0].toLowerCase();

            if (!this.landing.getMethods().isMatchingNumberOfArgs(requiredArgs, items)) {
                this.landing.getMessageUtils().sendMessage(event, "This command requires " +
                        requiredArgs.length + " " + (requiredArgs.length == 1 ? "Argument" : "Arguments") + ".\nExample: !" + this.getName() + " " + this.getArguments());
                return;
            }

            if (!Boolean.parseBoolean(FileUtils.getProperty(event.getGuild().getId(), "vetoInProgress"))) {
                this.landing.getMessageUtils().sendMessage(event, "There's currently no VETO in progress.");
                return;
            }
            if (!event.getMessage().getAuthor().getAsMention().equals(FileUtils.getProperty(event.getGuild().getId(), "captainOne"))
                    && !event.getMessage().getAuthor().getAsMention().equals((FileUtils.getProperty(event.getGuild().getId(), "captainTwo")))) {
                this.landing.getMessageUtils().sendMessage(event, "Only the team captains can perform the vetoes.");
                return;
            }

            if (FileUtils.getProperty(event.getGuild().getId(), "lastTurn").equalsIgnoreCase(event.getMessage().getAuthor().getAsMention())) {
                this.landing.getMessageUtils().sendMessage(event, "It isn't your turn, let the other captain "
                        + (Objects.requireNonNull(BestOf.getBestOfByNumber(FileUtils.getProperty(event.getGuild().getId(), "bestOf")))[Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"))]
                        .equalsIgnoreCase("ban") ? "ban" : "pick")
                        + " a map.");
                return;
            }

            if (selected.equalsIgnoreCase("de_")
                    || selected.equalsIgnoreCase("de")
                    || selected.equalsIgnoreCase("d")) {
                this.landing.getMessageUtils().sendMessage(event, "Please type the map name not just de.");
                return;
            }

            if (selected.length() < 4) {
                this.landing.getMessageUtils().sendMessage(event, "The map name must have at least 4 letters/numbers.");
                return;
            }

            int count = 0;
            for (String map : mapArray) {
                if (map.contains(selected)) {
                    count++;
                }
            }

            if (count == 0) {
                this.landing.getMessageUtils().sendMessage(event, "Please select a map from the following list: \n" + Arrays.toString(mapArray));
                return;
            }

            ArrayList<String> maps       = new ArrayList<>(Arrays.asList(mapArray));
            List<String>      pickedMaps = new ArrayList<>(Arrays.asList(pickedArray));

            if (pickedMaps.get(0).equalsIgnoreCase("null")) {
                pickedMaps.removeAll(Arrays.asList(pickedArray));
            }

            Iterator<String> iter = maps.iterator();
            while (iter.hasNext()) {
                String map = iter.next();

                if (map.substring("de_".length()).startsWith(selected.startsWith("de_") ? selected.substring("de_".length()) : selected)) {
                    iter.remove();
                    FileUtils.changeProperty(event.getGuild().getId(), "maps", String.valueOf(maps));
                    maps.clear();
                    maps.addAll(Arrays.asList(FileUtils.getProperty(event.getGuild().getId(), "maps").replace("[", "").replace("]", "").split(", ")));

                    FileUtils.changeProperty(event.getGuild().getId(), "lastTurn", event.getAuthor().getAsMention());

                    this.landing.getMessageUtils().sendMessage(event, this.landing.getMethods().getFileForMap(map), new EmbedBuilder()
                            .setTitle(this.landing.getMethods().capitalizeSentence(map.substring("de_".length())) + " has been "
                                    + (Objects.requireNonNull(BestOf.getBestOfByNumber(FileUtils.getProperty(event.getGuild().getId(), "bestOf")))[Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"))]
                                    .equalsIgnoreCase("ban") ? "banned" : "picked") + ".")
                            .setImage("attachment://" + map.toLowerCase() + ".jpg"));

                    if (Objects.requireNonNull(BestOf.getBestOfByNumber(FileUtils.getProperty(event.getGuild().getId(), "bestOf")))[Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"))].equalsIgnoreCase("pick")) {
                        pickedMaps.add(this.landing.getMethods().capitalizeSentence(map.substring("de_".length())));
                        FileUtils.changeProperty(event.getGuild().getId(), "pickedMaps", String.valueOf(pickedMaps));
                        pickedMaps.clear();
                        pickedMaps.addAll(Arrays.asList(FileUtils.getProperty(event.getGuild().getId(), "pickedMaps").split(", ")));
                    }

                    int vetoIndex = Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"));

                    FileUtils.changeProperty(event.getGuild().getId(), "vetoIndex", String.valueOf(vetoIndex + 1));

                    if (maps.size() == 1
                            && pickedMaps.size() < Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "bestOf"))) {
                        pickedMaps.add(this.landing.getMethods().capitalizeSentence(FileUtils.getProperty(event.getGuild().getId(), "maps").substring("de_".length())));
                        FileUtils.changeProperty(event.getGuild().getId(), "pickedMaps", String.valueOf(pickedMaps));
                        pickedMaps.clear();
                        pickedMaps.addAll(Arrays.asList(FileUtils.getProperty(event.getGuild().getId(), "pickedMaps").split(", ")));
                    }
                    if (Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "bestOf")) > pickedMaps.size()) {
                        String banOrPick = (Objects.requireNonNull(BestOf.getBestOfByNumber(FileUtils.getProperty(event.getGuild().getId(), "bestOf")))[Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"))]
                                .equalsIgnoreCase("ban") ? "ban" : "pick");

                        this.landing.getMessageUtils().sendMessage(event, this.landing.getMethods().getFileForVeto(banOrPick.equalsIgnoreCase("ban")), new EmbedBuilder()
                                .setThumbnail("attachment://" + (banOrPick.equalsIgnoreCase("ban") ? "ban" : "pick") + ".png")
                                .setTitle("Map Veto")
                                .setDescription("Type !veto <mapName> to " + banOrPick + " any of the following maps:")
                                .addField("Maps", String.valueOf(maps), false)
                                .addField("Turn", (FileUtils.getProperty(event.getGuild().getId(), "lastTurn")
                                        .equalsIgnoreCase(FileUtils.getProperty(event.getGuild().getId(), "captainOne"))
                                        ? FileUtils.getProperty(event.getGuild().getId(), "captainTwo")
                                        : FileUtils.getProperty(event.getGuild().getId(), "captainOne")), false));

                    } else if (Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "bestOf")) == pickedMaps.size()) {
                        String mapName = "de_" + pickedMaps.get(0);

                        this.landing.getMessageUtils().sendMessage(event, this.landing.getMethods().getFileForMap(mapName), new EmbedBuilder()
                                .setTitle("Map Veto")
                                .setDescription("You will play on:")
                                .addField(pickedMaps.size() == 1 ? "Map" : "Maps", Arrays.toString(pickedMaps.toArray()), true)
                                .addField("Captains", FileUtils.getProperty(event.getGuild().getId(), "captainOne")
                                        + " " + FileUtils.getProperty(event.getGuild().getId(), "captainTwo"), true)
                                .setImage("attachment://" + mapName.toLowerCase() + ".jpg"));

                        FileUtils.changeProperty(event.getGuild().getId(), "vetoInProgress", String.valueOf(false));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
