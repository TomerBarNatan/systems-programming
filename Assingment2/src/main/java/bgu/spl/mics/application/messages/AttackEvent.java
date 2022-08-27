package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;
import java.util.List;

/**
 * AttackEvent is kind of event which is sent by Liea and
 * subscribed by Han-Solo and C3PO.
 * It contains information about how long is the attack and what resources should be used in it.
 */
public class AttackEvent implements Event<Boolean> {
	private final Attack attack;

    /**
     * Constructor.
     * @param attack contains the relevant information in order to create the attack event
     */
	public AttackEvent(Attack attack){
	    this.attack = attack;
    }

    /**
     * @return the duration of the attack
     */
    public int attackDuration(){
	    return attack.getDuration();
    }

    /**
     * @return the list of serial numbers needed for the event
     */
    public List<Integer> serialNumbers(){
	    return attack.getSerials();
    }

}
