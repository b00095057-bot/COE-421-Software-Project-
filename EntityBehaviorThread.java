public class EntityBehaviorThread implements Runnable {
    // background thread that makes the world feel alive
    // also satisfies the course's multi-threading + synchronization requirement

    private final GameState state;
    private volatile boolean running = true; // volatile so the stop signal is seen across threads

    public EntityBehaviorThread(GameState state) {
        this.state = state;
    }

    // called from Main when the game ends so the thread exits cleanly
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(15000); // wait 15 seconds between ticks
            } catch (InterruptedException e) {
                break;
            }
            // synchronized block ensures no race with the main game loop
            synchronized (state) {
                // suspicion slowly rises the longer the player plays
                if (state.getTurnsPassed() % 5 == 0 && state.getTurnsPassed() > 0) {
                    state.updateSuspicion(2, "Passive monitoring");
                }
                // lingering in restricted zones raises the alarm
                String loc = state.getLocation();
                if (loc.equals("securityMaze") || loc.equals("memoryArchive")) {
                    state.updateAlarm(3, "Lingering in restricted zone");
                }
            }
        }
    }
}