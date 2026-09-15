package br.edu.mackenzie.gerenciadornomes;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Import de Banco de Dados JDBC
import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootApplication
public class GerenciadorNomesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GerenciadorNomesApplication.class, args);
    }

    @Override
    public void run(String... args) {

        // Conexao com banco de dados
        String url = "jdbc:h2:file:./data/banco_dados";
        String usuario = "admin";
        String senha = "admin";

        try (Connection connection =
                DriverManager.getConnection(url, usuario, senha);
            Statement statement = connection.createStatement()) {

            statement.execute("""
                CREATE TABLE IF NOT EXISTS nomes (
                    nome VARCHAR(256) NOT NULL UNIQUE
                )
                """);
            
            codigoAnterior(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void codigoAnterior(Connection connection) { 
        // alterado para nova implementação de banco de dados
        GerenciadorNomes gerenciador = new GerenciadorNomesBD(connection);

        gerenciador.adicionar("Ana");
        gerenciador.adicionar("Bruno");
        gerenciador.adicionar("Carlos");

        System.out.println("Nomes cadastrados:");
        for (String nome : gerenciador.obter()) {
            System.out.println("- " + nome);
        }

        System.out.print("\nAlterando Bruno para Beatriz... ");
        System.out.println(gerenciador.atualizar("Bruno", "Beatriz"));

        System.out.print("\nAlterando Bruno outra vez para Beatriz... ");
        System.out.println(gerenciador.atualizar("Bruno", "Beatriz"));

        System.out.print("Removendo Carlos... ");
        System.out.println(gerenciador.remover("Carlos"));

        System.out.print("Removendo Italo que não existe... ");
        System.out.println(gerenciador.remover("Italo"));

        System.out.println("\nNomes finais:");
        for (String nome : gerenciador.obter()) {
            System.out.println("- " + nome);
        }
    }
}
