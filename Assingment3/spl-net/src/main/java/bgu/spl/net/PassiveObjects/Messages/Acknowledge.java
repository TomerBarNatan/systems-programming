package bgu.spl.net.PassiveObjects.Messages;

/**
 * This class is used to wrap information about acknowledge set to the client
 *
 */
public class Acknowledge implements ServerMessage{
    private final short opcode = 12;
    private short message_opcode;
    private String optional = null;

    /**
     * Constructor
     * @param message_opcode
     */
    public Acknowledge(short message_opcode) {
        this.message_opcode = message_opcode;
    }
    @Override
    public short getOpcode() {
        return opcode;
    }

    @Override
    public short getMessageOpcode() {
        return message_opcode;
    }

    public void setOptional(String optional) {
        this.optional = optional;
    }

    public String getOptional() {
        return optional;
    }
}

