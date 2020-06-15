package fr.rushcubeland.rcbapi.tools.tablist;

import net.minecraft.server.v1_15_R1.ChatComponentText;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class Tablist {

    public void sendTabList(Player player){

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        Object header = new ChatComponentText("§6§nRushcubeland\n§f\n§aFais le plein d'émotions et de joie !\n§7===================");
        Object footer = new ChatComponentText("§7===================\n§aPour obtenir un grade achète en un sur:\n §6shop.rushcubeland.fr\n§ewww.rushcubeland.fr");

        try {

            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);
            a.set(packet, header);
            b.set(packet, footer);

        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e){
            e.printStackTrace();
        }
        (((CraftPlayer)player).getHandle()).playerConnection.sendPacket(packet);
    }

}
