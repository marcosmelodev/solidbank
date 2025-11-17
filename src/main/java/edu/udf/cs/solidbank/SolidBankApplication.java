package edu.udf.cs.solidbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SolidBankApplication {
    public static void main(String[] args) {
        SpringApplication.run(SolidBankApplication.class, args);
        System.out.println("\n=== BEM-VINDO AO SOLID BANK  ===");
        System.out.println("API disponível em: http://localhost:8080");

    }
}