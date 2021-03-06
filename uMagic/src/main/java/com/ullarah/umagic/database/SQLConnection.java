package com.ullarah.umagic.database;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLConnection extends SQLDatabase {

    private final Plugin plugin;
    private final String database;
    private final String create;
    private final SQLMessage message;

    public SQLConnection(Plugin plugin, String database, String create) {
        super(plugin, database);
        this.plugin = plugin;
        this.database = database;
        this.create = create;
        message = new SQLMessage();

        startSQLConnection();
    }

    private Plugin getPlugin() {
        return plugin;
    }

    private String getDatabase() {
        return database;
    }

    private String getCreate() {
        return create;
    }

    private SQLMessage getMessage() {
        return message;
    }

    public Connection getSQLConnection() {

        boolean databaseFileCreation = true;

        File dataDir = getPlugin().getDataFolder();
        if (!dataDir.exists()) databaseFileCreation = dataDir.mkdir();

        File databaseFile = new File(dataDir + File.separator + getDatabase() + ".db");
        if (!databaseFile.exists()) databaseFileCreation = true;

        if (databaseFileCreation) try {

            if (getConnection() != null && !getConnection().isClosed()) return getConnection();

            Class.forName("org.sqlite.JDBC");
            setConnection(DriverManager.getConnection("jdbc:sqlite:" + databaseFile));

            return getConnection();

        } catch (SQLException ex) {

            getPlugin().getLogger().log(Level.SEVERE, getMessage().sqlInitialiseError(), ex);

        } catch (ClassNotFoundException ex) {

            getPlugin().getLogger().log(Level.SEVERE, getMessage().sqlLibraryNotFound());

        }

        return null;

    }

    private void startSQLConnection() {

        connection = getSQLConnection();

        try {

            Statement s = connection.createStatement();
            s.executeUpdate(getCreate());
            s.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

        initSQLConnection();

    }

}