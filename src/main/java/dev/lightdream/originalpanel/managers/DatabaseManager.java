package dev.lightdream.originalpanel.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.SQLConfig;
import dev.lightdream.originalpanel.dto.Staff;
import dev.lightdream.originalpanel.utils.Debugger;
import dev.lightdream.originalpanel.utils.Logger;
import lombok.SneakyThrows;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class DatabaseManager {

    public SQLConfig sqlConfig;
    public Main main;
    private Connection connection;

    @SuppressWarnings("FieldMayBeFinal")
    public DatabaseManager(Main main) {
        Logger.good("Connecting to the database...");
        this.main = main;
        this.sqlConfig = main.sqlConfig;
        connect();
        Logger.good("Database connected");
    }

    public @NotNull String getDatabaseURL() {
        switch (sqlConfig.driver) {
            case MYSQL:
            case MARIADB:
            case POSTGRESQL:
                return "jdbc:" + sqlConfig.driver.toString().toLowerCase() + "://" + sqlConfig.host + ":" + sqlConfig.port + "/" + sqlConfig.database + "?useSSL=" + sqlConfig.useSSL + "&autoReconnect=true";
            case SQLSERVER:
                return "jdbc:sqlserver://" + sqlConfig.host + ":" + sqlConfig.port + ";databaseName=" + sqlConfig.database;
            case H2:
                return "jdbc:h2:file:" + sqlConfig.database;
            case SQLITE:
                return "jdbc:sqlite:" + new File(main.getDataFolder(), sqlConfig.database + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + sqlConfig.driver.name());
        }
    }

    @SneakyThrows
    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getDatabaseURL());
        config.setUsername(sqlConfig.username);
        config.setPassword(sqlConfig.password);
        config.setConnectionTestQuery("SELECT 1");
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(1000000000);
        config.setIdleTimeout(600000000);
        config.setMaxLifetime(1800000000);
        switch (sqlConfig.driver) {
            case SQLITE:
                config.setDriverClassName("org.sqlite.JDBC");
                config.addDataSourceProperty("dataSourceClassName", "org.sqlite.SQLiteDataSource");
                break;
        }
        HikariDataSource ds = new HikariDataSource(config);
        connection = ds.getConnection();
    }

    @SneakyThrows
    private void executeUpdate(String sql, List<Object> values) {
        Debugger.info(sql);
        PreparedStatement statement = connection.prepareStatement(sql);

        for (int i = 0; i < values.size(); i++) {
            statement.setObject(i + 1, values.get(i));
        }

        statement.executeUpdate();
    }

    @SneakyThrows
    private ResultSet executeQuery(String sql, List<Object> values) {
        Debugger.info(sql);
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
        } catch (Throwable t) {
            Logger.error("The connection to the database has been lost trying to reconnect!");
            connect();
            return executeQuery(sql, values);
        }

        for (int i = 0; i < values.size(); i++) {
            statement.setObject(i + 1, values.get(i));
        }

        return statement.executeQuery();
    }

    @SneakyThrows
    public int getDonorsCount() {
        String sql = "SELECT COUNT(*) FROM `luckperms`.`luckperms_user_permissions` WHERE permission LIKE \"group.%\" AND permission!=\"group.default\" AND server=\"global\";";
        ResultSet r = executeQuery(sql, new ArrayList<>());
        if (r.next()) {
            return r.getInt(1);
        }
        return 0;
    }


    @SneakyThrows
    public int getRegisteredCount() {
        String sql = "SELECT COUNT(*) FROM `authme`.`authme` WHERE 1";
        ResultSet r = executeQuery(sql, new ArrayList<>());
        if (r.next()) {
            return r.getInt(1);
        }
        return 0;
    }

    @SneakyThrows
    public String getAuthMePassword(String username) {
        String sql = "SELECT password FROM `authme`.`authme` WHERE username=? OR realname=?";
        ResultSet r = executeQuery(sql, Arrays.asList(
                username, username
        ));
        if (r.next()) {
            return r.getString(1);
        }
        return "";
    }

    @SneakyThrows
    public List<Staff> getStaff() {
        String sql = "SELECT * FROM `luckperms`.`luckperms_user_permissions` WHERE server=\"global\" AND ( permission=\"group.trainee\" OR permission=\"group.helper\" OR permission=\"group.jrmod\" OR permission=\"group.mod\" OR permission=\"group.srmod\" OR permission=\"group.admin\" OR permission=\"group.sradmin\" OR permission=\"group.operator\" OR permission=\"group.manager\" OR permission=\"group.owner\" );";
        ResultSet r = executeQuery(sql, new ArrayList<>());

        List<Staff> staffs = new ArrayList<>();

        while (r.next()) {
            Staff staff = new Staff();
            staff.uuid = r.getString("uuid");
            staff.rank = r.getString("permission").replace("group.", "");
            staff.username = getPlayerName(staff.uuid);

            staffs.add(staff);
        }

        //Sort staff

        List<Staff> owner = staffs.stream().filter(staff->staff.rank.equals("owner")).collect(Collectors.toList());
        List<Staff> hManager = staffs.stream().filter(staff->staff.rank.equals("h-manager")).collect(Collectors.toList());
        List<Staff> manager = staffs.stream().filter(staff->staff.rank.equals("manager")).collect(Collectors.toList());
        List<Staff> operator = staffs.stream().filter(staff->staff.rank.equals("operator")).collect(Collectors.toList());
        List<Staff> srAdmin = staffs.stream().filter(staff->staff.rank.equals("srAdmin")).collect(Collectors.toList());
        List<Staff> admin = staffs.stream().filter(staff->staff.rank.equals("admin")).collect(Collectors.toList());
        List<Staff> srMod = staffs.stream().filter(staff->staff.rank.equals("srMod")).collect(Collectors.toList());
        List<Staff> mod = staffs.stream().filter(staff->staff.rank.equals("mod")).collect(Collectors.toList());
        List<Staff> jrMod = staffs.stream().filter(staff->staff.rank.equals("jrMod")).collect(Collectors.toList());
        List<Staff> helper = staffs.stream().filter(staff->staff.rank.equals("helper")).collect(Collectors.toList());
        List<Staff> trainee = staffs.stream().filter(staff->staff.rank.equals("trainee")).collect(Collectors.toList());

        Collections.sort(owner);
        Collections.sort(hManager);
        Collections.sort(manager);
        Collections.sort(operator);
        Collections.sort(srAdmin);
        Collections.sort(admin);
        Collections.sort(srMod);
        Collections.sort(mod);
        Collections.sort(jrMod);
        Collections.sort(helper);
        Collections.sort(trainee);

        List<Staff> output = new ArrayList<>();

        output.addAll(owner);
        output.addAll(hManager);
        output.addAll(manager);
        output.addAll(operator);
        output.addAll(srAdmin);
        output.addAll(admin);
        output.addAll(srMod);
        output.addAll(mod);
        output.addAll(jrMod);
        output.addAll(helper);
        output.addAll(trainee);

        return output;
    }

    @SneakyThrows
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public String getPlayerName(String uuid) {
        String sql = "SELECT * FROM `luckperms`.`luckperms_players` WHERE uuid=?";
        ResultSet r = executeQuery(sql, Arrays.asList(uuid));

        if (r.next()) {
            return r.getString("username");
        }

        return "Undefined";
    }


}
