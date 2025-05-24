package eng.pro.yui.mcpl.moveAsYou.consts;

import org.bukkit.ChatColor;

public final class Consts {
    private Consts(){ /* インスタンス化禁止 */ }

    /** Minecraftチャット欄の改行文字 */
    public static final String br = "\n";
    
    /** MoveAsYou装飾テキスト */
    public static final String MAYDecorated =
            "" + ChatColor.RESET + ChatColor.ITALIC +
            ChatColor.BLUE + "Move" + ChatColor.RED + "As" + ChatColor.GREEN + "You" + 
            ChatColor.RESET;
}
