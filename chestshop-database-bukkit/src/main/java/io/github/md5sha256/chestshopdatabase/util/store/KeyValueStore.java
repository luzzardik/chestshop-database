package io.github.md5sha256.chestshopdatabase.util.store;

import net.kyori.adventure.key.Key;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class KeyValueStore {

    private final Map<Class<?>, Values<?>> typeStore = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Optional<T> value(@Nonnull TypedKey<T> key) {
        Values<?> rawValues = this.typeStore.get(key.type());
        if (rawValues == null) {
            return Optional.empty();
        }
        Values<T> values = (Values<T>) rawValues;
        return values.value(key.key());
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrCreate(@Nonnull TypedKey<T> key, @Nonnull Supplier<T> supplier) {
        Values<T> values = (Values<T>) this.typeStore.computeIfAbsent(key.type(), Values::new);
        return values.getOrDefault(key.key(), supplier);
    }

    @SuppressWarnings("unchecked")
    public <T> void value(@Nonnull TypedKey<T> key, @Nonnull T value) {
        Values<T> values = (Values<T>) this.typeStore.computeIfAbsent(key.type(), Values::new);
        values.value(key.key(), value);
    }

    public void remove(@Nonnull TypedKey<?> key) {
        Values<?> rawValues = this.typeStore.get(key.type());
        if (rawValues == null) {
            return;
        }
        rawValues.remove(key.key());
    }

    public void clear() {
        this.typeStore.values().forEach(Values::clear);
    }

    private static class Values<T> {
        private final Map<Key, Object> valueStore = new HashMap<>();

        private final Class<T> type;

        public Values(@Nonnull Class<T> type) {
            this.type = type;
        }

        public Optional<T> value(@Nonnull Key key) {
            return Optional.ofNullable(this.valueStore.get(key)).map(this.type::cast);
        }

        public void value(@Nonnull Key key, @Nonnull T value) {
            this.valueStore.put(key, value);
        }

        public void remove(@Nonnull Key key) {
            this.valueStore.remove(key);
        }

        public void clear() {
            this.valueStore.clear();
        }

        @Nonnull
        public T getOrDefault(@Nonnull Key key, @Nonnull Supplier<T> value) {
            return this.type.cast(this.valueStore.computeIfAbsent(key, unused -> value.get()));
        }
    }

}
