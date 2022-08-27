package bgu.spl.mics.application;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		Simulator simulator = new Simulator(args[0], args[1]);
		simulator.simulate();
	}
}
