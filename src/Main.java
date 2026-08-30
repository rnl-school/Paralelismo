
private static int valor = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                valor++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                valor++;
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(valor);
    }
