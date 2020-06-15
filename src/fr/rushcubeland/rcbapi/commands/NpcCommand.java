package fr.rushcubeland.rcbapi.commands;

import fr.rushcubeland.rcbapi.RcbAPI;
import fr.rushcubeland.rcbapi.tools.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NpcCommand implements CommandExecutor {


    public NpcCommand(RcbAPI rcbAPI) {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("createnpc")){
            if(!(sender instanceof Player)){
                return true;
            }
            Player player = (Player) sender;
            if(args.length == 0){
                NPC.createNPC(player, player.getName(), player.getName(), player.getLocation());
                return true;
            }
            if(args.length == 2){
                NPC.createNPC(player, args[1].replace('&', '§'), args[0], player.getLocation());
                return true;
            }
        }
        return false;
    }
}
