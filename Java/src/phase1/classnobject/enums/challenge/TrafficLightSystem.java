package phase1.classnobject.enums.challenge;

enum Lights {
    GREEN(25),
    YELLOW(5),
    RED(30);

    private final int count;

    Lights(int count) {
        this.count = count;
    }

    int getCount() {
        return count;
    }
}

record LightModel(int counter, Lights nextState) {}

class TrafficSystemController {
    private int counter;
    private Lights currentState;
    private LightModel stateModel;

    public TrafficSystemController(Lights initialLight) {
        this.counter = initialLight.getCount();
        this.currentState = initialLight;
        this.stateModel = switch (currentState) {
            case GREEN -> new LightModel(counter, Lights.YELLOW);
            case YELLOW -> new LightModel(counter, Lights.RED);
            case RED -> new LightModel(counter, Lights.GREEN);
        };
    }

    public void runCycle() {
        System.out.print(currentStatus());

        // Deterministic countdown (no threads)
        while (counter > 0) {
            counter--;
        }

        // Transition to next state
        currentState = stateModel.nextState();
        counter = currentState.getCount();

        // Rebuild next state model
        stateModel = switch (currentState) {
            case GREEN -> new LightModel(counter, Lights.YELLOW);
            case YELLOW -> new LightModel(counter, Lights.RED);
            case RED -> new LightModel(counter, Lights.GREEN);
        };

        System.out.print(nextStatus());
        System.out.println();
    }

    private String currentStatus() {
        return """
        ====================================================
        The Current Signal : %s | counter :  %d
        """.formatted(currentState, counter);
    }

    private String nextStatus() {
        return """
        
        The Next Signal : %s | counter :  %d
        ====================================================""".formatted(stateModel.nextState(), stateModel.counter());
    }
}

public class TrafficLightSystem {
    public static void main(String[] args) {
        var controller = new TrafficSystemController(Lights.GREEN);

        controller.runCycle(); // GREEN → YELLOW
        controller.runCycle(); // YELLOW → RED
        controller.runCycle(); // RED → GREEN
    }
}