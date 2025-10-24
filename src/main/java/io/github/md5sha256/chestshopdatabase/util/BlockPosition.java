package io.github.md5sha256.chestshopdatabase.util;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public record BlockPosition(@Nonnull UUID world, int x, int y, int z) {
    public BlockPosition {
        Objects.requireNonNull(world, "world cannot be null!");
    }
}
