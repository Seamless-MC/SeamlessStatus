package io.github.droppinganvil;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;

public class SeamlessStatus extends JavaPlugin {
    public static JDA jda;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        try {
            jda = new JDABuilder(getConfig().getString("BotToken")).build();
        } catch (LoginException e) {e.printStackTrace(); getServer().getPluginManager().disablePlugin(this); return;}
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () ->
        {
            if (Bukkit.hasWhitelist()) {
                jda.getPresence().setPresence(Activity.playing(parse(getConfig().getString("Whitelisted", "Seamless Factions, Currently Whitelisted {PLAYERS}/{MAX}"))), true);
            } else {
                jda.getPresence().setPresence(Activity.playing(parse(getConfig().getString("Normal", "Seamless Factions, {PLAYERS} players online"))), true);
            }
        }, 0L, Integer.toUnsignedLong(getConfig().getInt("Refresh")));
    }
    @Override
    public void onDisable() {
        if (jda != null) {
            jda.getPresence().setPresence(Activity.watching(parse(getConfig().getString("ShuttingDown", "Server Offline"))), true);
            jda.shutdown();
        }
    }
    private String parse(String s) {
        String temp = s;
        if (temp.contains("{PLAYERS}")) {temp = temp.replace("{PLAYERS}", String.valueOf(Bukkit.getOnlinePlayers().size()));}
        if (temp.contains("{MAX}")) {temp = temp.replace("{MAX}", String.valueOf(Bukkit.getMaxPlayers()));}
        return temp;
    }
}
