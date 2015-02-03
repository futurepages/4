package org.futurepages.util.brazil;

import java.util.ArrayList;

public class SemanaUtil {

    private int id;
    private String nome;
    private static ArrayList<String> diasSemana = new ArrayList<String>();
    
    static{
        diasSemana.add("domingo");
        diasSemana.add("segunda-feira");
        diasSemana.add("terça-feira");
        diasSemana.add("quarta-feira");
        diasSemana.add("quinta-feira");
        diasSemana.add("sexta-feira");
        diasSemana.add("sábado");
    }
    
    public static ArrayList<String> diasSemana() {
        return diasSemana;
    }

    public static String get(int i) {
        return diasSemana.get(i - 1);
    }

    public SemanaUtil(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
