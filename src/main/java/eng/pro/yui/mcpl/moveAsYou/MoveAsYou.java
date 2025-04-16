package eng.pro.yui.mcpl.moveAsYou;

import eng.pro.yui.mcpl.moveAsYou.config.MoveAsYouConfig;
import eng.pro.yui.mcpl.moveAsYou.web.WebServer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MoveAsYou extends JavaPlugin {

    // fields
    private static MoveAsYou plugin;
    public static MoveAsYou plugin(){
        return plugin;
    }
    
    public static Logger log(){
        return plugin.getLogger();
    }
    
    private static WebServer webServer;
    public static WebServer webServer(){
        return webServer;
    }

    // constructor
    public MoveAsYou(){
        super();
    }

    //methods
    @Override
    public void onLoad(){
        super.onLoad();
        plugin = this;
        createDataFolder();
        generateDefaultConfig();
        loadConfig();
        outputCopyright();
    }
    private void createDataFolder(){
        //noinspection PointlessBooleanExpression
        if(getDataFolder().exists() == false){
            if(getDataFolder().mkdir()) {
                this.getLogger().info("created data folder");
            }else{
                this.getLogger().warning("failed to create data folder");
            }
        }
    }
    private void generateDefaultConfig(){
        this.saveDefaultConfig();
    }
    private void loadConfig(){
        MoveAsYouConfig.load();
    }
    private void outputCopyright() {
        this.getLogger().info("=== MoveAsYou copyright ===");
        this.getLogger().info("= under Apache License 2.0  =");
        this.getLogger().info("=== (c)Yui-KITAMURA 2025 ===");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.getLogger().info("MoveAsYou is enabled!");
        addCommandHandler();
        startUpWebServer();
    }
    private void addCommandHandler(){
//        super.getCommand(cmdName).setExecutor(new CommandHandler());
//        super.getCommand(cmdName).setTabCompleter(new CmdMngTabCompleter());
    }
    private void startUpWebServer(){
        webServer = WebServer.create(MoveAsYouConfig.webPort);
        webServer.start();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.getLogger().info("MoveAsYou is disabled!");
    }
}
