package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

/**
 * DeactivationEvent is kind of event sent once by Leia and subscribed by R2D2.
 * It informs R2D2 that all the attacks were finish and its time to deactivate the shield generator.
 **/
public class DeactivationEvent implements Event<Boolean> {
}
