package bgu.spl.net.PassiveObjects.Messages;

/**
 * Represent a message sent to client by server
 */
public interface ServerMessage extends Message{
    public short getMessageOpcode();
}
