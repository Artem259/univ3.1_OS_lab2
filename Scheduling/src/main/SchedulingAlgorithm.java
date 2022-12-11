// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

package main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class SchedulingAlgorithm {
    private final String processFile;
    private int comptime;

    public SchedulingAlgorithm(String processFile) {
        this.processFile = processFile;
    }

    private void printProcess(PrintStream out, sProcess process, String action) {
        out.println(comptime + " Process: " + process.getId() + " "
                + action + "... ( arrival_time:" + process.getArrivaltime()
                + ", io_blocking:" + process.getIoblocking() + ", done:"
                + process.getCpudone() + "/" + process.getCputime() + " )");
    }

    private sProcess getNextProcess(PriorityQueue<sProcess> processesQueue) {
        return processesQueue.poll();
    }

    public Results run(int runtime, ArrayList<sProcess> processVector) {
        comptime = 0;
        sProcess currentProcess = null;
        int size = processVector.size();
        int completed = 0;
        int nextArrivalIndex = 0;
        Results result = new Results();
        PriorityQueue<sProcess> processesQueue = new PriorityQueue<>(
                Comparator.comparing(sProcess::getIoblocking));

        processVector.sort(Comparator.comparing(sProcess::getArrivaltime));

        result.setSchedulingType("Batch (Nonpreemptive)");
        result.setSchedulingName("First-Come First-Served");
        try (PrintStream out = new PrintStream(new FileOutputStream(processFile))) {
            while (comptime < runtime) {
                if (nextArrivalIndex < size) {
                    while (comptime == processVector.get(nextArrivalIndex).getArrivaltime()) {
                        printProcess(out, processVector.get(nextArrivalIndex), "arrived");
                        processesQueue.add(processVector.get(nextArrivalIndex));
                        nextArrivalIndex++;
                        if (nextArrivalIndex == size) {
                            break;
                        }
                    }
                }
                if (currentProcess == null && !processesQueue.isEmpty()) {
                    currentProcess = getNextProcess(processesQueue);
                    printProcess(out, currentProcess, "registered");
                }
                if (currentProcess != null) {
                    if (currentProcess.getCpudone() == currentProcess.getCputime()) {
                        printProcess(out, currentProcess, "completed");
                        completed++;
                        if (completed == size) {
                            result.setCompuTime(comptime);
                            out.println("Completed.");
                            return result;
                        }
                        currentProcess = null;
                        continue;
                    }
                    if (currentProcess.getIoblocking() == currentProcess.getIonext()) {
                        printProcess(out, currentProcess, "I/O blocked");
                        currentProcess.setNumblocked(currentProcess.getNumblocked() + 1);
                        currentProcess.setIonext(0);

                        if (!processesQueue.isEmpty()) {
                            sProcess previousProcess = currentProcess;
                            currentProcess = getNextProcess(processesQueue);
                            printProcess(out, currentProcess, "registered");
                            processesQueue.add(previousProcess);
                        } else {
                            processesQueue.add(currentProcess);
                            currentProcess = null;
                        }
                    }
                }
                if (currentProcess != null) {
                    currentProcess.setCpudone(currentProcess.getCpudone() + 1);
                    if (currentProcess.getIoblocking() > 0) {
                        currentProcess.setIonext(currentProcess.getIonext() + 1);
                    }
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
