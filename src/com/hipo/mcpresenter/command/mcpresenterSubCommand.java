package com.hipo.mcpresenter.command;

import com.hipo.mcpresenter.command.sub.SubCommandCreate;

import com.hipo.mcpresenter.command.sub.SubCommandRender;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taylan on 2016-10-04.
 */
public abstract class mcpresenterSubCommand {

    private static List<mcpresenterSubCommand> commands;

    public abstract String getSub();
    public abstract String getPermission();
    public abstract String getDescription();
    public abstract String getSyntax();

    public abstract void onCommand(CommandSender sender,
                                   String[] args, String prefix );

    public static void loadCommands() {
        commands = new ArrayList<mcpresenterSubCommand>();

        commands.add(new SubCommandCreate());
        commands.add(new SubCommandRender());

    }

    public static List<mcpresenterSubCommand> getCommands() {
        return commands;
    }
}
