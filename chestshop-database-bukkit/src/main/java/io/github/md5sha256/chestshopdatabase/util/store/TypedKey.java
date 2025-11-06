package io.github.md5sha256.chestshopdatabase.util.store;

import net.kyori.adventure.key.Key;

import javax.annotation.Nonnull;

public record TypedKey<T>(@Nonnull Class<T> type, @Nonnull Key key) {


}
