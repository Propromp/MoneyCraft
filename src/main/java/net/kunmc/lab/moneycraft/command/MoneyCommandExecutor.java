package net.kunmc.lab.moneycraft.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * コマンドを所定のメソッドへ振り分ける、コマンドの管理のためのクラス
 */
public class MoneyCommandExecutor implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender,  Command command,  String label,  String[] args) {
        if(args.length < 1){
            return false;
        }
        if(sender instanceof Player){
            Player player = (Player)sender;
            try {
                MoneyCommand.class.getMethod(args[0],Player.class,Command.class,String.class,String[].class).invoke(null,player,command,label,args);
            } catch (NoSuchMethodException e) {
                player.sendMessage("そのコマンドは存在しません。");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("このコマンドはプレイヤーしか実行できません。");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete( CommandSender sender,  Command command,  String alias,  String[] args) {
        List<String> list = new ArrayList<>();
        int length = args.length;
        switch(length){
            case 1:
                for(Method method: MoneyCommand.class.getMethods()){
                    if(!method.getDeclaringClass().equals(Object.class)){
                        list.add(method.getName());
                    }
                }
                break;
            default:
                for(Player player: Bukkit.getOnlinePlayers()){
                    list.add(player.getName());
                }
        }
        List<String> resList = new ArrayList<>();
        for(String str:list){
            if(str.startsWith(args[length-1])){
                resList.add(str);
            }
        }
        return resList;
    }
}
