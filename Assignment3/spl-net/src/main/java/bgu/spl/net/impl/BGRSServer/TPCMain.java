package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.PassiveObjects.Database;
import bgu.spl.net.api.MessageEncoderDecoderImp;
import bgu.spl.net.api.MessagingProtocolImp;
import bgu.spl.net.srv.Server;

public class TPCMain {

    public static void main(String[] args) {
        Database database = Database.getInstance();
        if(database.initialize("Courses.txt")){
            Server.threadPerClient(
                    Integer.parseInt(args[0]), //port
                    MessagingProtocolImp::new, //protocol factory
                    MessageEncoderDecoderImp::new //message encoder decoder factory
            ).serve();
        }
    }
}
