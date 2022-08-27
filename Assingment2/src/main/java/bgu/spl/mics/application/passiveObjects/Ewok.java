package bgu.spl.mics.application.passiveObjects;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
	private int serialNumber;
	private boolean available;

    /**
     * Constructor.
     */
  public Ewok(int serialNumber){
      this.serialNumber = serialNumber;
      this.available = true;
  }
    /**
     * Acquires an Ewok
     */
    public void acquire() {
		this.available = false;
    }

    /**
     * release an Ewok
     */
    public void release() {
    	this.available = true;
    }

    /**
     * @return true if this ewok is available
     */
    public boolean isAvailable() {
        return this.available;
    }

    /**
     * @return the serial number of the ewok
     */
    public int getSerialNumber(){
        return this.serialNumber;
    }
}
