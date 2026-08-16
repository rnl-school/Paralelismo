

    private static int totalTarefas = 4;

    private static double tarefaPesadaVariavelEficiente() {
        String nomeAtual = Thread.currentThread().getName();
        System.out.println("Rodando na thread: " + nomeAtual);
        double soma = 0;

        for (int i = 0; i < 100000000; i++) {
                soma += i;
        }

        return soma;
    }

    private static Double Calculate()
    {
        double somaTotal = 0.0;
        for (int i = 0; i < totalTarefas; i++) {
            somaTotal = somaTotal + tarefaPesadaVariavelEficiente();
        }
        return somaTotal;
    }

    public static void main(String[] args) throws InterruptedException {


        System.out.println("Executando " + totalTarefas + " tarefas pesadas...\n");

        // ==========================================
        // 1. EXECUÇÃO SEQUENCIAL
        // ==========================================
        long inicioSeq = System.currentTimeMillis();
        double somaTotal = 0;
        somaTotal = Calculate();

        long tempoSeq = System.currentTimeMillis() - inicioSeq;
        System.out.println("Soma Total: " + somaTotal);
        System.out.println("Tempo Sequencial: " + tempoSeq + " ms");

        System.out.println("------------------------------------------------------");
        // ==========================================
        // 2. EXECUÇÃO PARALELA (Usando Thread)
        // ==========================================

        long inicioPar = System.currentTimeMillis();

        List<CompletableFuture<Double>> futures =
                new ArrayList<>();

        for (int i = 0; i < totalTarefas; i++) {

            CompletableFuture<Double> future =
                    CompletableFuture.supplyAsync(
                            () -> tarefaPesadaVariavelEficiente()
                    );

            futures.add(future);
        }

        double somaTotalParalela = 0.0;

        for (CompletableFuture<Double> future : futures) {
            somaTotalParalela += future.join();
        }

        long tempoPar = System.currentTimeMillis() - inicioPar;
        System.out.println("Soma Total: " + somaTotalParalela);
        System.out.println("Tempo Paralelo:   " + tempoPar + " ms\n");

        // Resultado
        System.out.printf("O código paralelo foi %.1fx mais rápido!%n", (double) tempoSeq / tempoPar);
    }
