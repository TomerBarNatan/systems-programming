package bgu.spl.net.PassiveObjects.Messages;

/**
 * A message witch server sends to client to inform his that his request was bad
 */
public class Error implements ServerMessage {
    private final short opcode = 13;
    private short message_opcode;

    public Error(short message_opcode) {
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
}
