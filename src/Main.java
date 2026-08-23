

    private static int quantidadeTarefas = 100;

    private static void VirtualThread() {
        long inicio = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {

            for (int i = 0; i < quantidadeTarefas; i++) {

                int tarefa = i;

                executor.submit(() -> {

                    System.out.println(
                            "Tarefa " + tarefa +
                                    " - " + Thread.currentThread()
                    );

                    // Simulando uma chamada HTTP
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println(
                            "Tarefa " + tarefa + " concluída"
                    );
                });
            }
        }

        long tempo = System.currentTimeMillis() - inicio;
        System.out.println("Tempo Virtual Thread: " + tempo + " ms");
    }

    private static void ThreadComum() {
        long inicio = System.currentTimeMillis();

        try (ExecutorService executor = Executors.newFixedThreadPool(10)) {

            for (int i = 0; i < quantidadeTarefas; i++) {

                int tarefa = i;

                executor.submit(() -> {

                    System.out.println(
                            "Tarefa " + tarefa +
                                    " - " + Thread.currentThread()
                    );

                    // Simulando uma chamada HTTP
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    System.out.println(
                            "Tarefa " + tarefa + " concluída"
                    );
                });
            }
        }

        long tempo = System.currentTimeMillis() - inicio;
        System.out.println("Tempo Thread Comum: " + tempo + " ms");
    }


    public static void main(String[] args){

            ThreadComum();
            VirtualThread();
    }
