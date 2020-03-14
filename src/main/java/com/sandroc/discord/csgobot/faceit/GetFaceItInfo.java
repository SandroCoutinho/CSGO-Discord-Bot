package com.sandroc.discord.csgobot.faceit;

import com.google.gson.Gson;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.data.Constants;
import com.sandroc.discord.csgobot.faceit.gson.Profile;

public class GetFaceItInfo {
    private ILanding landing;

    public GetFaceItInfo(ILanding landing) {
        this.landing = landing;
    }

    public Profile getProfiles(String steamId) {
        try {
            return new Gson().fromJson(this.landing.getMethods().readUrl(String.format(Constants.SEARCH_USER, steamId)), Profile.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
