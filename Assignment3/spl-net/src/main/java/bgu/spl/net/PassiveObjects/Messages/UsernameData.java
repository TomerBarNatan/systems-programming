package bgu.spl.net.PassiveObjects.Messages;
/**
 * A message sent from client witch contains username information
 */
public class UsernameData implements ClientMessage{
    private final short opcode = 8;
    private final String username;

    public UsernameData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public short getOpcode() {
        return opcode;
    }
}
