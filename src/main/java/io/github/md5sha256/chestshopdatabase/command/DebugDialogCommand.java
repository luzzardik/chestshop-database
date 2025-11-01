package io.github.md5sha256.chestshopdatabase.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.md5sha256.chestshopdatabase.database.FindTaskFactory;
import io.github.md5sha256.chestshopdatabase.gui.FindState;
import io.github.md5sha256.chestshopdatabase.gui.ShopComparators;
import io.github.md5sha256.chestshopdatabase.gui.ShopResultsGUI;
import io.github.md5sha256.chestshopdatabase.gui.dialog.FindDialog;
import io.github.md5sha256.chestshopdatabase.model.ChestshopItem;
import io.github.md5sha256.chestshopdatabase.util.BlockPosition;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.dialog.Dialog;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public record DebugDialogCommand(
        @Nonnull FindTaskFactory taskFactory,
        @Nonnull ShopResultsGUI gui) implements CommandBean.Single {

    @Override
    public @NotNull LiteralArgumentBuilder<CommandSourceStack> command() {
        return Commands.literal("debugdialog")
                .requires(sourceStack -> sourceStack.getSender() instanceof Player)
                .executes(ctx -> {
                    if (!(ctx.getSource().getSender() instanceof Player player)) {
                        return Command.SINGLE_SUCCESS;
                    }
                    var pos = player.getLocation().toBlock();
                    BlockPosition blockPos = new BlockPosition(player.getWorld().getUID(),
                            pos.blockX(),
                            pos.blockY(),
                            pos.blockX());
                    FindState findState = new FindState(
                            new ChestshopItem(ItemStack.of(Material.DIAMOND), "Diamond"),
                            new ShopComparators().withDistance(blockPos).build()
                    );
                    Dialog dialog = FindDialog.createMainPageDialog(findState, taskFactory, gui);
                    player.showDialog(dialog);
                    return Command.SINGLE_SUCCESS;
                });
    }
}
