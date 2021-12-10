package dev.lightdream.originalpanel.managers;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.lightdream.originalpanel.Main;
import dev.lightdream.originalpanel.dto.SQLConfig;
import dev.lightdream.originalpanel.dto.Staff;
import dev.lightdream.originalpanel.dto.data.Complain;
import dev.lightdream.originalpanel.dto.data.ComplainData;
import dev.lightdream.originalpanel.dto.data.LoginData;
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
        setup();

    }

    public void setup() {
        executeUpdate("CREATE TABLE IF NOT EXISTS `new_panel`.`complaints` ( `id` INT NOT NULL AUTO_INCREMENT , `user` TEXT NOT NULL , `target` TEXT NOT NULL , `section` TEXT NOT NULL , `date_and_time` TEXT NOT NULL , `description` TEXT NOT NULL, `status` TEXT NOT NULL , `target_response` TEXT NOT NULL , `proof` TEXT NOT NULL , `timestamp` BIGINT NOT NULL , PRIMARY KEY( `id`));", new ArrayList<>());
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
        Debugger.info(sql + " => " + values);
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

        List<String> staffRanks = Arrays.asList(
                "owner",
                "h-manager",
                "manager",
                "operator",
                "aradmin",
                "admin",
                "srmod",
                "mod",
                "jrmod",
                "helper",
                "trainee"
        );

        String args1 = "";
        String args2 = "";

        for (String staffRank : staffRanks) {
            //noinspection StringConcatenationInLoop
            args1 += "permission=\"group." + staffRank + "\" OR ";
            //noinspection StringConcatenationInLoop
            args2 += "primary_group=\"" + staffRank + "\" OR ";
        }
        args1 += "OR";
        args1 = args1.replace("OR OR", "");
        args2 += "OR";
        args2 = args2.replace("OR OR", "");

        String sql1 = "SELECT * FROM `luckperms`.`luckperms_user_permissions` WHERE server=\"global\" AND ( " + args1 + " );";
        String sql2 = "SELECT * FROM `luckperms`.`luckperms_players` WHERE " + args2;

        ResultSet r1 = executeQuery(sql1, new ArrayList<>());
        ResultSet r2 = executeQuery(sql2, new ArrayList<>());

        HashSet<String> staffsUUID = new HashSet<>();

        while (r1.next()) {
            staffsUUID.add(r1.getString("uuid"));
        }

        while (r2.next()) {
            staffsUUID.add(r2.getString("uuid"));
        }

        List<Staff> staffs = new ArrayList<>();

        staffsUUID.forEach(uuid -> {
            Staff staff = new Staff();
            staff.uuid = uuid;
            staff.username = getPlayerName(uuid);
            staff.rank = getRank(uuid);
            if (!staff.username.equals("null")) {
                staffs.add(staff);
            }
        });

        //Sort staff

        List<Staff> owner = staffs.stream().filter(staff -> staff.rank.equals("owner")).collect(Collectors.toList());
        List<Staff> hManager = staffs.stream().filter(staff -> staff.rank.equals("h-manager")).collect(Collectors.toList());
        List<Staff> manager = staffs.stream().filter(staff -> staff.rank.equals("manager")).collect(Collectors.toList());
        List<Staff> operator = staffs.stream().filter(staff -> staff.rank.equals("operator")).collect(Collectors.toList());
        List<Staff> srAdmin = staffs.stream().filter(staff -> staff.rank.equals("sradmin")).collect(Collectors.toList());
        List<Staff> admin = staffs.stream().filter(staff -> staff.rank.equals("admin")).collect(Collectors.toList());
        List<Staff> srMod = staffs.stream().filter(staff -> staff.rank.equals("srmod")).collect(Collectors.toList());
        List<Staff> mod = staffs.stream().filter(staff -> staff.rank.equals("mod")).collect(Collectors.toList());
        List<Staff> jrMod = staffs.stream().filter(staff -> staff.rank.equals("jrmod")).collect(Collectors.toList());
        List<Staff> helper = staffs.stream().filter(staff -> staff.rank.equals("helper")).collect(Collectors.toList());
        List<Staff> trainee = staffs.stream().filter(staff -> staff.rank.equals("trainee")).collect(Collectors.toList());

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

    @SneakyThrows
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public boolean validateUser(String username) {
        String sql = "SELECT COUNT(*) FROM `luckperms`.`luckperms_players` WHERE username=?";
        ResultSet r = executeQuery(sql, Arrays.asList(username));

        if (r.next()) {
            return r.getInt(1) >= 1;
        }

        return false;
    }

    public void saveComplain(ComplainData.ComplainDataRequest data) {
        String sql = "INSERT into `complaints` (user, target, section, date_and_time, description, proof, status, target_response, timestamp) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        LoginData.LoginDataAuth loginData;

        try {
            loginData = new Gson().fromJson(data.cookie, LoginData.LoginDataAuth.class);
        } catch (Throwable t) {
            return;
        }

        executeUpdate(sql, Arrays.asList(
                loginData.username,
                data.target,
                data.section,
                data.dateAndTime,
                data.description,
                data.proof,
                data.status.toString(),
                data.target,
                data.timestamp
        ));
    }

    @SneakyThrows
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public String getPlayerUUID(String username) {
        String sql = "SELECT uuid FROM `luckperms`.`luckperms_players` WHERE username=?";
        ResultSet r = executeQuery(sql, Arrays.asList(username.toLowerCase()));

        if (r.next()) {
            return r.getString(1);
        }

        return "";

    }

    @SneakyThrows
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public String getRank(String uuid) {
        String sql1 = "SELECT primary_group FROM `luckperms`.`luckperms_players` WHERE uuid=?";
        String sql2 = "SELECT permission FROM `luckperms`.`luckperms_user_permissions` WHERE permission LIKE \"group.%\" AND uuid=?";

        List<String> userRanks = new ArrayList<>();

        ResultSet r1 = executeQuery(sql1, Arrays.asList(uuid));
        ResultSet r2 = executeQuery(sql2, Arrays.asList(uuid));

        if (r1.next()) {
            userRanks.add(r1.getString(1).replace("group.", ""));
        }

        while (r2.next()) {
            userRanks.add(r2.getString(1).replace("group.", ""));
        }

        return selectHighestRank(userRanks);
    }

    public String selectHighestRank(List<String> ranks) {

        Debugger.info(ranks);

        List<String> definedRanks = new ArrayList<>(Arrays.asList(
                "owner",
                "h-manager",
                "manager",
                "operator",
                "aradmin",
                "admin",
                "srmod",
                "mod",
                "jrmod",
                "helper",
                "trainee",
                "sponsor",
                "original",
                "eternal",
                "platinum",
                "legendary",
                "god",
                "xenon",
                "heda",
                "suprem",
                "alpha"
        ));

        for (String rank : definedRanks) {
            if (ranks.contains(rank)) {
                return rank;
            }
        }

        return "default";
    }

    @SneakyThrows
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public Long getJoinDate(String username) {
        String sql = "SELECT regdate FROM `authme`.`authme` WHERE  username=?";
        ResultSet r = executeQuery(sql, Arrays.asList(username.toLowerCase()));

        if (r.next()) {
            return r.getLong(1);
        }

        return 0L;
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @SneakyThrows
    public int getPlayTime(String uuid) {
        String sql = "SELECT playedtime FROM `bungeecord`.`playtime` WHERE uuid=?";
        ResultSet r = executeQuery(sql, Arrays.asList(uuid));

        int output = 0;

        while (r.next()) {
            output += r.getInt(1);
        }

        return output;
    }

    @SneakyThrows
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public int getOriginalCoins(String uuid) {
        String sql = "SELECT points FROM `player_points`.`playerpoints_points` WHERE uuid=?";
        ResultSet r = executeQuery(sql, Arrays.asList(uuid));

        if (r.next()) {
            return r.getInt(1);
        }

        return 0;
    }

    @SneakyThrows
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public Long getDiscordID(String uuid) {
        String sql = "SELECT discord_id FROM `royal_security`.`users` WHERE uuid=?";
        ResultSet r = executeQuery(sql, Arrays.asList(uuid));

        if (r.next()) {
            return r.getLong(1);
        }

        return 0L;
    }


    @SneakyThrows
    public List<Complain> getComplains(String username) {
        List<Complain> complaints =new ArrayList<>();

        String sql = "SELECT * FROM `new_panel`.`complaints` WHERE user=? or target=?";
        LoginData.LoginDataAuth loginData;

        ResultSet r =executeQuery(sql, Arrays.asList(
                username,
                username
        ));

        while(r.next()){
            Complain complain =new Complain(
                    r.getInt("id"),
                    r.getString("user"),
                    r.getString("target"),
                    r.getString("section"),
                    r.getString("date_and_time"),
                    r.getString("description"),
                    r.getString("proof"),
                    ComplainData.ComplainStatus.valueOf(r.getString("status")),
                    r.getString("target_response"),
                    r.getLong("timestamp")
            );
            complaints.add(complain);
        }

        return complaints;
    }



}
