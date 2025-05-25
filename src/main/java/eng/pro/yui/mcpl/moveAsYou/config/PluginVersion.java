package eng.pro.yui.mcpl.moveAsYou.config;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;
import eng.pro.yui.mcpl.moveAsYou.consts.Consts;
import eng.pro.yui.mcpl.moveAsYou.exception.MAYException;
import eng.pro.yui.mcpl.moveAsYou.exception.RuntimeMAYException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PluginVersion {
    
    public static final String CURRENT = MoveAsYou.plugin().getDescription().getVersion();
    
    public static String latest = CURRENT;
    
    private static long checkedTimestamp = 0L;
    
    private PluginVersion(){
        /* nothing to do */
    }
    /** 
     * GitHub上のVersionファイルを同期的に読み出して最新情報を取得する.
     * Exceptionはloggingされて吸収する */
    public static void read() {
        try {
            read(false);
        }catch(Exception e) {
            MoveAsYou.log().throwing(PluginVersion.class.getName(), "read", e);
        }
    }
    /**
     * {@link PluginVersion#read()} 参照.
     * ただし、Exceptionは呼び元に波及する.
     */
    public static void readForce() throws RuntimeMAYException {
        read(true);
    }
    private static void read(boolean force){
        if(force == false && System.currentTimeMillis() - checkedTimestamp < 60 * 60 * 1000) { //1h
            MoveAsYou.log().info("checking version is skipped");
            return;
        }
        
        final String url = "https://raw.githubusercontent.com/yui-Kitamura/MoveAsYou/refs/heads/main/bukkit/version.txt";
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setRequestMethod("GET");
            
            con.setConnectTimeout(3000); //ms
            con.setReadTimeout(5000); //ms
            
            MoveAsYou.log().info("checking version. requesting to raw.GitHubUserContent.com --->");
            int resCode = con.getResponseCode();
            MoveAsYou.log().info("version info request has returned");
            if(resCode != 200) {
                throw new IOException("GitHub raw content returned " + resCode);
            }
            try(InputStream in = con.getInputStream();
                Scanner s = new Scanner(in, StandardCharsets.UTF_8)
                ) {
                if(s.hasNextLine()) {
                    latest = s.nextLine();
                    checkedTimestamp = System.currentTimeMillis();
                }else {
                    throw new IOException("read version file is empty");
                }
            }
            if(hasLatest()) {
                MoveAsYou.log().warning("plugin MoveAsYou has new version!");
            }else {
                MoveAsYou.log().info("plugin MoveAsYou is running with the latest version.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeMAYException("illegal url", e);
        } catch (SocketTimeoutException e) {
            throw new RuntimeMAYException("GitHub server does not answer", e);
        } catch (IOException e) {
            throw new RuntimeMAYException("unexpected exception during getting latest version", e);
        }
    }
    
    public static boolean hasLatest(){
        return !(CURRENT.equals(latest));
    }
}
