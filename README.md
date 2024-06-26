# ModLogger
A server side Forge mod to log mods installed on clients connecting to the server.

## Download
Downloads are available on the [releases page](https://github.com/olly007opm/ModLogger/releases).

## Commands
- `/ml reload`: Reload the config file.
- `/ml whitelist <add|remove> <username>`: Add or remove a player from the whitelist.

## Config
The config file is located at `config/modlogger/config.json`. The following options are available:
- `bannedMods`: An array of mod IDs that are not allowed on the server.
- `defaultMods`: An array of mod IDs that are expected on the client.
- `ignoredMods`: An array of mod IDs that are ignored.
- `requiredMods`: An array of mod IDs that are required.
- `matchExactModName`: Whether to match the exact mod name when checking for banned mods. If this is set to false, any mod ID that contains the banned mod ID will be detected.
- `commandPermissionLevel`: The permission level required to run ModLogger commands. Set to -1 to disable commands.
- `bypassKickPermissionLevel`: The permission level required to bypass kicks. Set to -1 to disable.
- `playerWhitelist`: An array of player usernames that are exempt from being kicked or banned.
- `webhook`: The configuration for Discord webhooks.
  - `discordWebhook`: The URL of the Discord webhook to send messages to. If this is not set, Discord messages will not be sent.
  - `onBanned`: Whether to send a Discord message when a banned mod is detected.
  - `onAdded`: Whether to send a Discord message when a mod is detected that is not in the default mods list.
  - `onDefault`: Whether to send a Discord message when the mods list matches the default mods.
  - `onRequired`: Whether to send a Discord message when a required mod is missing.
- `kick`: The configuration for kicking players.
  - `onBanned`: Whether to kick players when a banned mod is detected.
  - `onAdded`: Whether to kick players when a mod is detected that is not in the default mods list.
  - `onRequired`: Whether to kick players when a required mod is missing.
  - `showBannedMods`: Whether to show the banned mods in the kick message.
  - `showAddedMods`: Whether to show the added mods in the kick message.
  - `showRequiredMods`: Whether to show the required mods in the kick message.
  - `bannedMessage`: The message to show when a player is kicked for using banned mods.
  - `addedMessage`: The message to show when a player is kicked for using additional mods.
  - `requiredMessage`: The message to show when a player is kicked for missing required mods.
  - `bannedMessageWithMods`: The message to show when a player is kicked for using banned mods, with the list of banned mods.
  - `addedMessageWithMods`: The message to show when a player is kicked for using additional mods, with the list of additional mods.
  - `requiredMessageWithMods`: The message to show when a player is kicked for missing required mods, with the list of required mods.
- `ban`: The configuration for banning players.
  - `onBanned`: Whether to ban players when a banned mod is detected.
  - `banMessage`: The message to show when a player is banned for using banned mods.

## Join Data
The mods list of every join attempt is logged under `config/modlogger/players/<uuid>.json`, where `<uuid>` is the UUID of the player.

## Credits
The client mod list detection mixin is based on ModBlacklist by pedruhb
https://github.com/pedruhb/ModBlacklist/tree/main
Licensed under the Creative Commons Zero v1.0 Universal
