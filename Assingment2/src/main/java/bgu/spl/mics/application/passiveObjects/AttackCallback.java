package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.messages.AttackEvent;

/**
 * AttackCallback is an object that is used by C3PO and Han-Solo in their AttackEvent callback,
 * to avoid code duplication.
 * This object has both diary and ewoks instance.
 */

public class AttackCallback {
    private final Ewoks ewoks;
    private final Diary diary;

    /**
     * Constructor
     */
    public AttackCallback(){
        this.diary = Diary.getInstance();
        this.ewoks = Ewoks.getInstance();
    }

    /**
     * Receives an {@code attackEvent} and simulates the attack by Han-Solo or C3PO.
     * <p>
     * @param attackEvent    The attack event received by Leia.
     */
    public void callBack (AttackEvent attackEvent){
        ewoks.request(attackEvent.serialNumbers());
        try {
            Thread.sleep(attackEvent.attackDuration());
            diary.incTotalAttacks();
        } catch (InterruptedException error){
            error.printStackTrace();
        }
        ewoks.release(attackEvent.serialNumbers());
    }
}
