package xinrui.cloud.util;

public class TimeInner {
    private volatile boolean canContinue = true;

    public boolean isCanContinue() {
        return canContinue;
    }

    public void setCanContinue(boolean canContinue) {
        this.canContinue = canContinue;
    }

    public synchronized void stop() {
        setCanContinue(false);
    }
}
