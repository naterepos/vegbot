package com.github.higgs.vegbot.resources;

import com.github.higgs.vegbot.VegBot;
import com.github.higgs.vegbot.user.Role;
import com.github.higgs.vegbot.user.Roles;
import com.github.higgs.vegbot.user.VegUser;
import net.dv8tion.jda.api.entities.Member;
import org.apache.commons.collections4.comparators.ReverseComparator;

import java.sql.*;
import java.util.*;

public class MySQL {

    private static MySQL instance;

    public static MySQL getOrCreate() {
        if(instance == null) {
            instance = new MySQL();
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private MySQL() {
        try(Connection conn = getConnection()) {
            assert conn != null;
            Statement stmt = conn.createStatement();
            stmt.execute("create database if not exists VegBot");
            stmt.execute("create table if not exists USER_DATA (" +
                    "UUID varchar(18) not null primary key, " +
                    "NAME varchar(30), " +
                    "MONTHS_VEGAN smallint default 0," +
                    "PRONOUNS varchar(20)" +
                    ")");

            stmt.execute("create table if not exists ROLES (" +
                    "UUID varchar(18) not null, " +
                    "ROLE_ID varchar(35) not null, " +
                    "primary key(UUID, ROLE_ID))");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Member member : VegBot.getUsers().getAllMembers()) {
            tryCreateProfile(member);
            trySetupDefaultRole(member);
        }
    }

    public void tryCreateProfile(Member user) {
        try(Connection conn = getConnection()) {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("insert ignore into USER_DATA (UUID, NAME, MONTHS_VEGAN, PRONOUNS) values (?,?,?,?)");
            stmt.setString(1, user.getId());
            stmt.setString(2, null);
            stmt.setInt(3, 0);
            stmt.setString(4, null);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void trySetupDefaultRole(Member user) {
        try(Connection conn = getConnection()) {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("insert ignore into ROLES (UUID, ROLE_ID) values (?,?)");
            stmt.setString(1, user.getId());
            stmt.setString(2, Roles.CURIOUS.getID());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRealName(String user, String realName) {
        try(Connection conn = getConnection()) {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("insert into USER_DATA (UUID, NAME) values (?,?) on duplicate key update NAME = ?");
            stmt.setString(1, user);
            stmt.setString(2, realName);
            stmt.setString(3, realName);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRole(String user, Role role) {
        try(Connection conn = getConnection()) {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("insert ignore into ROLES (UUID, ROLE_ID) values (?,?)");
            stmt.setString(1, user);
            stmt.setString(2, role.getID());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeRole(String user, Role role) {
        try(Connection conn = getConnection()) {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("delete from ROLES where UUID=? AND ROLE_ID=?");
            stmt.setString(1, user);
            stmt.setString(2, role.getID());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPronouns(String user, String pronouns) {
        try(Connection conn = getConnection()) {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("insert into USER_DATA (UUID, PRONOUNS) values (?,?) on duplicate key update PRONOUNS = ?");
            stmt.setString(1, user);
            stmt.setString(2, pronouns);
            stmt.setString(3, pronouns);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMonthsVegan(String user, int monthsVegan) {
        try(Connection conn = getConnection()) {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("insert into USER_DATA (UUID, MONTHS_VEGAN) values (?,?) on duplicate key update MONTHS_VEGAN = ?");
            stmt.setString(1, user);
            stmt.setInt(2, monthsVegan);
            stmt.setInt(3, monthsVegan);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getUserInfo(String user) {
        Map<String, String> info = new HashMap<>();
        try(Connection conn = getConnection()) {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("select * from USER_DATA where UUID=?");
            stmt.setString(1, user);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    info.put("NAME", Optional.ofNullable(rs.getString("NAME")).orElse("N/A"));
                    info.put("MONTHS_VEGAN", String.valueOf(Optional.of(rs.getInt("MONTHS_VEGAN")).orElse(0)));
                    info.put("PRONOUNS", Optional.ofNullable(rs.getString("PRONOUNS")).orElse("N/A"));
                }
            }
            //TODO: get roles retrieved in user constructor
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return info;
    }

    public TreeMap<String, Role> getRoles(VegUser user) {
        TreeMap<String, Role> roles = new TreeMap<>();
        try(Connection conn = getConnection()) {
            assert conn != null;
            PreparedStatement stmt = conn.prepareStatement("select * from ROLES where UUID=?");
            stmt.setString(1, user.getUser().getId());
            try(ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    String value = rs.getString("ROLE_ID");
                    roles.put(value, Roles.getRoleFromString(value));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return withSorting(roles);
    }

    private TreeMap<String, Role> withSorting(TreeMap<String, Role> map) {
        TreeMap<String, Role> sorted = new TreeMap<>(Comparator.comparing(map::get));
        sorted.putAll(map);
        return sorted;
    }

    private Connection getConnection() {
        try {
            String hostname = VegBot.<String>getSetting("MySQLHost").orElse(null);
            String database = VegBot.<String>getSetting("MySQLDatabase").orElse(null);
            String username = VegBot.<String>getSetting("MySQLUsername").orElse(null);
            String password = VegBot.<String>getSetting("MySQLPassword").orElse(null);

            if(hostname == null || database == null || username == null || password == null) {
                throw new SQLException("Could not load MySQL login correctly");
            } else {
                return DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + database + "?characterEncoding=latin1", username, password);
            }

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
