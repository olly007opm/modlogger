package net.ollycodes.modlogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Webhook {
    private final String bannedMessage = ModLogger.fileHandler.getMessage("banned");
    private final String addedMessage = ModLogger.fileHandler.getMessage("added");
    private final String defaultMessage = ModLogger.fileHandler.getMessage("default");
    private final String requiredMessage = ModLogger.fileHandler.getMessage("required");

    public String formatModsList(List<String> mods, boolean escapeNewLine) {
        if (mods.isEmpty()) return "None";
        if (escapeNewLine) return "- " + String.join("\\n- ", mods);
        return "- " + String.join("\n- ", mods);
    }

    public String prepareMessage(
        String message, String uuid, String username, Date timestamp,
        List<String> defaultMods, List<String> ignoredMods
    ) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        return message
            .replace("<USERNAME>", username)
            .replace("<TIMESTAMP>", formatter.format(timestamp))
            .replace("<UUID>", uuid)
            .replace("<DEFAULT_COUNT>", String.valueOf(defaultMods.size()))
            .replace("<DEFAULT_TOTAL>", String.valueOf(ModLogger.fileHandler.config.defaultMods.size()))
            .replace("<REQUIRED_COUNT>", String.valueOf(ModLogger.fileHandler.config.requiredMods.size()))
            .replace("<REQUIRED_TOTAL>", String.valueOf(ModLogger.fileHandler.config.requiredMods.size()))
            .replace("<IGNORED_COUNT>", String.valueOf(ignoredMods.size()));
    }

    public void sendRequiredMessage(
        String uuid, String username, Date timestamp, boolean playerWhitelisted,
        List<String> defaultMods, List<String> ignoredMods, List<String> requiredMods
    ) {
        String action = ModLogger.fileHandler.config.kick.onRequired ? "Kicked" : "None";
        String message = requiredMessage.replace("<REQUIRED_COUNT>", String.valueOf(
                ModLogger.fileHandler.config.requiredMods.size() - requiredMods.size()
        ));
        message = prepareMessage(message, uuid, username, timestamp, defaultMods, ignoredMods)
                .replace("<ACTION>", playerWhitelisted ? "None (whitelisted)" : action)
                .replace("<REQUIRED_LIST>", formatModsList(requiredMods, true));
        sendWebhook(message);
    }

    public void sendBannedMessage(
        String uuid, String username, Date timestamp, boolean playerWhitelisted,
        List<String> defaultMods, List<String> ignoredMods, List<String> addedMods, List<String> bannedMods
    ) {
        String action = ModLogger.fileHandler.config.ban.onBanned ? "Banned"
                : ModLogger.fileHandler.config.kick.onBanned ? "Kicked" : "None";
        String message = prepareMessage(bannedMessage, uuid, username, timestamp, defaultMods, ignoredMods)
            .replace("<BANNED_COUNT>", String.valueOf(bannedMods.size()))
            .replace("<ADDED_COUNT>", String.valueOf(addedMods.size()))
            .replace("<BANNED_LIST>", formatModsList(bannedMods, true))
            .replace("<ADDED_LIST>", formatModsList(addedMods, true))
            .replace("<ACTION>", playerWhitelisted ? "None (whitelisted)" : action);
        sendWebhook(message);
    }

    public void sendAddedMessage(
        String uuid, String username, Date timestamp, boolean playerWhitelisted,
        List<String> defaultMods, List<String> ignoredMods, List<String> addedMods
    ) {
        String action = ModLogger.fileHandler.config.kick.onAdded ? "Kicked" : "None";
        String message = prepareMessage(addedMessage, uuid, username, timestamp, defaultMods, ignoredMods)
            .replace("<ADDED_COUNT>", String.valueOf(addedMods.size()))
            .replace("<ADDED_LIST>", formatModsList(addedMods, true))
            .replace("<ACTION>", playerWhitelisted ? "None (whitelisted)" : action);
        sendWebhook(message);
    }

    public void sendDefaultMessage(
        String uuid, String username, Date timestamp,
        List<String> defaultMods, List<String> ignoredMods
    ) {
        String message = prepareMessage(defaultMessage, uuid, username, timestamp, defaultMods, ignoredMods);
        sendWebhook(message);
    }

    public void sendWebhook(String message) {
        if (ModLogger.fileHandler.config.webhook.discordWebhook.isEmpty()) {
            ModLogger.logger.warn("No webhook configured");
            return;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ModLogger.fileHandler.config.webhook.discordWebhook))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
            ModLogger.logger.debug("Sent message to webhook");
        } catch (IOException | InterruptedException e) {
            ModLogger.logger.error("Failed to send webhook", e);
        }
    }
}
