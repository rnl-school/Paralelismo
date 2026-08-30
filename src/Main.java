
private static final List<Pedido> pedidos =
        new CopyOnWriteArrayList<>();

public static void adicionar(Pedido pedido) {
    pedidos.add(pedido);
}

public static void main(String[] args)
        throws InterruptedException {

    Thread produtor1 = new Thread(() -> {
        for (int i = 0; i < 10_000; i++) {
            adicionar(
                    new Pedido(i, "Cliente-A")
            );
        }
    });

    Thread produtor2 = new Thread(() -> {
        for (int i = 10_000; i < 20_000; i++) {
            adicionar(
                    new Pedido(i, "Cliente-B")
            );
        }
    });

    produtor1.start();
    produtor2.start();

    produtor1.join();
    produtor2.join();

    System.out.println(
            "Pedidos: " + pedidos.size()
    );
}
