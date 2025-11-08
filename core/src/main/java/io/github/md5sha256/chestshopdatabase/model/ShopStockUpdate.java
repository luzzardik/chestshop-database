package io.github.md5sha256.chestshopdatabase.model;

import java.util.UUID;

public record ShopStockUpdate(UUID worldUUID, int x, int y, int z, int stock, int estimatedCapacity) {}
