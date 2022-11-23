package main;

public class Results {
    private String schedulingType;
    private String schedulingName;
    private int compuTime;

    public Results() {
        this.schedulingType = "null";
        this.schedulingName = "null";
        this.compuTime = 0;
    }

    public Results(String schedulingType, String schedulingName, int compuTime) {
        this.schedulingType = schedulingType;
        this.schedulingName = schedulingName;
        this.compuTime = compuTime;
    }

    public String getSchedulingType() {
        return schedulingType;
    }

    public void setSchedulingType(String schedulingType) {
        this.schedulingType = schedulingType;
    }

    public String getSchedulingName() {
        return schedulingName;
    }

    public void setSchedulingName(String schedulingName) {
        this.schedulingName = schedulingName;
    }

    public int getCompuTime() {
        return compuTime;
    }

    public void setCompuTime(int compuTime) {
        this.compuTime = compuTime;
    }
}
