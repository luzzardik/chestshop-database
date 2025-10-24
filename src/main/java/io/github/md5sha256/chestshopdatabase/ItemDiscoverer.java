package io.github.md5sha256.chestshopdatabase;

import com.Acrobot.ChestShop.Events.ItemParseEvent;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.md5sha256.chestshopdatabase.util.TickUtil;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ItemDiscoverer {

    private final TickUtil<String> itemCodes;
    private final Set<String> queuedItemCodes = new HashSet<>();
    private final Cache<String, ItemStack> cachedItemCodes;
    private final Map<String, List<Consumer<ItemStack>>> callbacks = new HashMap<>();
    private final Server server;

    public ItemDiscoverer(int bufferSize,
                          @Nonnull Duration cacheTime,
                          int cacheSize,
                          @Nonnull Server server) {
        this.server = server;
        this.itemCodes = new TickUtil<>(bufferSize, this::discoverItemCodes);
        this.cachedItemCodes = CacheBuilder.newBuilder()
                .expireAfterAccess(cacheTime)
                .initialCapacity(cacheSize)
                .build();
    }

    public void schedulePollTask(@Nonnull Plugin plugin,
                                 @Nonnull BukkitScheduler scheduler,
                                 int elementsPerTick,
                                 int intervalTicks) {
        this.itemCodes.schedulePollTask(plugin, scheduler, elementsPerTick, intervalTicks);
    }

    private void discoverItemCodes(@Nonnull List<String> itemCodes) {
        PluginManager pluginManager = this.server.getPluginManager();
        for (String code : itemCodes) {
            this.queuedItemCodes.remove(code);
            ItemParseEvent parseEvent = new ItemParseEvent(code);
            pluginManager.callEvent(parseEvent);
            ItemStack itemStack = parseEvent.getItem();
            this.cachedItemCodes.put(code, itemStack);
            triggerCallbacks(code, itemStack);
        }
    }

    private void triggerCallbacks(@Nonnull String code, @Nonnull ItemStack itemStack) {
        List<Consumer<ItemStack>> consumers = this.callbacks.remove(code);
        if (consumers != null) {
            consumers.forEach(consumer -> consumer.accept(itemStack));
        }
    }

    public void discoverItemCode(@Nonnull String code, @Nonnull Consumer<ItemStack> callback) {
        ItemStack cached = this.cachedItemCodes.getIfPresent(code);
        if (cached != null) {
            callback.accept(cached);
            return;
        }
        if (this.queuedItemCodes.add(code)) {
            this.itemCodes.queueElement(code);
        }
        this.callbacks.computeIfAbsent(code, unused -> new ArrayList<>()).add(callback);
    }

}
