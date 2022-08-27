package bgu.spl.net.PassiveObjects.Messages;

/**
 * Client message witch contains number of course
 */
public class CourseInformation implements ClientMessage{
    private short opcode;
    private short courseNumber;

    public CourseInformation(short opcode, short courseNumber){
        this.opcode = opcode;
        this.courseNumber = courseNumber;
    }
    @Override
    public short getOpcode() {
        return opcode;
    }

    public short getCourseNumber() {
        return courseNumber;
    }
}
