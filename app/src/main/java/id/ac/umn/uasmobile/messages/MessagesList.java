package id.ac.umn.uasmobile.messages;

public class MessagesList {

    private String name, password, lastMessage, profilePic, chatKey;
    private int unseenMessages;

    public MessagesList(String name, String password, String lastMessage, String profilePic, int unseenMessages, String chatKey) {
        this.name = name;
        this.password = password;
        this.lastMessage = lastMessage;
        this.profilePic = profilePic;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
}
