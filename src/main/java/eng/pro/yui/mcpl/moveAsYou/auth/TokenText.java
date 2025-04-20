package eng.pro.yui.mcpl.moveAsYou.auth;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class TokenText {
    
    private static final String DELIMITER = "-";
    
    private final String value;
    public String value(){
        return value;
    }
    public String[] getParts(){
        return value.split(DELIMITER);
    }
    
    public TokenText(String value){
        if(isValid(value)){
            this.value = value;
        } else {
            throw new IllegalArgumentException("Invalid token text");
        }
    }
    
    @Contract(" -> new")
    public static @NotNull TokenText generate(){
        return new TokenText("test-token-that-valid"); //TODO use itemName list
    }
    
    public static boolean isValid(String applicant){
        if(applicant == null || applicant.isBlank()){
            return false;
        }

        String[] parts = applicant.split(DELIMITER);
        if (parts.length < 3 || 4 < parts.length) {
            return false;
        }
        for (String part : parts) {
            if (part.length() < 3 || 7 < part.length()) {
                return false;
            }
        }
        return true;
    }
    

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (getClass() != obj.getClass()) { return false; }
        TokenText other = (TokenText) obj;
        return value.equals(other.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
