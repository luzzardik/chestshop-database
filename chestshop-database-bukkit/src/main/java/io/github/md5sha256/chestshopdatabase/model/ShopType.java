package io.github.md5sha256.chestshopdatabase.model;

import javax.annotation.Nonnull;

public enum ShopType {
    BUY("Buy"),
    SELL("Sell"),
    BOTH("Buy & Sell");

    private final String displayName;

    ShopType(@Nonnull String displayName) {
        this.displayName = displayName;
    }

    @Nonnull
    public String displayName() {
        return this.displayName;
    }
}
