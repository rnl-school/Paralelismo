

    private static int totalTarefas = 4;

    private static double tarefaPesadaVariavelEficiente(int numeroTafera, boolean paralelo) {
        String nomeAtual = Thread.currentThread().getName();
        System.out.println("Rodando na thread: " + nomeAtual);
        double soma = 0;

        if (numeroTafera == 2 && paralelo) {
            throw new RuntimeException(
                    "Erro na tarefa " + numeroTafera
            );
        }

        for (int i = 0; i < 100000000; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return 0;
            }
                soma += i;
        }

        return soma;
    }

    private static Double SequencialCalculate()
    {
        double somaTotal = 0.0;
        for (int i = 0; i < totalTarefas; i++) {
            somaTotal = somaTotal + tarefaPesadaVariavelEficiente(i, false);
        }
        return somaTotal;
    }

    private static double SomaParelelaEstruturada()
            throws InterruptedException {

        double somaTotal = 0.0;

        try (var scope = StructuredTaskScope.open()) {


            Supplier<Double>[] tarefas = new Supplier[totalTarefas];

            for (int i = 0; i < totalTarefas; i++) {

                final int numeroTarefa = i;
                tarefas[i] = scope.fork(() ->
                        tarefaPesadaVariavelEficiente(numeroTarefa, true));
            }

            System.out.println("Todas as tarefas foram iniciadas.");

            scope.join();

            for (Supplier<Double> tarefa : tarefas) {
                somaTotal += tarefa.get();
            }

            System.out.println(
                    "Todas as tarefas terminaram com sucesso."
            );

        } catch (StructuredTaskScope.FailedException e) {

            System.out.println("\n!!! UMA TAREFA FALHOU !!!");

            System.out.println("Erro: " + e.getCause().getMessage());

            somaTotal = 0.0;

            System.out.println("O escopo foi encerrado.");
        }

        return somaTotal;
    }

    public static Double SomaParalelaNaoEstruturada()
    {
        List<CompletableFuture<Double>> futures =
                new ArrayList<>();

        for (int i = 0; i < totalTarefas; i++) {

            final int numeroTarefa = i;

            CompletableFuture<Double> future =
                    CompletableFuture.supplyAsync(() -> {
                        return tarefaPesadaVariavelEficiente(numeroTarefa, true);
                    });

            futures.add(future);
        }

        double somaTotalParalela = 0.0;

        for (CompletableFuture<Double> future : futures) {
            try {
                somaTotalParalela += future.join();
            } catch (CompletionException e) {

                System.out.println(
                        "Uma tarefa falhou: " + e.getCause().getMessage()
                );
            }
        }

        System.out.println("Soma Total: " + somaTotalParalela);
        return somaTotalParalela;
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Executando " + totalTarefas + " tarefas pesadas...\n");
        long inicioSeq = System.currentTimeMillis();
        double somaTotal = 0;
        somaTotal = SequencialCalculate();

        long tempoSeq = System.currentTimeMillis() - inicioSeq;
        System.out.println("Soma Total Sequencial: " + somaTotal);
        System.out.println("Tempo Sequencial: " + tempoSeq + " ms");

        System.out.println("------------------------------------------------------");

        long inicioPar = System.currentTimeMillis();

        somaTotal = SomaParalelaNaoEstruturada();

        long tempoPar = System.currentTimeMillis() - inicioPar;
        System.out.println("Soma Total paralela nao estruturada: " + somaTotal);
        System.out.println("Tempo Paralelo nao estruturado:   " + tempoPar + " ms\n");

        inicioPar = System.currentTimeMillis();

        somaTotal = SomaParelelaEstruturada();

        tempoPar = System.currentTimeMillis() - inicioPar;
        System.out.println("Soma Total paralela estruturada: " + somaTotal);
        System.out.println("Tempo Paralelo estruturado:   " + tempoPar + " ms\n");

    }
