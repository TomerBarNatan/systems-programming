package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Simulator class is responsible for the program initialization and logic, called by main class.
 */
public class Simulator {
    private final Input input;
    private final LinkedList<Thread> thread_list;
    private final Ewoks ewoks;
    private final Diary diary;
    private final String output_path;

    /**
     * Constructor
     * @param input_path location of json input file
     * @param output_path location of saved output file
     */
    public Simulator(String input_path, String output_path){
        this.input = read_json(input_path);
        this.thread_list = initialize_thread_list();
        this.ewoks = Ewoks.getInstance();
        this.diary = Diary.getInstance();
        this.output_path = output_path;
    }

    /**
     * simulates the program flow.
     * initializes the ewoks map, sets start time and invokes each thread to start acting
     */
    public void simulate(){
        ewoks.initialize_ewoks_map(input.getEwoks());
        long start_time = System.currentTimeMillis();
        for (Thread thread: thread_list){
            thread.start();
            try {
                Thread.sleep(10);
            } catch (InterruptedException error){
                error.printStackTrace();
            }
        }

        for (Thread thread: thread_list){
            try {
                thread.join();
            }catch (InterruptedException error){
                error.printStackTrace();
            }
        }
        write_output(start_time);
    }

    private static Input read_json(String file_path){
        try {
            return JsonInputReader.getInputFromJson(file_path);
        } catch (IOException e) {
            return null;
        }
    }

    private LinkedList<Thread> initialize_thread_list(){
        LinkedList<Thread> output = new LinkedList<>();

        output.add(new Thread(new HanSoloMicroservice()));
        output.add(new Thread(new C3POMicroservice()));
        output.add(new Thread(new LeiaMicroservice(input.getAttacks())));
        output.add(new Thread(new LandoMicroservice(input.getLando())));
        output.add(new Thread(new R2D2Microservice(input.getR2D2())));

        return output;
    }

    private void write_output(long start_time){
        try {
            Writer writer = new FileWriter(output_path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(diary_output(start_time), writer);
            writer.close();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private Map<String, Long> diary_output(long start_time){
        Map<String, Long> diary_output = new LinkedHashMap<>();

        diary_output.put("totalAttacks", (long) diary.getTotalAttacks());
        diary_output.put("HanSoloFinish", diary.getHanSoloFinish() - start_time);
        diary_output.put("C3POFinish", diary.getC3POFinish() - start_time);
        diary_output.put("R2D2Deactivate", diary.getR2D2Deactivate() - start_time);
        diary_output.put("LeiaTerminate", diary.getLeiaTerminate() - start_time);
        diary_output.put("HanSoloTerminate", diary.getHanSoloTerminate() - start_time);
        diary_output.put("C3POTerminate", diary.getC3POTerminate() - start_time);
        diary_output.put("R2D2Terminate", diary.getR2D2Terminate() - start_time);
        diary_output.put("LandoTerminate", diary.getLandoTerminate() - start_time);

        return diary_output;
    }
}


