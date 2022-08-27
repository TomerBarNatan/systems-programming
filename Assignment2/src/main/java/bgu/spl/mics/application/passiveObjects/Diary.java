package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private final AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;
    private long HanSoloTerminate;

    /**
     * Create an Diary Holder in order to make sure the object is singleton.
     */
    private static class DiaryHolder {
        private static final Diary instance = new Diary();
    }

    /**
     * Constructor.
     */
    public Diary(){
        this.totalAttacks = new AtomicInteger(0);
        this.HanSoloFinish = 0;
        this.C3POFinish = 0;
        this.R2D2Deactivate = 0;
        this.LeiaTerminate = 0;
        this.C3POTerminate = 0;
        this.R2D2Terminate = 0;
        this.LandoTerminate = 0;
        this.HanSoloTerminate = 0;
    }

    /**
     * @return the only instance of Diary object
     */
    public static Diary getInstance() {
        return DiaryHolder.instance;
    }

    /**
     * Increments the number of total attacks by one.
     */
    public void incTotalAttacks(){
        this.totalAttacks.incrementAndGet();
    }

    /**
     * @return totalAttacks
     */
    public int getTotalAttacks() {
        return this.totalAttacks.get();
    }

    /**
     * @return the last time Han-Solo finished an attack.
     */
    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    /**
     * @return the last time C3PO finished an attack.
     */
    public long getC3POFinish() {
        return C3POFinish;
    }

    /**
     * @return the time when R2D2 deactivated the shield generator
     */
    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    /**
     * @return the time when Leia was terminated
     */
    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    /**
     * @return the time when C3PO was terminated
     */
    public long getC3POTerminate() {
        return C3POTerminate;
    }

    /**
     * @return the time when R2D2 was terminated
     */
    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    /**
     * @return the time when Lando was terminated
     */
    public long getLandoTerminate() {
        return LandoTerminate;
    }

    /**
     * @return the time when Han-Solo was terminated
     */
    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    /**
     * Set the {@HanSoloTerminate} field
     * @param hanSoloTerminate the time when han solo was terminate
     */
    public void setHanSoloTerminate(long hanSoloTerminate) {
        HanSoloTerminate = hanSoloTerminate;
    }

    /**
     * Set the {@HanSoloFinish} field
     * @param hanSoloFinish the time when han solo finished.
     */
    public void setHanSoloFinish(long hanSoloFinish) {
        HanSoloFinish = hanSoloFinish;
    }

    /**
     * Set the {@C3POFinish} field
     * @param c3POFinish the time when C3PO finished.
     */
    public void setC3POFinish(long c3POFinish) {
        C3POFinish = c3POFinish;
    }

    /**
     * Set the {@R2D2Deactivate} field
     * @param r2D2Deactivate the time when R2D2 deactivated the shield generator.
     */
    public void setR2D2Deactivate(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }

    /**
     * Set the {@LeiaTerminate} field
     * @param leiaTerminate the time when Leia was terminate
     */
    public void setLeiaTerminate(long leiaTerminate) {
        LeiaTerminate = leiaTerminate;
    }

    /**
     * Set the {@C3POTerminate} field
     * @param c3POTerminate the time when C3PO was terminate
     */
    public void setC3POTerminate(long c3POTerminate) {
        C3POTerminate = c3POTerminate;
    }

    /**
     * Set the {@R2D2Terminate} field
     * @param r2D2Terminate the time when R2D2 was terminate
     */
    public void setR2D2Terminate(long r2D2Terminate) {
        R2D2Terminate = r2D2Terminate;
    }

    /**
     * Set the {@LandoTerminate} field
     * @param landoTerminate the time when Lando was terminate
     */
    public void setLandoTerminate(long landoTerminate) {
        LandoTerminate = landoTerminate;
    }
}
