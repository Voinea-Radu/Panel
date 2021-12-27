package dev.lightdream.originalpanel;

import dev.lightdream.databasehandler.DatabaseMain;
import dev.lightdream.databasehandler.database.IDatabaseManager;
import dev.lightdream.databasehandler.dto.SQLConfig;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.originalpanel.managers.CacheManager;
import dev.lightdream.originalpanel.managers.DatabaseManager;
import dev.lightdream.originalpanel.managers.FileManager;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import java.io.File;

public class Main implements DatabaseMain, LoggableMain {

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
        Logger.good("Application started");
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

    @Override
    public dev.lightdream.databasehandler.dto.SQLConfig getSqlConfig() {
        return sqlConfig;
    }

    @Override
    public IDatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
