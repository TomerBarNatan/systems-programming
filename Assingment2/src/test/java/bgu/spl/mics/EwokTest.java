package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Ewok;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {

    private Ewok ewok;

    @BeforeEach
    void setUp() {
        ewok = new Ewok(2);
    }

    @Test
    void acquire() {
        ewok.acquire();
        assertFalse(ewok.isAvailable());
    }

    @Test
    void release() {
        ewok.acquire();
        ewok.release();
        assertTrue(ewok.isAvailable());
    }

    @Test
    void isAvailable() {
        ewok.acquire();
        assertFalse(ewok.isAvailable());
        ewok.release();
        assertTrue(ewok.isAvailable());
    }

    @Test
    void getSerialNumber() {
        assertEquals(ewok.getSerialNumber(),2);
    }
}