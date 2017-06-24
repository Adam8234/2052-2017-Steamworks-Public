package com.first.team2052.lib;

public class FlipFlopLatch {
    private boolean state = false;
    private boolean toggle = false;

    public FlipFlopLatch() {
    }

    public FlipFlopLatch(boolean defaultState) {
        state = defaultState;
    }

    protected void flip() {
        state = !state;
    }

    public boolean get() {
        return state;
    }

    public void update(boolean button) {
        if (button && !toggle)
            flip();
        toggle = button;
    }

    public void set(boolean state) {
        this.state = state;
    }
}
