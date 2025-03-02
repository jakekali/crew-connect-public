package com.coopercrew.crewconnect;

import com.coopercrew.crewconnect.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServerDAO extends DataAccessObject {
    public static final String GET_BY_SERVER_ID = "SELECT server_id, server_name FROM servers WHERE server_id = ?";
    public static final String GET_BY_SERVERNAME = "SELECT server_id, server_name FROM servers WHERE server_name = ?";
    public static final String INSERT_SERVER = "INSERT INTO servers (server_name) VALUES (?)";
    public static final String INSERT_SERVER_GROUPCHAT = "INSERT INTO server_groupchats (server_id, gc_id) VALUES (?, ?)";
    public static final String DELETE_SERVER = "DELETE FROM servers WHERE server_id = ?";
    public static final String GET_GROUPCHATS_BY_SERVER_ID = "SELECT gc.gc_id, gc.group_name, gc.group_size, gc.date_created FROM groupchats gc JOIN server_groupchats sg ON gc.gc_id = sg.gc_id WHERE sg.server_id = ?";
    // first we need to join users onto user_gc by user_id to get all groupchats that the user is a part of
    // then we need to join this new table onto server_groupchats by gc_id to get all the servers the groupchats are a part of
    // then do one final join on server_id to retrieve name and id of each server the user is a part of.
    public static final String FIND_SERVERS_BY_USER_ID = "SELECT DISTINCT s.server_id, s.server_name " +
    "FROM users u " +
    "JOIN users_gc ugc ON u.user_id = ugc.user_id " +
    "JOIN server_groupchats sg ON ugc.gc_id = sg.gc_id " +
    "JOIN servers s ON sg.server_id = s.server_id " +
    "WHERE u.user_id = ?";

    public ServerDAO(Connection connection) {
        super(connection);
    }

    public void setServerAttributes(PreparedStatement statement, Server server) throws SQLException {
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            server.setServerId(rs.getLong("server_id"));
            server.setServerName(rs.getString("server_name"));
        } 
    }

    public Server findByServerId(long id) {
        Server server = new Server();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_BY_SERVER_ID);) {
            statement.setLong(1, id);
            setServerAttributes(statement, server);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return server;
    }

    public Server findByServerName(String name) {
        Server server = new Server();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_BY_SERVERNAME);) {
            statement.setString(1, name);
            setServerAttributes(statement, server);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println(server);
        return server;
    }

    public void createServer(String serverName) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT_SERVER);) {
            statement.setString(1, serverName);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void addGroupChatToServer(long serverId, long gcId) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT_SERVER_GROUPCHAT);) {
            statement.setLong(1, serverId);
            statement.setLong(2, gcId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void deleteServer(long serverId) {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_SERVER);) {
            statement.setLong(1, serverId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Groupchat> getGroupChatsByServerId(long serverId) {
        List<Groupchat> groupchats = new ArrayList<>();

        try (PreparedStatement statement = this.connection.prepareStatement(GET_GROUPCHATS_BY_SERVER_ID);) {
            statement.setLong(1, serverId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Groupchat groupchat = new Groupchat();
                groupchat.setGroupChatId(rs.getLong("gc_id"));
                groupchat.setGroupName(rs.getString("group_name"));
                groupchat.setGroupSize(rs.getInt("group_size"));
                groupchat.setDateCreated(rs.getString("date_created"));
                groupchats.add(groupchat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return groupchats;
    }

    public List<Server> findServersByUserId(long userId) {
        List<Server> servers = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_SERVERS_BY_USER_ID);) {
            statement.setLong(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Server server = new Server();
                server.setServerId(rs.getLong("server_id"));
                server.setServerName(rs.getString("server_name"));
                servers.add(server);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return servers;
    }
}
