package io.github.md5sha256.chestshopdatabase.gui;

import io.github.md5sha256.chestshopdatabase.model.ShopAttribute;
import io.github.md5sha256.chestshopdatabase.util.SortDirection;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ShopAttributeMeta {

    private final ShopAttribute attribute;
    private SortDirection sortDirection;
    private int weight;

    public ShopAttributeMeta(@Nonnull ShopAttribute attribute) {
        this(attribute, SortDirection.ASCENDING, 0);
    }

    public ShopAttributeMeta(@Nonnull ShopAttributeMeta attribute) {
        this(attribute.attribute, attribute.sortDirection, attribute.weight);
    }

    public ShopAttributeMeta(@Nonnull ShopAttribute attribute,
                             @Nonnull SortDirection sortDirection,
                             int weight) {
        this.attribute = attribute;
        this.sortDirection = sortDirection;
        this.weight = weight;
    }

    public ShopAttribute attribute() {
        return attribute;
    }

    public SortDirection sortDirection() {
        return sortDirection;
    }

    public void sortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public int weight() {
        return weight;
    }

    public void weight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ShopAttributeMeta that)) return false;
        return weight == that.weight && attribute == that.attribute && sortDirection == that.sortDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attribute, sortDirection, weight);
    }

    @Override
    public String toString() {
        return "ShopAttributeMeta{" +
                "attribute=" + attribute +
                ", sortDirection=" + sortDirection +
                ", weight=" + weight +
                '}';
    }
}
