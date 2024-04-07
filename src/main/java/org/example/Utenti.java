package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Utenti {

    public static Map<String, Map<String, Integer>> creaMappaUtenti(String csvFile) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> csvData = reader.readAll();

            //creo una mappa per raggruppare le recensioni per utente
            Map<String, Map<String, Integer>> mappaUtenti = new HashMap<>();
            /*Map<User, Map<Anime, Recensione>>
            Cioè: c'è una mappa interna che alla chiave Anime fa corrispondere il valore Recensione;
            poi c'è una mappa esterna che alla chiave User fa corrispondere la coppia Anime-Recensione che l'user ha dato.
             */

            for (String[] riga : csvData) { //leggo i dati del csv
                String user = riga[0]; //nella prima riga ci sono gli user
                String anime = riga[1]; //nella seconda riga ci sono i titoli
                Integer recensione = Integer.valueOf(riga[3]); //nella quarta riga ci sono le recensioni

                //questo serve per aggiungere un utente nuovo: non so se servirà nel momento in cui devo aggiungere un nuovo utente e suggerirgli quale film guardare
                if (!mappaUtenti.containsKey(user)) {
                    mappaUtenti.put(user, new HashMap<>());

                }

                mappaUtenti.get(user).put(anime, recensione);

                //  questo serve per aggiungere le coppie anime-recensione all'hashmap mappaUtenti

            }
            return mappaUtenti;
        }
    }

    //con questo metodo aggiungo un nuovo utente nella mappa: sarà la persona a cui dovrò consigliare un nuovo Film
    //per tutto il resto del codice il nuovo utente è chiamato Arianna o Ari per riconoscerlo
    public static void aggiungiUser (String user, Map<String, Map<String, Integer>> mappaUtenti) {
        if (!mappaUtenti.containsKey(user)) {
            mappaUtenti.put(user, new HashMap<>());
        }
        else System.out.println("Questo utente esiste già");
    }

    //aggiungo l'elenco dei film recensiti dal nuovo utente Arianna
    public static void aggiungiRecensioneNuovoUtente (String nuovoutente, Map<String, Map<String, Integer>> mappaUtenti, String anime, String recensione) {
        mappaUtenti.get(nuovoutente).put(anime, Integer.valueOf(recensione));
    }

    /*a partire dalla mappa di tutti gli utenti che hanno recensito vari film, creo una mappa più piccola
    in cui ci sono soltanto gli utenti che hanno nella loro mappa interna di Film almeno un film compatibile
    con quelli recensiti dal nuovo utente Arianna.
    Il risultato è una mappa del genere:
    - utente 1: ha solo un film in comune con Arianna
    - utente 2: ha 3 film in comune con Arianna
    - utente 3: ha tutti i film in comune con Arianna
     */
   public static Map<String, Map<String, Integer>> mapUserConMieiFilm(Map<String, Map<String, Integer>> mappaUtenti, String nuovoUser) {
       Map<String, Map<String, Integer>> userConMieiStessiFilm = new HashMap<>(); //nuova mappa in cui ci saranno solo gli utenti che hanno almeno un film in comune con me

       Map<String, Integer> recensioniArianna = mappaUtenti.get(nuovoUser); //ottengo l'insieme delle recensioni di Arianna
       for (Map.Entry<String, Integer> recAri : recensioniArianna.entrySet()) { //per ogni film recensito da Arianna

        for (Map.Entry<String, Map<String, Integer>> user : mappaUtenti.entrySet()) { //ciclo ogni utente
            Map<String, Integer> recensioniUser = mappaUtenti.get(user.getKey());
            for (Map.Entry<String, Integer> recUser : recensioniUser.entrySet()) { //e per ogni utente ciclo ogni film che ha recensito
                if (recUser.getKey().equals(recAri.getKey())) { // se il film che sto considerando è uguale al film che ha recensito Arianna
                    userConMieiStessiFilm.putIfAbsent(user.getKey(), new HashMap<>()); //lo inserisco nella nuova hashmap
                    userConMieiStessiFilm.get(user.getKey()).putIfAbsent(recUser.getKey(), recUser.getValue());
                }
            }


        }
       } return userConMieiStessiFilm;
    }

    /*A partire dalla mappa creata subito sopra (in cui gli utenti hanno almeno un film in comune con Arianna), creo
    una nuova mappa ancora più piccola in cui lascio soltanto gli utenti che hanno almeno 3 film in comune con Arianna.
    * */
    public static Map<String, Map<String, Integer>> mapUserTreFilm(Map<String, Map<String, Integer>> mappaUserConMieRecensioni) {
        Map<String, Map<String, Integer>> userCon3AnimeUgualiAiMiei = new HashMap<>(); //nuovo hashmap di utenti che hanno almeno tre film recensiti in comune con Arianna
        for (Map.Entry<String, Map<String, Integer>> user : mappaUserConMieRecensioni.entrySet()) { //per ogni utente nella mappa di utenti che ha almeno un film in comune con Arianna

            if (user.getValue().size() >= 3) { //se l'insieme delle recensioni è maggiore o uguale a 3
                Map<String, Integer> titoli = user.getValue(); //lo inserisco nella nuova mappa
                userCon3AnimeUgualiAiMiei.put(user.getKey(), titoli);
            }
        }
        return userCon3AnimeUgualiAiMiei;
    }


    /*In questo metodo creo una mappa ancora più piccola:
    a partire dalla mappa totale, creo una mappa in cui ci sono soltanto gli utenti che hanno una mappa interna
    di recensioni con almeno 3 film uguali ai miei PIU almeno un film che io non ho recensito.
    Lo faccio controllando che la mappa dell'utente in questione abbia più elementi della mappa dell'utente Arianna
     */
    public static Map<String, Map<String, Integer>> mapUserTreFilmEPiu(Map<String, Map<String, Integer>> mappaUserConTreFilmComeMe, Map<String, Map<String, Integer>> mappaTotale) {

        Map<String, Map<String, Integer>> userCon3AnimeUgualiAiMieiAndMore = new HashMap<>(); //sarà la nuova mappa in cui inserisco gli utenti che hanno almeno tre film in comune con Arianna ma anche almeno un film che Arianna non ha visto


        for (Map.Entry<String, Map<String, Integer>> user1 : mappaUserConTreFilmComeMe.entrySet()) { // ciclo per ogni utente presente nella mappa di persone che hanno almeno 3 film in comune con Arianna
            for (Map.Entry<String, Map<String, Integer>> user : mappaTotale.entrySet()) { // ciclo per ogni utente presente nella mappa TOTALE
                if (user1.getKey().equals(user.getKey())) { //quando trovo nella mappa totale l'utente corrispondente a quello che sto considerando nel ciclo for esterno...
                    if (user.getValue().size() > user1.getValue().size()) { //guardo se il numero di film che ha recensito in totale è maggiore del numero di film che ha in comune con Arianna
                        Map<String, Integer> titoli = user.getValue(); //se è maggiore, lo aggiungo alla nuova mappa di persone che hanno almeno tre film in comune con Arianna ma anche almeno un film non-in comune
                        userCon3AnimeUgualiAiMieiAndMore.put(user.getKey(), titoli);
                    }
                }

            }
        }
        return userCon3AnimeUgualiAiMieiAndMore;
    }

    //metodo per calcolare la distanza tra Arianna e ciascun utente presente nella nuova mappa creata con il metodo appena sopra
    //mi ritorna una mappa <String, Float> in cui String = utente e Float = distanza da Arianna
    /*
    La distanza è calcolata in questo modo: guardo i film che Arianna e l'utente X hanno in comune.
    Faccio una differenza tra il valore della recensione di Arianna e il valore della recensione di X
    Divido il valore che ottengo per il numero di film che ho considerato.
     */
    public static Map<String, Float> calcoloDistanzaDaUser(String nuovoUser, Map<String, Map<String, Integer>> mappaUserCon3AnimeUgualiAiMieiAndMore, Map<String, Map<String, Integer>> mappaTotale) {
        Map<String, Integer> recensioniArianna = mappaTotale.get(nuovoUser);
        Map<String, Float> distanzaDaArianna = new HashMap<>(); //nuova mappa in cui metto: user - distanza da Arianna

        for (Map.Entry<String, Map<String, Integer>> user : mappaUserCon3AnimeUgualiAiMieiAndMore.entrySet()) { //per ogni utente che ha almeno 3 film in comune con Arianna e almeno uno diverso da Arianna...
            float distanza = 0;
            float animeInComune = 0;
            float distanzamedia = 0;

            Map<String, Integer> recensioniUser = user.getValue(); //estraggo l'insieme delle recensioni di ciascun utente
            for (Map.Entry<String, Integer> recAri : recensioniArianna.entrySet()) { //per ogni film recensito da Arianna
                String anime = recAri.getKey();
                if (recensioniUser.containsKey(anime)) { //guardo se tra le recensioni dell'utente che sto osservando nel ciclo c'è anche il film recensito da Arianna
                    animeInComune++; //se c'è, faccio +1 al valore degli anime in comune: mi servirà per calcolare la media.
                    //Ora ho trovato una corrispondenza tra recensione di Arianna e recensione dell'user X
                    int recensionediArianna = recAri.getValue(); //estraggo la recensione di Arianna
                    int recensionedellUser = recensioniUser.get(anime); //estraggo la recensione di X

                    int differenzatrafilm = Math.abs(recensionediArianna - recensionedellUser); //calcolo la differenza tra i due

                    distanza = distanza + Math.abs(differenzatrafilm); //la divido per il numero di anime che Arianna e X hanno in comune

                }
            }

            distanzamedia = distanza / animeInComune;
            distanzaDaArianna.put(user.getKey(), distanzamedia); //nella nuova hashmap metto l'utente X e la sua distanza media da Arianna
        }
        return distanzaDaArianna;
    }

    /*questo metodo per trovare l'utente con distanza minore da Arianna. Sarebbe stato meglio creare un comparator
    per poi fare un sort ma i comparator non mi piacciono e non ne avevo voglia. Mi sembra che funzioni lo stesso.
     */
    public static Map<String, Map<String, Integer>> trovaUtentiConDistanzaMinore(Map<String, Float> distanze, Map<String, Map<String, Integer>> mappaUserCon3AnimeUgualiAiMieiAndMore, Map<String, Map<String, Integer>> utentiConAlmeno3AnimeComeMe) {
        float distanzaminore = Float.MAX_VALUE;
        String utentecondistanzaminore = null;

        HashMap<String, Map<String, Integer>> utentiPiuViciniAArianna = new HashMap<String, Map<String, Integer>>();
        /*
        In questa mappa metterò l'utente "X" che avrà la distanza minore da Arianna; oppure tutti gli utenti che hanno la stessa distanza che c'è tra Arianna e X.
         */

        for (Map.Entry<String, Float> utenteDistante : distanze.entrySet()) { //per ogni persona presente nell'hashmap che mostra la distanza da Arianna
            if (utenteDistante.getValue() < distanzaminore) {
                distanzaminore = utenteDistante.getValue();
                utentecondistanzaminore = utenteDistante.getKey();
            }

        }
        for (Map.Entry<String, Float> utenteDistante : distanze.entrySet()) {
            if (utenteDistante.getValue() == distanzaminore) {
                utentiPiuViciniAArianna.putIfAbsent(utenteDistante.getKey(), new HashMap<>());
            }
        }

        String anime = null;
        int recensione = 0;
        for (Map.Entry<String, Map<String, Integer>> utente : utentiPiuViciniAArianna.entrySet()) {
            for (Map.Entry<String, Map<String, Integer>> user : mappaUserCon3AnimeUgualiAiMieiAndMore.entrySet()) {
                Map<String, Integer> mappa = user.getValue();
                if (utente.getKey().equals(user.getKey())) {
                    for (Map.Entry<String, Integer> film : mappa.entrySet()) {
                        anime = film.getKey();
                        recensione = film.getValue();
                        utentiPiuViciniAArianna.get(utente.getKey()).put(anime, recensione);
                    }

                }
            }

        }
        System.out.println("Lista utenti meno distanti: " + utentiPiuViciniAArianna);
        return utentiPiuViciniAArianna;
    }

    public static String trovaUtentePiuVicino (Map<String, Map<String, Integer>> utentiConAlmeno3AnimeComeMe, Map<String, Map<String, Integer>> utentiConStessaDistanzaDaArianna) {
        String utentepiuvicino = null;
        int grandezzamappa = 0;
        for (Map.Entry<String, Map<String, Integer>> utente : utentiConAlmeno3AnimeComeMe.entrySet()) {
            for (Map.Entry<String, Map<String, Integer>> user : utentiConStessaDistanzaDaArianna.entrySet()) {
                if (utente.getKey().equals(user.getKey())) {
                    if (user.getValue().size() > grandezzamappa) {
                        utentepiuvicino = user.getKey();
                    }
                }

            }

        }

        return utentepiuvicino;
    }




    /*
    Dopo aver trovato l'utente con distanza minore da Arianna, prendo questo utente e guardo quali sono i film
    che lui ha guardano che non sono presenti nella mappa di Arianna.
    Tra questi film, trovo quello con recensione più alta.
    Estraggo il titolo, che sarà il film da consigliare ad Arianna.
     */


    public static String filmConsigliato(Map<String, Float> distanze, Map<String, Map<String, Integer>> mappaUserCon3AnimeUgualiAiMieiAndMore, Map<String, Map<String, Integer>> animeTotale, Map<String, Map<String, Integer>> utenticon3FilmComeMe, Map<String, Map<String, Integer>> utentiConStessaDIstanzaDaArianna, String userNuovo ) {

        String userPiuSimile = trovaUtentePiuVicino(utenticon3FilmComeMe, utentiConStessaDIstanzaDaArianna); //applicando il metodo precedente, trovo l'utente più "vicino" ad Arianna
        Map<String, Integer> recensioniUserPiuSimile = mappaUserCon3AnimeUgualiAiMieiAndMore.get(userPiuSimile); //estraggo l'elenco dei film che questo utente Y ha recensito

        Map<String, Integer> recensioniArianna = animeTotale.get(userNuovo); //estraggo le recensioni di Arianna

        int recensionepiualta = 0;
        String filmconsigliato = null;

        HashMap<String, Integer> recPossibili = new HashMap<>(); //sarà la nuova mappa di anime che Y ha guardato ma Arianna no

                for (Map.Entry<String, Integer> recUserPiuSimile : recensioniUserPiuSimile.entrySet()){ //per ogni film guardato da Y
                    if(recensioniArianna.get(recUserPiuSimile.getKey()) == null) { //controllo che Arianna nella sua mappa NON abbia quel film
                        recPossibili.put(recUserPiuSimile.getKey(), recUserPiuSimile.getValue()); //se non lo ha, inserisco questo film nella mappa di anime che Y ha guardato ma Arianna no
                    }
                }
        // a questo punto ho una lista di film da suggerire a Arianna

                for (Map.Entry<String, Integer> recPossibile : recPossibili.entrySet()) { //per ogni film in questa mappa di potenziali suggerimenti
                    if (recPossibile.getValue() > recensionepiualta) { //cerco il film che l'utente Y ha recensito meglio
                        recensionepiualta = recPossibile.getValue();
                        filmconsigliato = recPossibile.getKey();


                    }
                }

                /*suggerisco questo film all'utente solo se la recensione è da 6 a 10; sennò sto consigliando all'utente
                un film che al suo utente più simile NON è piaciuto!
                 */

                if (recensionepiualta > 5) {
                    System.out.println("Ciao " + userNuovo);
                    System.out.println("L'utente con gusti più simili ai tuoi è: " + userPiuSimile);
                    System.out.println("In base ai film che " + userPiuSimile + "ha già guardato, ti consigliamo " + filmconsigliato);
                }


                /* se invece il film è recensito da 5 in giù: creo una mappa di utenti con distanza simile ad Arianna
                La distanza minima Arianna-Utente è 0; la massima è 10; ho pensato che una distanza minore di 2 fosse una distanza
                sufficientemente piccola per trovare utenti simili ad Arianna.
                 */
         else if (recensionepiualta <= 5) {
            var utentipiusimili = new HashMap<String, Map<String, Integer>>(); // Creo l'hashmap degli utenti con distanza da Ari minore di 2: vuota per ora

            for (Map.Entry<String, Float> utente : distanze.entrySet()) { //per ogni utente nell'hashmap <Utente, DistanzaDaArianna>
                if (utente.getValue() <= 2) { //se la distanza è minore di 2
                    var filmutentisimili = new HashMap<String, Integer>(); // Creo una nuova mappa per ogni utente con distanza minore di 2
                    utentipiusimili.put(utente.getKey(), filmutentisimili); // Aggiungo l'utente e la mappa appena creata a "utentipiusimili"
                }
            }

            for (Map.Entry<String, Map<String, Integer>> user : utentipiusimili.entrySet()) {
                String utente = user.getKey();
                Map<String, Integer> filmutentisimili = user.getValue();

                Map<String, Integer> filmuser = mappaUserCon3AnimeUgualiAiMieiAndMore.get(utente);
                Map<String, Integer> filmuser2 = utenticon3FilmComeMe.get(utente);


                    for (Map.Entry<String, Integer> film : filmuser.entrySet()) {
                        String anime = film.getKey();
                        if (!filmuser2.containsKey(anime)) { // Verifico se il film non è presente nella mappa di utente2
                            filmutentisimili.put(anime, film.getValue()); // Aggiungo il film alla mappa
                        }
                    }

            }

            int filmpiualto = 0;
            String animeConsigliato = null;
            for (Map.Entry<String, Map<String, Integer>> user : utentipiusimili.entrySet()) {
                Map<String, Integer> listafilm = user.getValue();

                for (var film : listafilm.entrySet()) {
                    if (film.getValue() > filmpiualto) {
                        filmpiualto = film.getValue();
                        animeConsigliato = film.getKey();
                        filmconsigliato = animeConsigliato;
                    }
                }

            }

           System.out.println("Ciao " + userNuovo);
            System.out.println("L'utente con gusti più simili ai tuoi è " + userPiuSimile + ", ma sembra non abbia nulla di nuovo da suggerirti.");
                    System.out.println("Ti consigliamo quindi un anime in base ad altri utenti comunque simili a te: " + filmconsigliato);

        }

        return filmconsigliato;

    }


    //metodo finale che racchiude tutti i metodi precedenti (non che faccia qualcosa in più rispetto a quelli precedenti, ma vabbe era bello)
    public static void consigliaFilm(Map<String, Map<String, Integer>> totaleUtenti, String nuovoUser) {
        Map<String, Map<String, Integer>> mappaConUserConMieiFilm = mapUserConMieiFilm(totaleUtenti, nuovoUser);
        Map<String, Map<String, Integer>> mappaCon3FilmComeMe = mapUserTreFilm(mappaConUserConMieiFilm);
        Map<String, Map<String, Integer>> mappaConPiudi3FilmComeMe = mapUserTreFilmEPiu(mappaCon3FilmComeMe, totaleUtenti);
        Map<String, Float> distanze = calcoloDistanzaDaUser(nuovoUser, mappaConPiudi3FilmComeMe, totaleUtenti);
        var mappaUserConStessaDistanza = trovaUtentiConDistanzaMinore(distanze, mappaConPiudi3FilmComeMe, mappaCon3FilmComeMe);
        String utentePiuVicino = trovaUtentePiuVicino(mappaCon3FilmComeMe, mappaUserConStessaDistanza);
        String animeConsigliato = filmConsigliato(distanze, mappaConPiudi3FilmComeMe, totaleUtenti, mappaCon3FilmComeMe, mappaUserConStessaDistanza, nuovoUser);
    }





}