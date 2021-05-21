package com.minecolonies.coremod.commands.colonycommands;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;

public class KNDeleteColonyCommand extends CommandBase {

    boolean confirmed = false;

    @Override
    public String getName() { return "deletecolony";}

    @Override
    public String getUsage(ICommandSender sender) { return "/deletecolony";}

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            IColony colony = IColonyManager.getInstance().getIColonyByOwner(sender.getEntityWorld(), player);
            if (colony != null) {
                if (!confirmed) {
                    confirmed = true;
                    final ITextComponent deleteButton = new TextComponentTranslation("tile.blockHutTownHall.deleteMessageLink")
                            .setStyle(new Style().setBold(true).setColor(TextFormatting.GOLD).setClickEvent(
                                    new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/deletecolony"
                                    )));
                    final ITextComponent deletionWarning = new TextComponentString("!!WARNING THIS WILL DESTROY BUILDING IN THE COLONY!!")
                            .setStyle(new Style().setBold(true).setColor(TextFormatting.RED));
                    final ITextComponent confirmToDelete = new TextComponentString("Click [DELETE] to confirm the deletion of colony: ")
                            .appendSibling(new TextComponentString(colony.getName())
                                    .setStyle(new Style().setColor(TextFormatting.GREEN)));

                    sender.sendMessage(deletionWarning);
                    sender.sendMessage(confirmToDelete);
                    sender.sendMessage(deleteButton);
                }
                else {
                    server.addScheduledTask(() -> IColonyManager.getInstance().deleteColonyByWorld(colony.getID(), true, sender.getEntityWorld()));
                    confirmed = false;
                }
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}
