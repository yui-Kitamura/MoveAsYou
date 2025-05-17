package eng.pro.yui.mcpl.moveAsYou.web.data;

import eng.pro.yui.mcpl.moveAsYou.MoveAsYou;

public class ConfigUpdateInfo {
    boolean hasNewSettings;
    
    public ConfigUpdateInfo(){
        this.hasNewSettings = true;
    }

    public String toJsonString(){
        return MoveAsYou.gson().toJson(this);
    }
}
