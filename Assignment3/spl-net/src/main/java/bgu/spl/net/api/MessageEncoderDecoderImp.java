package bgu.spl.net.api;

import bgu.spl.net.PassiveObjects.Messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoderImp implements MessageEncoderDecoder<Message> {

    private byte[] bytes = new byte[1 << 10];
    private int len = 0;
    private short opcode = 0;
    private short zeroCounter = 0;
    private int firstZeroIndex = 0;

    @Override
    public Message decodeNextByte(byte nextByte){
        pushByte(nextByte);
        if(len == 2){
            opcode = bytesToShort(bytes);
        }
        if (opcode != 0){
            return createMessage(opcode, nextByte);
        }
        return null;
    }

    private Message createMessage(short opcode, byte nextByte){

        if(opcode == 4 | opcode == 11) {
            short copied_opcode = opcode;
            reset_encoder_decoder();
            return new SimpleActions(copied_opcode);
        }
        if(opcode == 5 | opcode == 6 | opcode == 7 | opcode == 9 | opcode == 10){
            return CreateCourseInformationMessage(opcode);
        }
        if(opcode == 1 | opcode == 2 | opcode ==3){
            return CreateUserInformationMessage(opcode, nextByte);
        }
        else {
            return CreateUsernameDataMessage(nextByte);
        }
    }

    private CourseInformation CreateCourseInformationMessage(short opcode){
        if(len == 4){
            byte[] course_number_bytes = {bytes[2], bytes[3]};
            short course_number = bytesToShort(course_number_bytes);
            short copied_opcode = opcode;
            reset_encoder_decoder();
            return new CourseInformation(copied_opcode, course_number);
        }
        return null;
    }

    private UserInformation CreateUserInformationMessage(short opcode, byte nextByte){
        if(nextByte == '\0'){
            zeroCounter++;

            if (zeroCounter == 1){
                firstZeroIndex = len;
            }
            else {
                String username = new String(bytes, 2, firstZeroIndex - 3, StandardCharsets.UTF_8);
                String password = new String(bytes, firstZeroIndex, len - 1, StandardCharsets.UTF_8);
                short copied_opcode = opcode;
                reset_encoder_decoder();
                return new UserInformation(copied_opcode, username, password);
            }
        }
        return null;
    }

    private UsernameData CreateUsernameDataMessage(byte nextByte){
        if(nextByte == '\0'){
            String username = new String(bytes, 2, len - 3, StandardCharsets.UTF_8);
            reset_encoder_decoder();
            return new UsernameData(username);
        }
        return null;
    }

    private void pushByte(byte nextByte){
        if (len >= bytes.length) {
            bytes = Arrays.copyOf(bytes, len * 2);
        }

        bytes[len++] = nextByte;
    }

    private short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }


    @Override
    public byte[] encode(Message message) {
        short server_opcode = message.getOpcode();
        byte[] encoded_server_opcode = shortToBytes(server_opcode);
        byte[] encoded_message_opcode = shortToBytes(((ServerMessage)message).getMessageOpcode());
        byte[] encoded_optional = null;
        int optional_length = 0;
        if(server_opcode == 12){
            String optional = ((Acknowledge)message).getOptional();
            if(optional != null){
                encoded_optional = optional.getBytes();
                optional_length = encoded_optional.length;
            }
        }
        byte[] encoded_message = new byte[4 + optional_length];
        encoded_message[0] = encoded_server_opcode[0];
        encoded_message[1] = encoded_server_opcode[1];
        encoded_message[2] = encoded_message_opcode[0];
        encoded_message[3] = encoded_message_opcode[1];

        for(int i = 0; i < optional_length; i++) {
            encoded_message[i + 4] = encoded_optional[i];
        }
        return encoded_message;
    }

    private byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private void reset_encoder_decoder(){
        bytes = new byte[1 << 10];
        len = 0;
        opcode = 0;
        zeroCounter = 0;
        firstZeroIndex = 0;
    }
}
