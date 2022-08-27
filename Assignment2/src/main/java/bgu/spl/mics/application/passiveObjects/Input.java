package bgu.spl.mics.application.passiveObjects;

/**
 * Input is an object which holds the information given to the program by the json file.
 */
public class Input {
	private Attack[] attacks;
	int R2D2;
	int Lando;
	int Ewoks;

	/**
	 * @return number of ewoks
	 */
	public int getEwoks() {
		return Ewoks;
	}

	/**
	 * sets number of ewoks
	 * @param ewoks number of ewoks
	 */
	public void setEwoks(int ewoks) {
		Ewoks = ewoks;
	}

	/**
	 *
	 * @return duration of Lando's sleep
	 */
	public int getLando() {
		return Lando;
	}

	/**
	 * set lando's sleep to different time
	 * @param lando how much Lando's sleep
	 */
	public void setLando(int lando) {
		Lando = lando;
	}

	/**
	 * @return duration od R2D2's sleep
	 */
	public int getR2D2() {
		return R2D2;
	}

	/**
	 * set r2d2's sleep to different time
	 * @param r2d2 time to sleep
	 */
	public void setR2D2(int r2d2) {
		R2D2 = r2d2;
	}

	/**
	 * @return attacks the leia should send
	 */
	public Attack[] getAttacks() {
		return attacks;
	}

	/**
	 * set attacks to different attacks
	 * @param attacks to set
	 */
	public void setAttacks(Attack[] attacks) {
		this.attacks = attacks;
	}
}
