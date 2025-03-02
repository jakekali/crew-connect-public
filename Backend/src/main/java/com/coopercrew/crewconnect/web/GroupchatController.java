package com.coopercrew.crewconnect.web;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.coopercrew.crewconnect.DatabaseConnectionManager;
import com.coopercrew.crewconnect.*;

@RestController
@CrossOrigin
public class GroupchatController {
    String hostname = "db";

    // insert groupchat
    @PostMapping("/groupchat")
	public void makeGroupchat(@RequestBody Groupchat Groupchat) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
        try {
            Connection connection = dcm.getConnection();
            GroupchatDAO groupChatD = new GroupchatDAO(connection);

           groupChatD.createGroupChat(Groupchat.getGroupName(), Groupchat.getGroupSize(), Groupchat.getDateCreated());
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // get groupchat by group id
    @GetMapping("/groupchat/id/{group_id}") 
    public Groupchat getGroupchatbyGroupId(@PathVariable long group_id) {


        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
                Groupchat groupChat = new Groupchat();
            
        try {
            Connection connection = dcm.getConnection();
            GroupchatDAO groupChatD = new GroupchatDAO(connection);
            groupChat = groupChatD.findByGroupChatId(group_id);
            System.out.println(groupChat);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return groupChat;

    }
//get groupchat by group name
    @GetMapping("/groupchat/name/{groupname}") 
    public Groupchat getGroupchatbyGroupName(@PathVariable String groupname) {


        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
                Groupchat groupChat = new Groupchat();
            
        try {
            Connection connection = dcm.getConnection();
            GroupchatDAO groupChatD = new GroupchatDAO(connection);
            groupChat = groupChatD.findByGroupChatName(groupname);
            System.out.println(groupChat);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return groupChat;

    }

    // delete groupchat by id
    @DeleteMapping("/groupchat/id/{group_id}") 
    public void deleteByGroupchatID(@PathVariable long group_id) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
        try {
            Connection connection = dcm.getConnection();
            GroupchatDAO groupChatD = new GroupchatDAO(connection);
            groupChatD.deleteByGroupChatId(group_id);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }

    }

    // get groupchat by size 
    @GetMapping("/groupchat/size/{size}") 
    public Groupchat getbyGroupSize(@PathVariable int size) {
       
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
                Groupchat groupChat = new Groupchat();
        try {
            Connection connection = dcm.getConnection();
            GroupchatDAO groupChatD = new GroupchatDAO(connection);
            groupChat = groupChatD.findByGroupChatSize(size);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return groupChat;
    }
    
    // update groupchat size
    @PutMapping("/groupchat/id/{id}/size/{size}") 
    public void updateGroupSize(@PathVariable long id, @PathVariable int size) {
        
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
            
        try {
            Connection connection = dcm.getConnection();
            GroupchatDAO groupChatD = new GroupchatDAO(connection);
            groupChatD.updateGroupChatSize(id, size);

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // update groupchat name
    @PutMapping("/groupchat/id/{id}/name/{name}") 
    public void updateGroupName(@PathVariable long id, @PathVariable String name) {
        
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
            
        try {
            Connection connection = dcm.getConnection();
            GroupchatDAO groupChatD = new GroupchatDAO(connection);
            groupChatD.updateGroupChatName(id, name);

        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // get all groupchats with user
    @GetMapping("/groupchats/userId/{user_id}")
    public ArrayList<Groupchat> getGroupchatfromUser (@PathVariable int user_id) {
        ArrayList<Groupchat> Groupchats = new ArrayList<Groupchat>();
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
            
        try {
            Connection connection = dcm.getConnection();
            JoinsDAO joinsDAO = new JoinsDAO(connection);
            Groupchats = joinsDAO.getAllGroupChatsWithUser(user_id);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return Groupchats;
    }

    // add user to groupchat
    @PutMapping("/groupchat/gcId/{gcId}/userId/{userId}") 
    public void addUserToGroupChat(@PathVariable long gcId, @PathVariable long userId) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
        try {
            Connection connection = dcm.getConnection();
            GroupchatDAO groupChatD = new GroupchatDAO(connection);
           groupChatD.addUserToGroupChat(gcId, userId);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // delete user from groupchat
    @DeleteMapping("/groupchat/gcId/{gcId}/userId/{userId}") 
    public void deleteUserFromGroupChat(@PathVariable long gcId, @PathVariable long userId) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager(hostname,
                "crewconnect3", "postgres", "password");
        try {
            Connection connection = dcm.getConnection();
            GroupchatDAO groupChatD = new GroupchatDAO(connection);
            groupChatD.deleteUserFromGroupChat(gcId, userId);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
}