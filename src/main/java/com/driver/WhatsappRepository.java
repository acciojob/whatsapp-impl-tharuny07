package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashMap<String, User> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashMap<>();
        this.customGroupCount = 0;
        this.messageId = -1;
    }
    public String createUser(String name, String mobile) {
        if(userMobile.containsKey(mobile)){
            return "";
        }
        userMobile.put(mobile,new User(name,mobile));
        return"SUCCESS";
    }

    public Group createGroup(List<User> users) {

        int usersCount=users.size();
        String groupName;
        if(usersCount==2){
            groupName=users.get(1).getName();
            Group group=new Group(groupName,usersCount);
            groupUserMap.put(group,users);
            adminMap.put(group,users.get(0));
            return group;
        }

        groupName="Group "+customGroupCount;
        customGroupCount++;
        Group group=new Group(groupName,usersCount);
        groupUserMap.put(group,users);
        adminMap.put(group,users.get(0));
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        Date date=new Date();
        Message message=new Message(messageId,content,new Date());
        // System.out.println(date);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) {
        if(!groupUserMap.containsKey(group)){
            return -1;
        }
        boolean flag=false;
        for(User user:groupUserMap.get(group)){
            if(user.equals(sender)){
                flag=true;
            }
        }
        if(flag==false){
            return -2;
        }

        senderMap.put(message,sender);

        List<Message> messageList=new ArrayList<>();

        messageList.add(message);
        groupMessageMap.put(group,messageList);
        return messageId;
    }

    public String changeAdmin(User approver, User user, Group group) {
        if(!groupUserMap.containsKey(group)){
            return "1";
        }
        boolean flag=false;
        if(adminMap.get(group).equals(approver)){
            flag=true;
        }
        if(flag==false){
            return "2";
        }
        for(User user1:groupUserMap.get(group)){
            if(user1.equals(user)){
                flag=false;
            }
        }
        if(flag==true){
            return "3";
        }
        adminMap.put(group,user);
        return "SUCCESS";
    }
}
