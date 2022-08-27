package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {

    private MessageBus messageBus;
    private MicroService C3POmicroService;
    private MicroService hanSoloMicroservice;
    private AttackEvent attackEvent;
    private AttackEvent attackEvent2;
    private AttackEvent attackEvent3;
    private TerminateBroadcast broadcast;

    @BeforeEach
    void setUp() {
        LinkedList<Integer> ewoks_numbers = new LinkedList<>(Arrays.asList(1, 2));
        Attack attack = new Attack(ewoks_numbers, 100);
        this.messageBus = MessageBusImpl.getInstance();
        this.C3POmicroService = new C3POMicroservice();
        this.hanSoloMicroservice = new HanSoloMicroservice();
        this.attackEvent = new AttackEvent(attack);
        this.attackEvent2 = new AttackEvent(new Attack(ewoks_numbers, 100));
        this.attackEvent3 = new AttackEvent(new Attack(ewoks_numbers, 100));
        this.broadcast = new TerminateBroadcast();
        messageBus.register(C3POmicroService);
        messageBus.register(hanSoloMicroservice);
    }

    @AfterEach
    void tearDown(){
        messageBus.unregister(C3POmicroService);
        messageBus.unregister(hanSoloMicroservice);
    }

    @Test
    void complete() {
        messageBus.subscribeEvent(attackEvent.getClass(), C3POmicroService);
        Future<Boolean> future = messageBus.sendEvent(attackEvent);
        messageBus.complete(attackEvent, true);
        assertTrue(future.isDone());
    }

    @Test
    void sendBroadcast() {
        messageBus.subscribeBroadcast(broadcast.getClass(), C3POmicroService);
        messageBus.subscribeBroadcast(broadcast.getClass(), hanSoloMicroservice);
        messageBus.sendBroadcast(broadcast);
        try {
            Message message1 = messageBus.awaitMessage(C3POmicroService);
            Message message2 = messageBus.awaitMessage(hanSoloMicroservice);
            assertEquals(message1,message2);
        } catch (InterruptedException e) {
            fail("Test failed: " + e);
        }
    }

    @Test
    void sendEvent() {
        messageBus.subscribeEvent(attackEvent.getClass(), C3POmicroService);
        messageBus.subscribeEvent(attackEvent.getClass(), hanSoloMicroservice);
        messageBus.sendEvent(attackEvent);
        messageBus.sendEvent(attackEvent2);
        messageBus.sendEvent(attackEvent3);
        try {
            Message message = messageBus.awaitMessage(C3POmicroService);
            Message message3 = messageBus.awaitMessage(C3POmicroService);
            Message message2 = messageBus.awaitMessage(hanSoloMicroservice);
            assertEquals(message, attackEvent);
            assertEquals(message2, attackEvent2);
            assertEquals(message3, attackEvent3);
        } catch (InterruptedException e) {
            fail("Test failed: " + e);
        }
    }
}