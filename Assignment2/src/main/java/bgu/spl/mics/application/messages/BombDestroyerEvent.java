package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

/**
 * BombDestroyedEvent is kind of event sent once by Leia and subscribed by Lando.
 * It informs Lando that it is time for him to bomb the star destroyer
 **/
public class BombDestroyerEvent implements Event<Boolean> {
}
