package com.sandroc.discord.csgobot.hltv.gson;

import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("title")
    public String title;
    @SerializedName("description")
    public String description;
    @SerializedName("link")
    public String link;
    @SerializedName("date")
    public String date;
}
