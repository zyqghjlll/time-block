package com.calm.tools.botfy.domain.timer;

public abstract class Timer {

    private int workTime;
    private int restTime;
    private boolean countDown;

    public Timer(int workTime, int restTime, boolean countDown) {
        this.workTime = workTime;
        this.restTime = restTime;
        this.countDown = countDown;
    }

    public void start() {
        timing();
        finish();
    }

    public void pause() {

    }

    public void interrupt() {

    }

    private boolean timing() {
        return false;
    }

    protected abstract void finish();
}
