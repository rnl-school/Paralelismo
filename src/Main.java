import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;



private static final int totalTarefas = 4;

private static double tarefaPesadaVariavelEficiente(
        int numeroTarefa,
        boolean excecao) {

    Thread thread = Thread.currentThread();

    System.out.println(
            "Tarefa " + numeroTarefa +
                    " -> Thread: " + thread
    );

    if (numeroTarefa == 2 && excecao) {
        throw new RuntimeException(
                "Erro na tarefa " + numeroTarefa
        );
    }

    try {
        // Simula uma operação bloqueante:
        // banco de dados, API REST, arquivo etc.
        Thread.sleep(1000);

    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return 0;
    }

    return 100.0;
}

// ==========================================================
// 1. EXECUÇÃO SEQUENCIAL
// ==========================================================

private static double executarSequencial() {

    double somaTotal = 0.0;

    for (int i = 0; i < totalTarefas; i++) {

        somaTotal +=
                tarefaPesadaVariavelEficiente(i, false);
    }

    return somaTotal;
}


// ==========================================================
// 2. COMPLETABLE FUTURE
// ==========================================================

private static double executarCompletableFuture() {

    List<CompletableFuture<Double>> futures =
            new ArrayList<>();

    for (int i = 0; i < totalTarefas; i++) {

        final int numeroTarefa = i;

        CompletableFuture<Double> future =
                CompletableFuture.supplyAsync(() ->
                        tarefaPesadaVariavelEficiente(
                                numeroTarefa,
                                false
                        )
                );

        futures.add(future);
    }

    double somaTotal = 0.0;

    for (CompletableFuture<Double> future : futures) {

        somaTotal += future.join();
    }

    return somaTotal;
}


// ==========================================================
// 3. STRUCTURED CONCURRENCY + VIRTUAL THREADS
// ==========================================================

private static double executarEstruturado()
        throws InterruptedException {

    double somaTotal = 0.0;

    try (var scope = StructuredTaskScope.open(
            StructuredTaskScope.Joiner.allSuccessfulOrThrow()
    )) {

        List<Supplier<Double>> tarefas =
                new ArrayList<>();

        for (int i = 0; i < totalTarefas; i++) {

            final int numeroTarefa = i;

            tarefas.add(
                    scope.fork(() ->
                            tarefaPesadaVariavelEficiente(
                                    numeroTarefa,
                                    false
                            )
                    )
            );
        }

        System.out.println(
                "Todas as tarefas foram iniciadas."
        );

        scope.join();

        for (Supplier<Double> tarefa : tarefas) {

            somaTotal += tarefa.get();
        }

        System.out.println(
                "Todas as tarefas terminaram."
        );

    } catch (StructuredTaskScope.FailedException e) {

        System.out.println(
                "Uma tarefa falhou."
        );

        System.out.println(
                "Erro: " + e.getCause().getMessage()
        );
    }

    return somaTotal;
}


// ==========================================================
// MAIN
// ==========================================================

public static void main(String[] args)
        throws InterruptedException {

    System.out.println(
            "Executando " + totalTarefas +
                    " tarefas...\n"
    );





    // ------------------------------------------------------
    // COMPLETABLE FUTURE
    // ------------------------------------------------------

    long inicio = System.currentTimeMillis();

    Double resultado =
            executarCompletableFuture();

    long tempo =
            System.currentTimeMillis() - inicio;

    System.out.println(
            "\nResultado CompletableFuture: " + resultado
    );

    System.out.println(
            "Tempo CompletableFuture: " +
                    tempo + " ms"
    );


    System.out.println(
            "\n------------------------------------------"
    );


    // ------------------------------------------------------
    // STRUCTURED CONCURRENCY
    // ------------------------------------------------------

    inicio = System.currentTimeMillis();

    resultado =
            executarEstruturado();

    tempo =
            System.currentTimeMillis() - inicio;

    System.out.println(
            "\nResultado StructuredTaskScope: " +
                    resultado
    );

    System.out.println(
            "Tempo StructuredTaskScope: " +
                    tempo + " ms"
    );
}
