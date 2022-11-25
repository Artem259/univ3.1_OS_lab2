// This file contains the main() function for the Scheduling
// simulation.  Init() initializes most of the variables by
// reading from a provided file.  SchedulingAlgorithm.Run() is
// called from main() to run the simulation.  Summary-Results
// is where the summary results are written, and Summary-Processes
// is where the process scheduling summary is written.

// Created by Alexander Reeder, 2001 January 06

package main;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Scheduling {
    private int processnum;
    private int meanDev;
    private int standardDev;
    private int runtime;
    private final ArrayList<sProcess> processVector;
    private final String processFile;
    private final String resultsFile;

    private void debug() {
        int i;
        System.out.println("processnum " + processnum);
        System.out.println("meandevm " + meanDev);
        System.out.println("standdev " + standardDev);
        int size = processVector.size();
        for (i = 0; i < size; i++) {
            sProcess process = processVector.get(i);
            System.out.println("process " + i + " " + process.getCputime() + " " + process.getIoblocking() + " " + process.getCpudone() + " " + process.getNumblocked());
        }
        System.out.println("runtime " + runtime);
    }

    public Scheduling(String configFile, String processFile, String resultFile) {
        this.processnum = 5;
        this.meanDev = 1000;
        this.standardDev = 100;
        this.runtime = 1000;
        this.processVector = new ArrayList<>();
        this.processFile = processFile;
        this.resultsFile = resultFile;


        File f = new File(configFile);
        if (!(f.exists())) {
            System.out.println("Scheduling: error, file '" + f.getName() + "' does not exist.");
            System.exit(-1);
        }
        if (!(f.canRead())) {
            System.out.println("Scheduling: error, read of " + f.getName() + " failed.");
            System.exit(-1);
        }
        System.out.println("Working...");

        String line;
        try (BufferedReader in = new BufferedReader(new FileReader(f))) {
            while ((line = in.readLine()) != null) {
                if (line.startsWith("numprocess")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    processnum = Common.s2i(st.nextToken());
                }
                if (line.startsWith("meandev")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    meanDev = Common.s2i(st.nextToken());
                }
                if (line.startsWith("standdev")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    standardDev = Common.s2i(st.nextToken());
                }
                if (line.startsWith("process")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    int arrivaltime = Common.s2i(st.nextToken());
                    int ioblocking = Common.s2i(st.nextToken());
                    double X = Common.R1();
                    while (X == -1.0) {
                        X = Common.R1();
                    }
                    X = X * standardDev;
                    int cputime = (int) X + meanDev;
                    processVector.add(new sProcess(arrivaltime, cputime, ioblocking, 0, 0, 0));
                }
                if (line.startsWith("runtime")) {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    runtime = Common.s2i(st.nextToken());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        if (processVector.size() < processnum) {
            int i = 0;
            while (processVector.size() < processnum) {
                double X = Common.R1();
                while (X == -1.0) {
                    X = Common.R1();
                }
                X = X * standardDev;
                int cputime = (int) X + meanDev;
                processVector.add(new sProcess(0, cputime,i*100,0,0,0));
                i++;
            }
        }
    }

    public void start() {
        SchedulingAlgorithm schedulingAlgorithm = new SchedulingAlgorithm(processFile);
        Results result = schedulingAlgorithm.run(runtime, processVector);

        int i;
        try (PrintStream out = new PrintStream(new FileOutputStream(resultsFile))) {
            out.println("Scheduling Type: " + result.getSchedulingType());
            out.println("Scheduling Name: " + result.getSchedulingName());
            out.println("Simulation Run Time: " + result.getCompuTime());
            out.println("Mean: " + meanDev);
            out.println("Standard Deviation: " + standardDev);
            out.println("Process #\tCPU Time\tIO Blocking\tCPU Completed\tCPU Blocked");
            for (i = 0; i < processVector.size(); i++) {
                sProcess process = processVector.get(i);
                out.print(i);
                if (i < 100) { out.print("\t\t"); } else { out.print("\t"); }
                out.print(process.getCputime());
                if (process.getCputime() < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
                out.print(process.getIoblocking());
                if (process.getIoblocking() < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
                out.print(process.getCpudone());
                if (process.getCpudone() < 100) { out.print(" (ms)\t\t"); } else { out.print(" (ms)\t"); }
                out.println(process.getNumblocked() + " times");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        System.out.println("Completed.");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: 'java Scheduling <INIT FILE>'");
            System.exit(-1);
        }
        new Scheduling(args[0], "Summary-Processes", "Summary-Results").start();
    }
}
