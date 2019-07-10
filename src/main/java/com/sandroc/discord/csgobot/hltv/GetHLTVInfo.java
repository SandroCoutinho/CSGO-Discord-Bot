package com.sandroc.discord.csgobot.hltv;

import com.google.gson.Gson;
import com.sandroc.discord.csgobot.ILanding;
import com.sandroc.discord.csgobot.data.Constants;
import com.sandroc.discord.csgobot.hltv.gson.Match;
import com.sandroc.discord.csgobot.hltv.gson.News;

import java.io.IOException;

public class GetHLTVInfo {
    private ILanding landing;

    public GetHLTVInfo(ILanding landing) {
        this.landing = landing;
    }

    public Match[] getResults() {
        try {
            return new Gson().fromJson(this.landing.getMethods().readUrl(Constants.GET_RESULTS), Match[].class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Match getResultForTeam(String teamName) {
        Match[] matches = getResults();

        for (Match match : matches) {
            if (match.teamOne.name.equalsIgnoreCase(teamName)
                    || match.teamTwo.name.equalsIgnoreCase(teamName)) {
                return match;
            }
        }

        return null;
    }

    public News[] getLatestNews() {
        try {
            return new Gson().fromJson(this.landing.getMethods().readUrl(Constants.GET_NEWS), News[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public News getNewsByIndex(int index) {
        return getLatestNews()[index];
    }
}
