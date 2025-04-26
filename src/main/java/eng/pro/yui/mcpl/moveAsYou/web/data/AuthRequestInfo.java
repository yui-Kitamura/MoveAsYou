package eng.pro.yui.mcpl.moveAsYou.web.data;

import eng.pro.yui.mcpl.moveAsYou.auth.TokenText;

public class AuthRequestInfo {
    public TokenText token;
    public String playerName;
    
    public AuthRequestInfo(TokenText token, String playerName) {
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
        return token.equals(other.token) && playerName.equals(other.playerName);
    }

    @Override
    public String toString() {
        return "AuthRequestInfo{token=" + token + ", playerName=" + playerName + "}";
    }
}
