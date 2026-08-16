    static double soma = 0;
    private static final Object lockSoma = new Object();


    private static double tarefaPesadaVariavelEficiente() {
        String nomeAtual = Thread.currentThread().getName();
        System.out.println("Rodando na thread: " + nomeAtual);
        double soma = 0;

        for (int i = 0; i < 100000000; i++) {
                soma += i;
        }

        return soma;
    }

    private static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown(); // Reject incoming tasks, complete existing ones
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Force kill running tasks if timeout expires
            }
        } catch (InterruptedException ex) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int totalTarefas = 4;

        System.out.println("Executando " + totalTarefas + " tarefas pesadas...\n");

        // ==========================================
        // 1. EXECUÇÃO SEQUENCIAL
        // ==========================================
        long inicioSeq = System.currentTimeMillis();
        double somaTotal = 0;
        for (int i = 0; i < totalTarefas; i++) {
            somaTotal = somaTotal + tarefaPesadaVariavelEficiente();
        }

        long tempoSeq = System.currentTimeMillis() - inicioSeq;
        System.out.println("Soma Total: " + somaTotal);
        System.out.println("Tempo Sequencial: " + tempoSeq + " ms");

        System.out.println("------------------------------------------------------");
        // ==========================================
        // 2. EXECUÇÃO PARALELA (Usando Thread)
        // ==========================================
        soma = 0;

        long inicioPar = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(totalTarefas);

        Callable<Double> asyncTask = () -> {
            return tarefaPesadaVariavelEficiente();
        };

        Future<Double> futureResult = executor.submit(asyncTask);

        try {
            // 4. Retrieve the result (blocks execution if task is incomplete)
            Double somaTotalD = 0.0;
            for (int i = 0; i < totalTarefas; i++) {
                somaTotalD = somaTotalD + futureResult.get();
            }

            System.out.println("Soma Total: " + somaTotalD);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            // 5. Always shut down the executor service to free resources
            shutdownExecutor(executor);
        }


        long tempoPar = System.currentTimeMillis() - inicioPar;
        System.out.println("Tempo Paralelo:   " + tempoPar + " ms\n");

        // Resultado
        System.out.printf("O código paralelo foi %.1fx mais rápido!%n", (double) tempoSeq / tempoPar);
    }
