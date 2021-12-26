package dev.lightdream.originalpanel;

import dev.lightdream.originalpanel.dto.SQLConfig;
import dev.lightdream.originalpanel.managers.CacheManager;
import dev.lightdream.originalpanel.managers.DatabaseManager;
import dev.lightdream.originalpanel.managers.FileManager;
import dev.lightdream.originalpanel.utils.Debugger;
import dev.lightdream.originalpanel.utils.Logger;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.File;

public class Main {

    public static Main instance;
    public SQLConfig sqlConfig;
    public FileManager fileManager;
    public DatabaseManager databaseManager;
    public CacheManager cacheManager;
    public RestEndPoints restEndPoints;
    public JDA bot;

    @SneakyThrows
    public Main() {
        Debugger.init(this);
        Logger.init(this);
        Main.instance = this;

        this.fileManager = new FileManager(this, FileManager.PersistType.YAML);
        loadConfigs();
        this.databaseManager = new DatabaseManager(this);
        this.cacheManager = new CacheManager(this);
        this.restEndPoints = new RestEndPoints();
        this.bot = JDABuilder.createDefault("OTAyNTgxODA2NTE4MzcwMzE0.YXggzw.tllpHKmKFul4mYgDG7Ihmv84mxk").build();
    }

    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
    }

    public boolean debug() {
        return false; //todo configure
    }

    public void log(String log) {
        System.out.println(log);
    }

    public File getDataFolder() {
        return new File(System.getProperty("user.dir"));
    }
}
