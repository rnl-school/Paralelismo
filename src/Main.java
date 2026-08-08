    static double soma = 0;
    // Simula um trabalho pesado de CPU
    private static void tarefaPesada() {
        String nomeAtual = Thread.currentThread().getName();
        System.out.println("Rodando na thread: " + nomeAtual);
        for (int i = 0; i < 5000; i++) {
            soma += i;
        }
        System.out.println("Soma total " + soma );
    }

    public static void main(String[] args) throws InterruptedException {
        int totalTarefas = 4;

        System.out.println("Executando " + totalTarefas + " tarefas pesadas...\n");

        // ==========================================
        // 1. EXECUÇÃO SEQUENCIAL
        // ==========================================
        long inicioSeq = System.currentTimeMillis();

        for (int i = 0; i < totalTarefas; i++) {
            tarefaPesada();
        }

        long tempoSeq = System.currentTimeMillis() - inicioSeq;
        System.out.println("Tempo Sequencial: " + tempoSeq + " ms");

        // ==========================================
        // 2. EXECUÇÃO PARALELA (Usando Thread)
        // ==========================================
        soma = 0;
        long inicioPar = System.currentTimeMillis();
        Thread[] threads = new Thread[totalTarefas];

        // Criando e iniciando as threads
        for (int i = 0; i < totalTarefas; i++) {
            threads[i] = new Thread(() -> tarefaPesada());
            threads[i].start(); // Dispara a execução em paralelo
        }

        // Aguardando todas as threads terminarem
        for (int i = 0; i < totalTarefas; i++) {
            threads[i].join(); // Pausa a thread principal até que a thread 'i' finalize
        }

        long tempoPar = System.currentTimeMillis() - inicioPar;
        System.out.println("Tempo Paralelo:   " + tempoPar + " ms\n");

        // Resultado
        System.out.printf("O código paralelo foi %.1fx mais rápido!%n", (double) tempoSeq / tempoPar);
    }
