/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.vitorrural.automatopilha1;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author USUARIO
 */

public class AutomatoPilha1 {

    public static void main(String[] args) throws IOException {
       Automato automato = new Automato("aNb2NcN.txt");
        Scanner scanner = new Scanner(System.in);
        String palavra;

        while (true) {
            System.out.println("Digite palavras com a e b (ou 'sair'):");
            palavra = scanner.nextLine();
            if (palavra.equals("sair")) break;
            boolean aceita = automato.aceita(palavra);
            System.out.println("Resultado: " + (aceita ? "ACEITA" : "REJEITADA"));
        }
        scanner.close();
    }
}

