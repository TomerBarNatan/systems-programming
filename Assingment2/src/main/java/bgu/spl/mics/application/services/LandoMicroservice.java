package bgu.spl.mics.application.services;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;

    /**
     * Constructor
     * @param duration of Lando's sleep
     */
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {

        Callback<BombDestroyerEvent> call = (__)->{
            try {
                Thread.sleep(duration);
            }catch (InterruptedException error){
                error.printStackTrace();
            }
            sendBroadcast(new TerminateBroadcast());
        };
        subscribeEvent(BombDestroyerEvent.class, call);

        subscribeBroadcast(TerminateBroadcast.class, (__)->{
            this.terminate();
            diary.setLandoTerminate(System.currentTimeMillis());
        });
    }
}
