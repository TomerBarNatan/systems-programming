package bgu.spl.mics.application.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.LinkedList;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
    private boolean deactivateSent;

    /**
     * Constructor
     * @param attacks that Leia needs to send
     */
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
		this.deactivateSent = false;
    }

    @Override
    protected void initialize() {
        LinkedList<Future> futures = new LinkedList<>();

        for(Attack attack: attacks) {
            futures.add(sendEvent(new AttackEvent(attack)));
        }

        Callback<AttackFinishedEvent> call = (__) -> {
            futures.removeIf(future -> future.get() != null);
            if (!deactivateSent) {
                Future deactivation_future = sendEvent(new DeactivationEvent());
                while (deactivation_future == null) {
                    deactivation_future = sendEvent(new DeactivationEvent());
                }
                deactivation_future.get();
                sendEvent(new BombDestroyerEvent());
                deactivateSent = true;
            }
        };
        subscribeEvent(AttackFinishedEvent.class, call);

        subscribeBroadcast(TerminateBroadcast.class, (__)->{
            this.terminate();
            diary.setLeiaTerminate(System.currentTimeMillis());
        });
    }
}

