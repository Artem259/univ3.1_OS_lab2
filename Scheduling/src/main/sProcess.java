package main;

public class sProcess {
    private int arrivaltime;
    private int cputime;
    private int ioblocking;
    private int cpudone;
    private int ionext;
    private int numblocked;

    public sProcess() {
        this.arrivaltime = 0;
        this.cputime = 0;
        this.ioblocking = 0;
        this.cpudone = 0;
        this.ionext = 0;
        this.numblocked = 0;
    }

    public sProcess(int arrivaltime, int cputime, int ioblocking, int cpudone, int ionext, int numblocked) {
        this.arrivaltime = arrivaltime;
        this.cputime = cputime;
        this.ioblocking = ioblocking;
        this.cpudone = cpudone;
        this.ionext = ionext;
        this.numblocked = numblocked;
    }

    public int getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(int arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public int getCputime() {
        return cputime;
    }

    public void setCputime(int cputime) {
        this.cputime = cputime;
    }

    public int getIoblocking() {
        return ioblocking;
    }

    public void setIoblocking(int ioblocking) {
        this.ioblocking = ioblocking;
    }

    public int getCpudone() {
        return cpudone;
    }

    public void setCpudone(int cpudone) {
        this.cpudone = cpudone;
    }

    public int getIonext() {
        return ionext;
    }

    public void setIonext(int ionext) {
        this.ionext = ionext;
    }

    public int getNumblocked() {
        return numblocked;
    }

    public void setNumblocked(int numblocked) {
        this.numblocked = numblocked;
    }
}
