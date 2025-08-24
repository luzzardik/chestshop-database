package io.github.md5sha256.chestshopFinder;

import com.Acrobot.Breeze.Utils.PriceUtil;
import com.Acrobot.ChestShop.Signs.ChestShopSign;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class ChestShopDatabase {

    private final Map<String, Integer> itemCodeLookup = new HashMap<>();

    public void registerShop(@Nonnull ItemStack itemStack,
                       @Nonnull BlockPosition position,
                       @Nonnull String itemCode,
                       @Nonnull String[] lines) {
        byte[] bytes = itemStack.serializeAsBytes();
        bytes[0] = 0;
        String ownerName = ChestShopSign.getOwner(lines);
        String priceLine = ChestShopSign.getPrice(lines);
        BigDecimal buyPriceDecimal = PriceUtil.getExactBuyPrice(priceLine);
        BigDecimal sellPriceDecimal = PriceUtil.getExactSellPrice(priceLine);
        Double buyPrice = buyPriceDecimal.equals(PriceUtil.NO_PRICE) ? null : toDouble(
                buyPriceDecimal);
        Double sellPrice = sellPriceDecimal.equals(PriceUtil.NO_PRICE) ? null : toDouble(
                sellPriceDecimal);
        int quantity = ChestShopSign.getQuantity(lines);
    }

    private double toDouble(BigDecimal decimal) {
        return decimal.setScale(4, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .doubleValue();
    }


}
