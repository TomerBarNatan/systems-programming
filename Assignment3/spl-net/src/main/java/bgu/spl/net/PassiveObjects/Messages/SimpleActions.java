package bgu.spl.net.PassiveObjects.Messages;

/**
 *A message got from client witch contains only opcode information
 */
public class SimpleActions implements ClientMessage{
    private final short opcode;

    public SimpleActions(short opcode){
        this.opcode = opcode;
    }
    @Override
    public short getOpcode(){
        return opcode;
    }
}
