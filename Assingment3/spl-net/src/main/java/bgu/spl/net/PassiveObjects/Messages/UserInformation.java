package bgu.spl.net.PassiveObjects.Messages;

/**
 * A message sent from client witch contains user information: username and password
 */
public class UserInformation implements ClientMessage{
    private final short opcode;
    private final String username;
    private final String password;

    public UserInformation(short opcode, String userName, String password){
        this.opcode = opcode;
        this.username = userName;
        this.password = password;
    }
    @Override
    public short getOpcode() {
        return opcode;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
