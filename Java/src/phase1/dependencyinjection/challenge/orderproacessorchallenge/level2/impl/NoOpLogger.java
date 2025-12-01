package phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.impl;

import phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.service.Logger;

public class NoOpLogger implements Logger {
    @Override
    public void log(String message) {
        // silent — no I/O, no cost
    }
}
