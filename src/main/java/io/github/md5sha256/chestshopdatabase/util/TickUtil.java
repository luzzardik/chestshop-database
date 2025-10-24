package io.github.md5sha256.chestshopdatabase.util;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class TickUtil<T> {

    private final ArrayDeque<T> queue;
    private final Consumer<List<T>> handler;
    private BukkitTask task;

    public TickUtil(int size, @Nonnull Consumer<List<T>> handler) {
        this.queue = new ArrayDeque<>(size);
        this.handler = handler;
    }

    public TickUtil(@Nonnull Consumer<List<T>> handler) {
        this.queue = new ArrayDeque<>();
        this.handler = handler;
    }

    public void queueElement(@Nonnull T element) {
        this.queue.addLast(element);
    }

    public void queueElements(@Nonnull Collection<T> elements) {
        this.queue.addAll(elements);
    }

    @Nonnull
    public List<T> pollElements(int numElements) {
        if (this.queue.isEmpty()) {
            return Collections.emptyList();
        }
        if (numElements >= this.queue.size()) {
            List<T> elements = List.copyOf(this.queue);
            this.queue.clear();
            return elements;
        }
        List<T> elements = new ArrayList<>(numElements);
        for (int i = 0; i < numElements; i++) {
            elements.add(this.queue.pollFirst());
        }
        return elements;
    }


    public void schedulePollTask(@Nonnull Plugin plugin,
                                 @Nonnull BukkitScheduler scheduler,
                                 int elementsPerTick,
                                 int intervalTicks) {
        if (this.task != null) {
            this.task.cancel();
        }
        this.task = scheduler.runTaskTimer(plugin,
                () -> {
                    List<T> elements = pollElements(elementsPerTick);
                    if (!elements.isEmpty()) {
                        this.handler.accept(elements);
                    }
                },
                intervalTicks,
                intervalTicks);
    }

    public void cancelPollTask() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

}
