package com.sandroc.discord.csgobot.cmds;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import com.jagrosh.jdautilities.examples.doc.Author;
import com.sandroc.discord.csgobot.data.BestOf;
import com.sandroc.discord.csgobot.data.Constants;
import com.sandroc.discord.csgobot.utils.FileUtils;
import com.sandroc.discord.csgobot.utils.MessageUtils;
import com.sandroc.discord.csgobot.utils.Methods;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;

import java.util.*;

@CommandInfo(
        name = { "Veto" },
        description = "Pick/Ban a map"
)
@Author("SandroC")
public class Veto extends Command {

    public Veto() {
        this.name = "veto";
        this.help = "pick/ban a map";
        this.arguments = "<mapName>";
        this.botPermissions = new Permission[]{ Permission.MESSAGE_WRITE };
        this.guildOnly = true;
    }

    @Override
    public void execute(CommandEvent event) {
        try {
            String[] mapArray    = FileUtils.getProperty(event.getGuild().getId(), "maps").split(", ");
            String[] pickedArray = FileUtils.getProperty(event.getGuild().getId(), "pickedMaps").split(", ");
            String[] items       = event.getArgs().split("\\s+");

            String selected = items[0].toLowerCase();

            if (!Boolean.parseBoolean(FileUtils.getProperty(event.getGuild().getId(), "vetoInProgress"))) {
                MessageUtils.sendMessage(event, "There's currently no VETO in progress.");
                return;
            }
            if (!event.getMessage().getAuthor().getAsMention().equals(FileUtils.getProperty(event.getGuild().getId(), "captainOne"))
                    && !event.getMessage().getAuthor().getAsMention().equals((FileUtils.getProperty(event.getGuild().getId(), "captainTwo")))) {
                MessageUtils.sendMessage(event, "Only the team captains can perform the vetoes.");
                return;
            }

            if (FileUtils.getProperty(event.getGuild().getId(), "lastTurn").equalsIgnoreCase(event.getMessage().getAuthor().getAsMention())) {
                MessageUtils.sendMessage(event, "It isn't your turn, let the other captain "
                        + (Objects.requireNonNull(BestOf.getBestOfByNumber(FileUtils.getProperty(event.getGuild().getId(), "bestOf")))[Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"))]
                        .equalsIgnoreCase("ban") ? "ban" : "pick")
                        + " a map.");
                return;
            }

            if (selected.equalsIgnoreCase("de_")
                    || selected.equalsIgnoreCase("de")
                    || selected.equalsIgnoreCase("d")) {
                MessageUtils.sendMessage(event, "Please type the map name not just de.");
                return;
            }

            if (selected.length() < 4) {
                MessageUtils.sendMessage(event, "The map name must have at least 4 letters/numbers.");
                return;
            }

            int count = 0;
            for (String map : mapArray) {
                if (map.contains(selected)) {
                    count++;
                }
            }

            if (count == 0) {
                MessageUtils.sendMessage(event, "Please select a map from the following list: \n" + Arrays.toString(mapArray));
                return;
            }

            FileUtils.changeProperty(event.getGuild().getId(), "lastVeto", String.valueOf(System.currentTimeMillis()));

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

                    MessageUtils.sendMessage(event, new EmbedBuilder()
                            .setTitle(Methods.capitalizeSentence(map.substring("de_".length())) + " has been "
                                    + (Objects.requireNonNull(BestOf.getBestOfByNumber(FileUtils.getProperty(event.getGuild().getId(), "bestOf")))[Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"))]
                                    .equalsIgnoreCase("ban") ? "banned" : "picked") + ".")
                            .setImage(Methods.getImageForMap(map)));

                    if (Objects.requireNonNull(BestOf.getBestOfByNumber(FileUtils.getProperty(event.getGuild().getId(), "bestOf")))[Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"))].equalsIgnoreCase("pick")) {
                        pickedMaps.add(Methods.capitalizeSentence(map.substring("de_".length())));
                        FileUtils.changeProperty(event.getGuild().getId(), "pickedMaps", String.valueOf(pickedMaps));
                        pickedMaps.clear();
                        pickedMaps.addAll(Arrays.asList(FileUtils.getProperty(event.getGuild().getId(), "pickedMaps").split(", ")));
                    }

                    int vetoIndex = Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"));

                    FileUtils.changeProperty(event.getGuild().getId(), "vetoIndex", String.valueOf(vetoIndex + 1));

                    if (maps.size() == 1
                            && pickedMaps.size() < Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "bestOf"))) {
                        pickedMaps.add(Methods.capitalizeSentence(FileUtils.getProperty(event.getGuild().getId(), "maps").substring("[de_".length())));
                        FileUtils.changeProperty(event.getGuild().getId(), "pickedMaps", String.valueOf(pickedMaps));
                        pickedMaps.clear();
                        pickedMaps.addAll(Arrays.asList(FileUtils.getProperty(event.getGuild().getId(), "pickedMaps").split(", ")));
                    }

                    if (Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "bestOf")) > pickedMaps.size()) {
                        String banOrPick = (Objects.requireNonNull(BestOf.getBestOfByNumber(FileUtils.getProperty(event.getGuild().getId(), "bestOf")))[Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "vetoIndex"))]
                                .equalsIgnoreCase("ban") ? "ban" : "pick");

                        MessageUtils.sendMessage(event, new EmbedBuilder()
                                .setThumbnail(banOrPick.equalsIgnoreCase("ban") ? Constants.BAN_URL : Constants.PICK_URL)
                                .setTitle("Map Veto")
                                .setDescription("Type !veto [mapname] to " + banOrPick + " any of the following maps:")
                                .addField("Maps", String.valueOf(maps), false)
                                .addField("Turn", (FileUtils.getProperty(event.getGuild().getId(), "lastTurn")
                                        .equalsIgnoreCase(FileUtils.getProperty(event.getGuild().getId(), "captainOne"))
                                        ? FileUtils.getProperty(event.getGuild().getId(), "captainTwo")
                                        : FileUtils.getProperty(event.getGuild().getId(), "captainOne")), false)
                        );
                    } else if (Integer.parseInt(FileUtils.getProperty(event.getGuild().getId(), "bestOf")) == pickedMaps.size()) {
                        MessageUtils.sendMessage(event, new EmbedBuilder()
                                .setTitle("Map Veto")
                                .setDescription("You will play on:")
                                .addField(pickedMaps.size() == 1 ? "Map" : "Maps", Arrays.toString(pickedMaps.toArray()), true)
                                .addField("Captains", FileUtils.getProperty(event.getGuild().getId(), "captainOne")
                                        + " " + FileUtils.getProperty(event.getGuild().getId(), "captainTwo"), true)
                                .setImage(Methods.getImageForMap(pickedMaps.get(0)))
                        );

                        FileUtils.changeProperty(event.getGuild().getId(), "vetoInProgress", String.valueOf(false));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
