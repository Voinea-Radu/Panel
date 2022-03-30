package dev.lightdream.originalpanel.dto;

import dev.lightdream.logger.Debugger;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Cache<T> {

    public long updatePeriod;
    public T value;
    public Consumer<Cache<T>> updater;

    public Cache(Consumer<Cache<T>> updater, long updatePeriod) {
        this.updater = updater;
        this.updatePeriod = updatePeriod;
        registerUpdater();
        update();
    }

    public void update() {
        Debugger.info("Initializing update");
        this.updater.accept(this);
    }

    public void update(T value) {
        Debugger.info("Updating cache to " + value);
        this.value = value;
    }

    public void registerUpdater() {
        Debugger.info("Registering updater with delay of " + updatePeriod);
        TimerTask task = new TimerTask() {
            public void run() {
                update();
            }
        };
        Timer timer = new Timer();

        timer.schedule(task, 0, updatePeriod);
    }

    public T get() {
        return value;
    }

}
