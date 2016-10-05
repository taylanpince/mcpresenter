package com.hipo.mcpresenter.command.sub;

import com.hipo.mcpresenter.Presentation;
import com.hipo.mcpresenter.command.mcpresenterSubCommand;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import com.hipo.mcpresenter.mcpresenterPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by taylan on 2016-10-04.
 */
public class SubCommandCreate extends mcpresenterSubCommand {
    @Override
    public String getSub() {
        return "create";
    }

    @Override
    public String getPermission() {
        return "mcpresenterPlugin.create";
    }

    @Override
    public String getDescription() {
        return "Creates a new presentation from the specified URL and presentation ID starting from the targeted block";
    }

    @Override
    public String getSyntax() {
        return "/mcpresenterPlugin create <presentation-id> <URL>";
    }

    /** Command "/mcpresenterPlugin create [PRESENTATION-ID] [URL]" **/
    @Override
    public void onCommand(CommandSender sender, String[] args, String prefix) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + prefix + "This command cannot be used from the console.");
            return;
        }

        Player player = (Player)sender;

        if(args.length < 3) {
            player.sendMessage(ChatColor.RED + prefix + "Error in command syntax. Try, \"" + getSyntax() + "\"");
            return;
        }

        String presentationID = args[1];

        if(presentationID.length() < 3 || presentationID.length() > 16) {
            player.sendMessage(ChatColor.RED + prefix + "Presentation ID must be between 3 - 16 characters");
            return;
        }

//        if(!StringUtils.isAlphanumeric(args[1].replace("_", "").replace("-", ""))) {
//            player.sendMessage(ChatColor.RED + prefix + "Map ID must be Alphanumeric");
//            return;
//        }

        if(mcpresenterPlugin.getPresentationByID(args[1]) != null) {
            player.sendMessage(ChatColor.RED + prefix + "A presentation with that ID already exists.");
            return;
        }

        URL url;

        try {
            url = new URL(args[2]);
        }
        catch (MalformedURLException ex) {
            player.sendMessage(ChatColor.RED + prefix + "Unable to load image. URL appears to be invalid");
            return;
        }

        Set<Material> airMaterials = null;

        Block targetBlock = player.getTargetBlock(airMaterials, 40);

        if(targetBlock.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + prefix + "Could not find target block. Ensure you are looking"
                    + " at a wall before using this command.");
            return;
        }

        try {
            player.sendMessage(ChatColor.AQUA + prefix + "Downloading Image");

            BufferedImage image = ImageIO.read(url);

            player.sendMessage(ChatColor.AQUA + prefix + "Processing Image");

            String ext = url.getFile().substring(url.getFile().length() - 3);

            if(!ext.equalsIgnoreCase("png")) {
                player.sendMessage(ChatColor.RED + prefix + "Sorry, Only PNG files are supported at the moment");
                return;
            }

            Presentation presentation = new Presentation(image, presentationID, targetBlock);

            presentation.save();

            player.sendMessage(ChatColor.GREEN + prefix + "Presentation \"" + ChatColor.GOLD
                    + args[1] + ChatColor.GREEN + "\" created!");

        } catch (IOException e) {
            player.sendMessage(ChatColor.RED + prefix + "Unable to load image at URL");
            return;
        }
    }
}