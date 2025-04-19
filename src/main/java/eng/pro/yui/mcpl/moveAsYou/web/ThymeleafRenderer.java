package eng.pro.yui.mcpl.moveAsYou.web;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ThymeleafRenderer {
    
    // fields
    private final TemplateEngine templateEngine;
    
    // constructor
    public ThymeleafRenderer(){
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("web/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(true);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        
    }
    
    // methods

    /** 
     * @param template 拡張子無しファイル名
     * @param values key:valueのペアマップ
     * @return 置き換え済みのhtml
     */
    public String render(String template, Map<String, Object> values){
        Context context = new Context();
        context.setVariables(values);
        return templateEngine.process(template, context);
    }
}
