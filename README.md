# CSGO Bot - A discord bot

A Java bot for [Discord](https://discordapp.com/) using the [JDA library](https://github.com/DV8FromTheWorld/JDA) and [JDA-Utilities](https://github.com/JDA-Applications/JDA-Utilities).

## What can it do?

* CSGO Map Veto
* Coinflip
* Random Map
* CSGO Stats

## Run the bot yourself

* [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [git](https://git-scm.com/)
* [Maven](https://maven.apache.org/)

1. Clone the project with git

    ```
    cd /path/to/your/project/folder
    git clone https://github.com/SandroCoutinho/CSGO-Discord-Bot.git
    ``` 
    
2. Build
    
    Type `mvn install` in the `CSGO-Discord-Bot` folder.
    In the targets/ folder there should be a file called `csgobot-{VERSION}.jar` (where version is the latest version number).
    Move this file over to a location wherever you want to start the bot from.    
        
3. Run

    You can launch the bot with the following command:
    `java -jar csgobot-{VERSION}.jar`
    On first launch, It will generate a `default.settings` file and exit.<br>
    You'll have to edit the config file and add in your token, Steam API key, etc.


## Usage

On the first run it will generate a config file and stop running. You'll need to at least set the token and the property **botEnabled** to true

## Commands

Commands are prefixed with a "!".
For a list of commands in discord the **help** command can be used.

Current list of all available commands.

Commands | |
--- | --- |
[Coinflip](#Coinflip) | [Map Veto](#Map-Veto)
[Veto](#Veto)  |[Stop Veto](#Stop-Veto) 
[Random Map](#Random-Map) | [CSGO Stats](#CSGO-Stats)

### Coinflip

CT/T Side Selector

Aliases: coinflip

Usable in public and private channels

### Map Veto

Starts the CSGO Map Veto System

Aliases: start

Usable in public channels

#### Usage

```php
start <boX> <captainOne> <captainTwo>
```

### Veto

Pick/Ban a map if a veto is in progress

Aliases: veto

Usable in public channels

#### Usage

```php
start <mapName>
```

### Stop Veto

Stops the current veto

Aliases: stopveto

Usable in public channels

### Random Map

Selects a random map of the active duty pool

Aliases: randommap

Usable in public and private channels

### CSGO Stats

Grabs CSGO data from [Steam](https://steampowered.com/)

Aliases: csgodata

Usable in public and private channels

```php
csgodata <steamProfileName>
```

## Global configuration

The global configuration is stored in the default.settings file, which is generated the first time you run the application

The following settings can be set globally:

Setting name | default | description
---|---|---
BOT_ENABLED | false | Enables the bot<br/> This must be set to true in order to run the bot
BOT_TOKEN | "BOT_TOKEN" | Token used to login to discord
STEAM_API_KEY | "STEAM_API_KEY" | [Steam API Key](https://steamcommunity.com/dev/apikey) to access CSGO Data
