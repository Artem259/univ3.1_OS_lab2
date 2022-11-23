// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class SchedulingAlgorithm {
    private final String processFile;

    public SchedulingAlgorithm(String processFile) {
        this.processFile = processFile;
    }

    public Results run(int runtime, ArrayList<sProcess> processVector) {
        int comptime = 0;
        int currentProcess = 0;
        int previousProcess;
        int size = processVector.size();
        int completed = 0;
        Results result = new Results();

        result.setSchedulingType("Batch (Nonpreemptive)");
        result.setSchedulingName("First-Come First-Served");
        try (PrintStream out = new PrintStream(new FileOutputStream(processFile))) {
            sProcess process = processVector.get(currentProcess);
            out.println("Process: " + currentProcess + " registered... (" + process.getCputime() + " " + process.getIoblocking() + " " + process.getCpudone() + " " + process.getCpudone() + ")");
            while (comptime < runtime) {
                if (process.getCpudone() == process.getCputime()) {
                    completed++;
                    out.println("Process: " + currentProcess + " completed... (" + process.getCputime() + " " + process.getIoblocking() + " " + process.getCpudone() + " " + process.getCpudone() + ")");
                    if (completed == size) {
                        result.setCompuTime(comptime);
                        out.close();
                        return result;
                    }
                    for (int i = size - 1; i >= 0; i--) {
                        process = processVector.get(i);
                        if (process.getCpudone() < process.getCputime()) {
                            currentProcess = i;
                        }
                    }
                    process = processVector.get(currentProcess);
                    out.println("Process: " + currentProcess + " registered... (" + process.getCputime() + " " + process.getIoblocking() + " " + process.getCpudone() + " " + process.getCpudone() + ")");
                }
                if (process.getIoblocking() == process.getIonext()) {
                    out.println("Process: " + currentProcess + " I/O blocked... (" + process.getCputime() + " " + process.getIoblocking() + " " + process.getCpudone() + " " + process.getCpudone() + ")");
                    process.setNumblocked(process.getNumblocked() + 1);
                    process.setIonext(0);
                    previousProcess = currentProcess;
                    for (int i = size - 1; i >= 0; i--) {
                        process = processVector.get(i);
                        if (process.getCpudone() < process.getCputime() && previousProcess != i) {
                            currentProcess = i;
                        }
                    }
                    process = processVector.get(currentProcess);
                    out.println("Process: " + currentProcess + " registered... (" + process.getCputime() + " " + process.getIoblocking() + " " + process.getCpudone() + " " + process.getCpudone() + ")");
                }
                process.setCpudone(process.getCpudone() + 1);
                if (process.getIoblocking() > 0) {
                    process.setIonext(process.getIonext() + 1);
                }
                comptime++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        result.setCompuTime(comptime);
        return result;
    }
}
