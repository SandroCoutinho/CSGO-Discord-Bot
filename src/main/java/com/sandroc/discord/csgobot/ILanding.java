package com.sandroc.discord.csgobot;

import com.sandroc.discord.csgobot.hltv.GetHLTVInfo;
import com.sandroc.discord.csgobot.steam.GetSteamInfo;
import com.sandroc.discord.csgobot.utils.MessageUtils;
import com.sandroc.discord.csgobot.utils.Methods;

public interface ILanding {

    Methods getMethods();
    MessageUtils getMessageUtils();
    GetSteamInfo getSteamInfo();
    GetHLTVInfo getHLTVInfo();
}
