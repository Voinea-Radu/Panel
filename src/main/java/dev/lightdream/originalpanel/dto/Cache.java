package dev.lightdream.originalpanel.dto;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Cache {

    public long updateDelay;
    public Object value;
    public Consumer<Cache> updater;

    public Cache(Consumer<Cache> updater, long updateDelay) {
        this.updater = updater;
        this.updateDelay = updateDelay;
        registerUpdater();
        update();
    }

    public void update() {
        this.updater.accept(this);
    }

    public void update(Object value) {
        this.value = value;
    }

    public void registerUpdater() {
        Timer timer = new Timer("Update timer");

        timer.schedule(new TimerTask() {
            public void run() {
                update();
            }
        }, updateDelay);
    }


    public Object get(){
        return value;
    }


}
