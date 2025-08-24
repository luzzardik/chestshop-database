package io.github.md5sha256.chestshopFinder;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public record PartialShop(
        @Nonnull UUID worldId,
        int posX,
        int posY,
        int posZ,
        @Nonnull ItemStack itemStack,
        @Nonnull String[] lines
) {

}
