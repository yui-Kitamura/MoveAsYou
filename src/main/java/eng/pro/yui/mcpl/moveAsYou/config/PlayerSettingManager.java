package eng.pro.yui.mcpl.moveAsYou.config;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.web.WebServer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.UUID;

public class PlayerSettingManager {
    
    // fields
    private final HashMap<UUID, PlayerSetting> playerSettings;
    
    private final Path settingsPath = MoveAsYou.plugin().getDataFolder().toPath().resolve("players");
    
    // constructor
    public PlayerSettingManager(){
        if(settingsPath.toFile().exists() == false) {
            settingsPath.toFile().mkdirs();
        }
        playerSettings = new HashMap<>();
    }
    
    // methods

    /**
     * Load settings. If file not exists, then generate and load.
     * @param playerUUID
     */
    public void loadPlayerSetting(UUID playerUUID){
        if(playerSettings.containsKey(playerUUID)) {
            return;
        }
        File playerSettingFile = settingsPath.resolve(playerUUID.toString()).toFile();
        if(playerSettingFile.exists() == false) {
            generatePlayerSetting(playerUUID);
        }
        YamlConfiguration settingFile = YamlConfiguration.loadConfiguration(playerSettingFile);
        PlayerSetting playerSetting = new PlayerSetting(playerUUID, settingFile);
        playerSettings.put(playerUUID, playerSetting);
    }
    
    public void unloadPlayerSetting(UUID playerUUID){
        save(playerUUID);
        playerSettings.remove(playerUUID);
    }
    
    public void generatePlayerSetting(UUID playerUUID){
        updatePlayerSetting(playerUUID, new PlayerSetting(playerUUID));
    }
    
    // update value and save to file
    public void updatePlayerSetting(UUID playerUUID, PlayerSetting newSetting){
        playerSettings.put(playerUUID, newSetting); // set or replace
        save(playerUUID);
        WebServer.socketSendSettingsUpdateNotify(playerSettings.get(playerUUID).getPlayerName()); //フロントへ通知
    }
    
    public PlayerSetting get(UUID playerUUID) {
        if(playerSettings.containsKey(playerUUID) == false) {
            loadPlayerSetting(playerUUID);
        }
        return playerSettings.get(playerUUID);
    }
    
    public void save(UUID playerUUID){
        try {
            playerSettings.get(playerUUID).toFile().save(settingsPath.resolve(playerUUID.toString()).toFile());
        }catch(IOException ioe) {
            MoveAsYou.log().throwing(PlayerSettingManager.class.getName(), "save", ioe);
        }
    }
}
