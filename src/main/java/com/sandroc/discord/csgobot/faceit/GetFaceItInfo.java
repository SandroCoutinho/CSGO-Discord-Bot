package com.sandroc.discord.csgobot.faceit;

import com.google.gson.Gson;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.data.Constants;
import com.sandroc.discord.csgobot.faceit.gson.Profile;

public class GetFaceItInfo {
    private static ILanding landing;

    public GetFaceItInfo(ILanding landing) {
        GetFaceItInfo.landing = landing;
    }

    public Profile getProfiles(String steamId) {
        try {
            String response = landing.getMethods().readUrl(String.format(Constants.SEARCH_USER, steamId));

            return new Gson().fromJson(response, Profile.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
