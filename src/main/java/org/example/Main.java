package org.example;

import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException, CsvException {


        //LEGGO IL CSV E CREO LA MIA MAPPA DI UTENTI
        Map<String, Map<String, Integer>> mappaUtenti = creazioneHashMap.creaMappaUtenti("primi50filmcon3recensioniperuser.csv");
        //System.out.println(mappaUtenti);


        //AGGIUNGO ARIANNA: L'UTENTE TEST A CUI CONSIGLIARE UN FILM
        creazioneHashMap.aggiungiUser("Arianna", mappaUtenti);
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Arianna", mappaUtenti, "Btooom!", "4");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Arianna", mappaUtenti, "Sword Art Online", "8");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Arianna", mappaUtenti, "Highschool of the Dead", "3");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Arianna", mappaUtenti, "K", "7");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Arianna", mappaUtenti, "Fate/stay night", "2");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Arianna", mappaUtenti, "Elfen Lied", "2");


        //AGGIUNGO NUOVO UTENTE2 PER IL TEST:
        //HA FILM RECENSITI UGUALI AD ARIANNA + UNO CHE "ARIANNA" NON HA ANCORA RECENSITO.
        //se tutto funziona dovrà essere lui l'utente più simile all'utente "Arianna"

        creazioneHashMap.aggiungiUser("Utente2", mappaUtenti);
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Utente2", mappaUtenti, "Btooom!", "4");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Utente2", mappaUtenti, "Sword Art Online", "8");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Utente2", mappaUtenti, "Highschool of the Dead", "3");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Utente2", mappaUtenti, "K", "7");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Utente2", mappaUtenti, "Fate/stay night", "2");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Utente2", mappaUtenti, "Elfen Lied", "2");
        creazioneHashMap.aggiungiRecensioneNuovoUtente("Utente2", mappaUtenti, "FilmTest1", "10"); //recensione alta
        //creazioneHashMap.aggiungiRecensioneNuovoUtente("Utente2", mappaUtenti, "FilmTest1", "2");       //recensione bassa

        //PER VEDERE COSA CAMBIA SE IL FILM DA CONSIGLIARE HA RECENSIONE BASSA O ALTA:
        // - TOGLIERE IL COMMENTO ALL'ULTIMA RIGA DI CODICE QUA SOPRA
        // - RENDERE COMMENTO LA PENULTIMA RIGA DI CODICE
        // il titolo del film rimane lo stesso (FilmTest1) ma in un caso gli diamo una recensione di "10" e nell'altro caso di "2".



        //CREO MAPPA DI UTENTI CHE HANNO ALMENO VISTO UN FILM CHE ANCHE L'UTENTE "ARIANNA" HA VISTO E RECENSITO

        //Map<String, Map<String, Integer>> userConMieiAnime = creazioneHashMap.mapUserConMieiAnime(mappaUtenti, "Arianna");
        //System.out.println(userConMieiAnime);


        //A PARTIRE DALLA MAPPA APPENA CREATA, NE CREO UNA SECONDA IN CUI TENGO SOLO UTENTI CON ALMENO 3 FILM IN COMUNE CON "ARIANNA"

        //Map<String, Map<String, Integer>> usercon3animecomeMe = creazioneHashMap.userConTreAnimeUgualiAiMiei(userConMieiAnime);
        //System.out.println(usercon3animecomeMe);


        // A PARTIRE DALLA MAPPA NUOVA, CREO NUOVA MAPPA IN CUI TENGO SOLO UTENTI CHE, OLTRE AD AVERE ALMENO 3 FILM IN COMUNE CON "ARIANNA",
        // NE HANNO ANCHE ALMENO UNO IN PIU CHE ARIANNA ANCORA NON HA VISTO/RECENSITO: SARà POTENZIALMENTE UN FILM DA CONSIGLIARE.

        //Map<String, Map<String, Integer>> MAPPAPIUUNO = creazioneHashMap.userConTreAnimeUgualiAiMieiEAltri(usercon3animecomeMe, mappaUtenti);
        //System.out.println(MAPPAPIUUNO);


        //CALCOLO PER OGNU UTENTE LA SUA DISTANZA DALL'UTENTE CHE PRENDO COME RIFERIMENTO ("ARIANNA") NEL MIO CASO:
        //DISTANZA VALUTATA COME SOMMA RECENSIONI FILM IN COMUNE DIVISO IL N DI FILM IN COMUNE.

        //Map<String, Float> distanze = creazioneHashMap.calcoloDistanzaDaUser("Arianna", MAPPAPIUUNO, mappaUtenti);
        //System.out.println(distanze);


        // A PARTIRE DA QUESTA MAPPA DI DISTANZE, TROVO L'UTENTE CON DISTANZA MINORE DA ARIANNA = UTENTE CON GUSTI PIU SIMILI
        //String utentepiuvicino = creazioneHashMap.trovaUtentiConDistanzaMinore(distanze, MAPPAPIUUNO, usercon3animecomeMe);
        //System.out.println(utentepiuvicino);

        // PRENDO L'UTENTE PIU SIMILE E CONSIGLIO AD ARIANNA IL FILM CHE L'UTENTE PIU SIMILE HA GIA VISTO MA ARIANNA ANCORA NO.
        //String filmconsigliato = creazioneHashMap.animeConsigliato(distanze, MAPPAPIUUNO, mappaUtenti, usercon3animecomeMe, "Arianna");
        //System.out.println(filmconsigliato);



        creazioneHashMap.consigliaAnime(mappaUtenti, "Arianna");
        //creazioneHashMap.consigliaAnime(mappaUtenti, "Utente2");
    }
}