package great.sachin.timer_upgraded;

public class DataModel {
    private String taskName;
    private long totalTimeInSec;
    private long timeLeft;

    public DataModel(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getTotalTimeInSec() {
        return totalTimeInSec;
    }

    public void setTotalTimeInSec(long totalTimeInSec) {
        this.totalTimeInSec = totalTimeInSec;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }
}
