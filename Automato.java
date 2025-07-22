/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vitorrural.automatopilha1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author USUARIO
 */
public class Automato {
    private int estadoInicial = 0;
    private int[] estadosFinais;
    private Transicao[][] transicoes;
    private char[] pilha;
    private int topo;

    private class Transicao {
        char leitura;
        char desempilha;
        char empilha;
        int proximoEstado;

        Transicao(char leitura, char desempilha, char empilha, int proximoEstado) {
            this.leitura = leitura;
            this.desempilha = desempilha;
            this.empilha = empilha;
            this.proximoEstado = proximoEstado;
        }
    }

    public Automato(String arquivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
            String linha = br.readLine();
            String[] finais = linha.split(",");
            estadosFinais = new int[finais.length];
            for (int i = 0; i < finais.length; i++) {
                estadosFinais[i] = Integer.parseInt(finais[i].trim());
            }

            transicoes = new Transicao[10][10];
            int estadoAtual = 0;

            while ((linha = br.readLine()) != null) {
                String[] trans = linha.split(",");
                for (int i = 0; i < trans.length; i++) {
                    String t = trans[i].trim().replace("(", "").replace(")", "");
                    if (t.equals("ε")) continue;

                    String[] partes = t.split(";");
                    int proximo = Integer.parseInt(partes[0]);
                    char leitura = partes[1].charAt(0);
                    char desempilha = partes[2].charAt(0);
                    char empilha = partes[3].charAt(0);

                    transicoes[estadoAtual][i] = new Transicao(leitura, desempilha, empilha, proximo);
                }
                estadoAtual++;
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    public boolean aceita(String palavra) {
        pilha = new char[100];
        topo = -1;
        empilhar('Z');
        return processar(palavra, 0, estadoInicial);
    }

    private boolean processar(String palavra, int pos, int estado) {
        if (pos == palavra.length()) {
            return isFinal(estado) && pilhaVazia();
        }

        for (int i = 0; i < transicoes[estado].length; i++) {
            Transicao t = transicoes[estado][i];
            if (t == null) continue;

            boolean leituraCombina = pos < palavra.length() && palavra.charAt(pos) == t.leitura;
            boolean epsilon = t.leitura == 'ε';

            if (leituraCombina || epsilon) {
                if (t.desempilha != 'ε' && topo() != t.desempilha) continue;

                int topoBackup = topo;
                char desempilhado = 'ε';

                if (t.desempilha != 'ε') desempilhado = desempilhar();
                if (t.empilha != 'ε') empilhar(t.empilha);

                boolean resultado = processar(palavra, epsilon ? pos : pos + 1, t.proximoEstado);

                if (t.empilha != 'ε') desempilhar();
                if (t.desempilha != 'ε') empilhar(desempilhado);
                topo = topoBackup;

                if (resultado) return true;
            }
        }

        return false;
    }

    private boolean pilhaVazia() {
        return topo == 0 && pilha[0] == 'Z';
    }

    private boolean isFinal(int estado) {
        for (int f : estadosFinais) {
            if (f == estado) return true;
        }
        return false;
    }

    private void empilhar(char c) {
        if (c != 'ε') pilha[++topo] = c;
    }

    private char desempilhar() {
        if (topo >= 0) return pilha[topo--];
        return 'ε';
    }

    private char topo() {
        if (topo >= 0) return pilha[topo];
        return 'ε';
    }
}