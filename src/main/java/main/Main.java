package main;



public class Main {
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        t.setDaemon(true);
    }
    synchronized void foo() {
    }
}
