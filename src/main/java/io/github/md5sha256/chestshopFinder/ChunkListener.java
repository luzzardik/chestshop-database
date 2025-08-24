package io.github.md5sha256.chestshopFinder;

import com.Acrobot.ChestShop.Signs.ChestShopSign;
import org.bukkit.Server;
import org.bukkit.Tag;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.logging.Logger;

public class ChunkListener implements Listener {

    private final Logger logger;
    private final Server server;
    private final ItemDiscoverer discoverer;
    private final ChestShopDatabase database;

    public ChunkListener(@Nonnull Logger logger,
                         @Nonnull Server server,
                         @Nonnull ItemDiscoverer discoverer,
                         @Nonnull ChestShopDatabase database) {
        this.logger = logger;
        this.server = server;
        this.discoverer = discoverer;
        this.database = database;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onChunkLoad(ChunkLoadEvent event) {
        UUID world = event.getWorld().getUID();
        var tileEntities = event.getChunk()
                .getTileEntities(b -> Tag.SIGNS.isTagged(b.getType()), false);
        for (BlockState state : tileEntities) {
            if (!(state instanceof Sign sign)) {
                return;
            }
            String[] lines = sign.getLines();
            if (!ChestShopSign.isValid(lines)) {
                return;
            }
            BlockPosition position = new BlockPosition(world,
                    sign.getX(),
                    sign.getY(),
                    sign.getZ());
            String itemCode = ChestShopSign.getItem(lines);
            this.discoverer.discoverItemCode(itemCode,
                    item -> this.database.registerShop(item, position, itemCode, lines));
        }
    }

}
