
private static final Map<String, Integer>
        pedidosPorCliente =
        new HashMap<>();

public static void registrar(String cliente) {

    pedidosPorCliente.merge(
            cliente,
            1,
            Integer::sum
    );
}

public static void main(String[] args)
        throws InterruptedException {

    Thread t1 = new Thread(() -> {
        for (int i = 0; i < 10_000; i++) {
            registrar("Ana");
        }
    });

    Thread t2 = new Thread(() -> {
        for (int i = 0; i < 10_000; i++) {
            registrar("Ana");
        }
    });

    t1.start();
    t2.start();

    t1.join();
    t2.join();

    System.out.println(
            pedidosPorCliente.get("Ana")
    );
}
