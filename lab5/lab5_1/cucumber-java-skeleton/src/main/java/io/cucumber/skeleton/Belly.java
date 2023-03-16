package io.cucumber.skeleton;

public class Belly {
    private static final int LOSS_PER_HOUR = 20;
    private int cukes;

    public void eat(int cukes) {
        this.cukes += cukes;
    }

    public void wait(int hour) {
        cukes -= LOSS_PER_HOUR * hour;
    }

    public boolean growls() {
        return cukes < 25;
    }
}
