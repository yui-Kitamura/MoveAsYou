package eng.pro.yui.mcpl.moveAsYou.config;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import org.bukkit.configuration.file.FileConfiguration;

public final class MoveAsYouConfig {
    
    // config values
    
    /** WebServerの実行port */
    public static int webPort;
    
    
    // methods
    
    /** 
     * config.ymlから値を読み込みます。
     * 
     * 指定が無い場合既定値とします
     * */
    public static void load(){
        
        FileConfiguration configFile = MoveAsYou.plugin().getConfig();
        
        webPort = 38080;
        if(configFile.contains("webPort")){
            webPort = configFile.getInt("webPort");
        }
        
        MoveAsYou.log().info("Config file loaded. Values are below: -->");
        MoveAsYou.log().info("webPort: " + webPort);
        MoveAsYou.log().info("<-- config.");
    }
}
