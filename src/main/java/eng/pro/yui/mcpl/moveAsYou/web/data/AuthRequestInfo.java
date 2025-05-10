package eng.pro.yui.mcpl.moveAsYou.web.data;

import eng.pro.yui.mcpl.moveAsYou.auth.TokenText;
import eng.pro.yui.mcpl.moveAsYou.mc.data.PlayerName;

public class AuthRequestInfo {
    public TokenText token;
    public PlayerName playerName;
    
    public AuthRequestInfo(TokenText token, PlayerName playerName) {
        this.token = token;
        this.playerName = playerName;
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + token.hashCode();
        hash = hash * 31 + playerName.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (obj == null) { return false; }
        if (obj.getClass() != this.getClass()) { return false; }
        AuthRequestInfo other = (AuthRequestInfo) obj;
        if(!token.equals(other.token)){ return false; }
        if(!playerName.equals(other.playerName)){ return false; }
        return true;
    }

    @Override
    public String toString() {
        return "AuthRequestInfo{token=" + token.value() + ", playerName=" + playerName.value() + "}";
    }
}
