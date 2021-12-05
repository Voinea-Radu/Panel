package dev.lightdream.originalpanel;

import dev.lightdream.originalpanel.dto.SQLConfig;
import dev.lightdream.originalpanel.managers.CacheManager;
import dev.lightdream.originalpanel.managers.FileManager;
import dev.lightdream.originalpanel.managers.DatabaseManager;
import dev.lightdream.originalpanel.utils.Debugger;
import dev.lightdream.originalpanel.utils.Logger;

import javax.sql.rowset.CachedRowSet;
import java.io.File;

public class Main {

    public static Main instance;
    public SQLConfig sqlConfig;
    public FileManager fileManager;
    public DatabaseManager databaseManager;
    public CacheManager cacheManager;

    public Main(){
        Debugger.init(this);
        Logger.init(this);
        Main.instance=this;
        this.fileManager = new FileManager(this, FileManager.PersistType.YAML);
        loadConfigs();
        databaseManager = new DatabaseManager(this);
        cacheManager=new CacheManager(this);
    }

    public void loadConfigs(){
        sqlConfig = fileManager.load(SQLConfig.class);
    }

    public boolean debug() {
        return true; //todo configure
    }

    public void log(String log){
        System.out.println(log);
    }

    public File getDataFolder() {
        return new File(System.getProperty("user.dir"));
    }
}
