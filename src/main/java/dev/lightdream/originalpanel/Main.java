package dev.lightdream.originalpanel;

import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.IDatabaseManager;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.originalpanel.dto.Config;
import dev.lightdream.originalpanel.dto.DriverConfig;
import dev.lightdream.originalpanel.dto.JDAConfig;
import dev.lightdream.originalpanel.managers.*;
import net.dv8tion.jda.api.JDA;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main implements DatabaseMain, LoggableMain, FileManagerMain, JDAExtensionMain {


    public static final boolean maintenance = false;

    // Static
    public static Main instance;
    public static List<String> maintenanceIP = new ArrayList<>();

    // Vars
    private final boolean enabled;

    // Config
    public SQLConfig sqlConfig;
    public Config config;
    public JDAConfig jdaConfig;
    public DriverConfig driverConfig;

    // Managers
    public FileManager fileManager;
    public DatabaseManager databaseManager;
    public CacheManager cacheManager;
    public DiscordManager discordManager;
    public NotificationManager notificationManager;

    // Spring
    public RestEndPoints restEndPoints;
    public RateLimiter rateLimiter;

    // Instances
    public JDA bot;

    public Main() {
        Debugger.init(this);
        Logger.init(this);
        Main.instance = this;

        Logger.good("Starting Panel version " + getVersion());

        this.fileManager = new FileManager(this);
        loadConfigs();
        this.databaseManager = new DatabaseManager();
        this.notificationManager = new NotificationManager();

        this.bot = JDAExtensionMain.generateBot(this);

        //LambdaExecutor.LambdaCatch.NoReturnLambdaCatch.executeCatch(() ->
        //        this.bot = JDABuilder.createDefault(config.botToken).build());

        this.discordManager = new DiscordManager();

        this.cacheManager = new CacheManager(this);
        this.restEndPoints = new RestEndPoints();
        this.rateLimiter = new RateLimiter();

        Logger.good("Application started");

        enabled = true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isEnabled() {
        return enabled;
    }

    public boolean maintenance(String ip) {
        if (maintenance) {
            return !maintenanceIP.contains(ip);
        }
        return maintenance;
    }

    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
        config = fileManager.load(Config.class);
        jdaConfig = fileManager.load(JDAConfig.class);
        driverConfig = fileManager.load(DriverConfig.class);
    }

    public boolean debug() {
        return config.debug;
    }

    public void log(String log) {
        System.out.println(log);
    }

    public File getDataFolder() {
        return new File(System.getProperty("user.dir") + "/config");
    }

    @Override
    public SQLConfig getSqlConfig() {
        return sqlConfig;
    }

    @Override
    public DriverConfig getDriverConfig() {
        return driverConfig;
    }

    @Override
    public IDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public String getVersion() {
        return "1.10";
    }

    @Override
    public JDA getBot() {
        return bot;
    }

    @Override
    public dev.lightdream.jdaextension.dto.JDAConfig getJDAConfig() {
        return jdaConfig;
    }

}
