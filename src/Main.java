
private static int valor = 0;

    private synchronized static void incrementar()
    {
        valor++;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                incrementar();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100_000; i++) {
                incrementar();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(valor);
    }
