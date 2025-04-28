package eng.pro.yui.mcpl.moveAsYou;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eng.pro.yui.mcpl.moveAsYou.auth.WebViewTokenManager;
import eng.pro.yui.mcpl.moveAsYou.config.MoveAsYouConfig;
import eng.pro.yui.mcpl.moveAsYou.config.PlayerSettingManager;
import eng.pro.yui.mcpl.moveAsYou.mc.EventHandlers;
import eng.pro.yui.mcpl.moveAsYou.mc.MAYCommandHandler;
import eng.pro.yui.mcpl.moveAsYou.mc.PlayerMoveMonitor;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;
import eng.pro.yui.mcpl.moveAsYou.web.WebServer;
import eng.pro.yui.mcpl.moveAsYou.web.data.PlayerInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
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
    
    private static PlayerSettingManager playerSettingManager;
    public static PlayerSettingManager playerSettings(){
        return playerSettingManager;
    }
    
    private static WebViewTokenManager webViewTokenManager;
    public static WebViewTokenManager tokenManager(){
        return webViewTokenManager;
    }
    
    private static PlayerMoveMonitor playerMoveMonitor;
    public static PlayerMoveMonitor playerMonitor(){
        return playerMoveMonitor;
    }
    
    private static Gson gson;
    public static Gson gson(){
        return gson;
    }
    private static Gson compactGson;
    public static Gson compactGson(){
        return compactGson;
    }
    
    private static BukkitTask monitorTask;
    
    // constructor
    public MoveAsYou(){
        super();
    }

    //methods
    @Override
    public void onLoad(){
        super.onLoad();
        plugin = this;
        playerSettingManager = new PlayerSettingManager();
        gson = new GsonBuilder().serializeNulls().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        compactGson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        webViewTokenManager = new WebViewTokenManager();
        playerMoveMonitor = new PlayerMoveMonitor();
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
        addEventHandler();
        startUpWebServer();
        runMonitor();
    }
    private void addCommandHandler(){
        super.getCommand(MAYCommandHandler.COMMAND).setExecutor(new MAYCommandHandler());
//        super.getCommand(cmdName).setTabCompleter(new CmdMngTabCompleter());
    }
    private void addEventHandler(){
        this.getServer().getPluginManager().registerEvents(new EventHandlers(), this);
    }
    private void startUpWebServer(){
        WebServer.create(MoveAsYouConfig.webPort, MoveAsYouConfig.socketPort);
        WebServer.start();
    }
    private void runMonitor(){
        monitorTask = new BukkitRunnable() {
            @Override
            public void run() {
                playerMoveMonitor.monitorAll();
                WebServer.socketSendMonitor();
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        monitorTask.cancel();
        WebServer.stop();
        this.getLogger().info("MoveAsYou is disabled!");
    }
}
