package eng.pro.yui.mcpl.moveAsYou.web.data;

import eng.pro.yui.mcpl.moveAsYou.config.MoveAsYouConfig;

public class HtmlText {

    private final String rawHtml;
    public String getRawHtml() {
        return rawHtml;
    }

    public HtmlText(String rawHtml) {
        if (rawHtml == null || rawHtml.isBlank()) {
            throw new IllegalArgumentException("HTML text cannot be null or empty.");
        }
        this.rawHtml = rawHtml;
    }
    
    public static HtmlText getFull(String rawHtml){
        return new HtmlText(rawHtml);
    }
    public static HtmlText get(String title, String body){
        return new HtmlText(
                "<!DOCTYPE html><html lang=\"ja\"><title>"+title+"|"+ MoveAsYouConfig.webTitle +"</title>" +
                "<body>"+body+"</body></html>");
    }
    public static HtmlText getWithBody(String body){
        return new HtmlText("<!DOCTYPE html><html lang=\"ja\"><title>"+ MoveAsYouConfig.webTitle +"</title>" +
                "<body>"+body+"</body></html>");
    }

    @Override
    public String toString() {
        return rawHtml;
    }
    
    public String toShortString(){
        String whiteRemoved = rawHtml.replaceAll("\\v", "");
        whiteRemoved = whiteRemoved.replaceAll("\\h\\h", " ");
        return  whiteRemoved.substring(0, Math.min(whiteRemoved.length(), 100));
    }

    @Override
    public int hashCode() {
        return rawHtml.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){ return true; }
        if(obj == null){ return false; }
        if(obj.getClass() != this.getClass()){ return false; }
        HtmlText other = (HtmlText) obj;
        return rawHtml.equals(other.rawHtml);
    }
}
