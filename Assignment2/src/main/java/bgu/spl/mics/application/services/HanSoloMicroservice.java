package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.AttackFinishedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.AttackCallback;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    private final AttackCallback attackCallback;
    private boolean attack_finished_sent;

    /**
     * Constructor
     */
    public HanSoloMicroservice() {
        super("Han");
        this.attackCallback = new AttackCallback();
        this.attack_finished_sent = false;
    }


    @Override
    protected void initialize() {
        Callback<AttackEvent> call = (AttackEvent attackEvent)->{
            this.attackCallback.callBack(attackEvent);
            diary.setHanSoloFinish(System.currentTimeMillis());
            complete(attackEvent,true);
            if(!attack_finished_sent) {
                sendEvent(new AttackFinishedEvent());
                attack_finished_sent = true;
            }
        };
        subscribeEvent(AttackEvent.class, call);

        subscribeBroadcast(TerminateBroadcast.class, (__)->{
            this.terminate();
            diary.setHanSoloTerminate(System.currentTimeMillis());
        });
    }
}
