public class Delay {
    static void delay() {
        int delay = Integer.getInteger("test.delay", 0);
        if (delay == 0) {
            return;
        }
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
