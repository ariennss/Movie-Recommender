package org.example;

import java.util.*;

public class CreazioneMatrice {

    // Method to create the matrix from the user reviews map
    public static String[][] creaMatrice(Map<String, Map<String, Integer>> mappaUtenti) { //questo metodo prende in input la mappa di utenti che creo con la classe creazioneHashMap
        Set<String> setTitoliUnici = new HashSet<>();
        //ora estraggo l'elenco dei titoli degli anime (non ripetuti): sarà il n. di colonne della mia matrice
        for (Map<String, Integer> mapAnimeRecensione : mappaUtenti.values()) {
            setTitoliUnici.addAll(mapAnimeRecensione.keySet());
        }
        List<String> titoliUnici = new ArrayList<>(setTitoliUnici);

        //creo array2D
        int totRighe = mappaUtenti.size();
        int totColonne = titoliUnici.size();

        String[][] matriceRecensioni = new String[totRighe][totColonne];

        //riempio array2D
        int indiceRiga = 0;
        for (Map<String, Integer> recensioniAnime : mappaUtenti.values()) {
            int indiceColonna = 0;
            for (String anime : titoliUnici) {
                Integer review = recensioniAnime.getOrDefault(anime, -1);
                matriceRecensioni[indiceRiga][indiceColonna++] = String.valueOf(review);
            }
            indiceRiga++;
        }
        return matriceRecensioni;
    }
}
