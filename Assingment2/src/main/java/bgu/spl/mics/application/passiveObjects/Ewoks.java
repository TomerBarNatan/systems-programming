package bgu.spl.mics.application.passiveObjects;

import java.util.HashMap;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private final HashMap<Integer, Ewok> ewoks_map;

    /**
     * Create an Ewoks Holder in order to make sure the object is singleton.
     */
    private static class EwoksHolder {
        private static final Ewoks instance = new Ewoks();
    }
    /**
     * @return the only instance of Ewoks object
     */
    public static Ewoks getInstance() {
        return EwoksHolder.instance;
    }

    /**
     * Constructor.
     */
    public Ewoks() {
        this.ewoks_map = new HashMap<>();
    }

    /**
     * Gets an integer which represents {@num_of_ewoks} to create.
     * <p>
     * @param num_of_ewoks 	The number of ewoks to put inside the hash-map.
     */
    public void initialize_ewoks_map(int num_of_ewoks) {
        for (int i = 1; i <= num_of_ewoks; i++) {
            ewoks_map.put(i, new Ewok(i));
        }
    }

    /**
     * Gets a list of ewok's {@code serials} which need to be acquire by Han-Solo or C3PO.
     * <p>
     * @param serials, list of serial numbers
     */
    public void request(List<Integer> serials) {
        serials.sort(Integer::compareTo);
        for (Integer serial : serials) {
            synchronized (serial) {
                while (!ewoks_map.get(serial).isAvailable()) {
                    try {
                        synchronized (this) {
                            wait(100);
                        }
                    } catch (InterruptedException error) {
                        error.printStackTrace();
                    }

                }
                ewoks_map.get(serial).acquire();
            }
        }
    }

    /**
     * Gets a list of ewok's {@code serials} which need to be release by Han-Solo or C3PO.
     * <p>
     * @param serials, list of serial numbers
     */
    public void release(List<Integer> serials){
        for(Integer serial: serials){
            ewoks_map.get(serial).release();
        }
        synchronized (this) {
            notifyAll();
        }
    }
}
