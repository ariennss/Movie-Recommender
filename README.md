COLLABORATIVE FILTERING IMPLEMENTATION

L'idea alla base del collaborative filtering è molto semplice: 
- gli utenti A, B, C, ecc... votano degli item;
- prendo un utente di riferimento X, e comparo i voti dei suoi item con i voti degli altri utenti
- trovo l'utente più simile a X e suggerisco a X un item che questo utente ha già votato, ma X ancora no.

La struttura dati che ho utilizzato è una nested hashmap:<br>
HashMap <Utente, Hashmap <Film, Recensione>><br>
La mappa esterna ha come chiave l'username e come valore una mappa interna che comprende le recensioni di tutti i film che tale utente ha guardato.<br>

Si forma una struttura di questo tipo:  <br>
  <UtenteA,  <FilmA, 5; FilmB, 7; FilmX, 9>> <br>
  <UtenteB,  <FilmA, 6; FilmD, 9; FilmZ, 3; FilmH, 8>>  <br>
  <UtenteC,  <FilmA, 7; FilmG, 10; FilmZ, 5; FilmX, 7>>  <br>
  ecc...

Funzionamento dei metodi più nello specifico:
  
  1. creaMappaUtenti <br>
     Legge il CSV e crea l'hashmap innestato di cui sopra a partire dal CSV
  
  2. aggiungiUser e aggiungiRecensioneNuovoUtente <br>
     Metodi per aggiungere eventualmente nuovi utenti nell'hashmap. Utilizzato per creare l'utente di riferimento X a cui suggerire i film
  
  3. mapUserConMieiFilm <br>
     Restituisce una nuova mappa: a partire dalla mappa di utenti iniziale, lascio soltanto gli utenti che hanno almeno un film in comune con l'utente di riferimento X
  
  4. mapUserTreFilm <br>
     Restituisce una nuova mappa: a partire dalla mappa di utenti con almeno un film in comune con X, lascio soltato gli utenti con almeno 3 film in comune con X 
  
  5. mapUserTreFilmEPiu <br>
     Restituisce una nuova mappa: a partire dalla mappa di utenti con almeno 3 film in comune con X, lascio solo quegli utenti che, oltre ai film in comune con X, hanno almeno un film in più NON in comune con X: sarà il film da consigliare potenzialmente a X
  
  6. calcoloDistanzaDaUser <br>
      A partire dalla mappa UserTreFilmePiu, calcolo per ogni utente la "distanza" dall'utente di riferimento X: più le recensioni tra l'utente di riferimento X e l'utente preso in considerazione sono simili, più la distanza tra i due utenti è piccola.
      Se gli utenti hanno visto tre film in comune e le recensioni sono identiche, la distanza sarà 0, ad esempio.
  
  7. trovaUtentiConDistanzaMinore <br>
      Restituisce una nuova mappa di utenti, i meno distanti dall'utente X di riferimento. Il risultato può essere:
      - una mappa con un'unico user (il più vicino all'utente X);
      - una mappa con più user (nel caso ci siano più utenti che condividono la stessa distanza minima da X)
  
  8. trovaUtentePiuVicino <br>
      Restituisce il nome dell'utente più vicino a X:
      - se nella mappa precedentemente creata esiste un solo utente, allora quello sarà l'utente più simile a X
      - se nella mappa di utenti più vicini a X ci sono più utenti, viene selezionato l'utente con più film in comune con X
        
  9. filmConsigliato <br>
      Restituisce il nome del film da consigliare a X.
      Prendiamo l'utente più vicino a X ricavato precedentemente: questo utente avrà almeno un film che X non ha ancora visto / recensito
      - se questo film ha voto da 6 a 10, lo suggeriamo a X;
      - se il film ha voto da 5 in giù, non lo suggeriamo. Sarebbe come suggerire a X qualcosa che probabilmente non piacerà, dato che non è piaciuto all'utente più simile.
        In questo caso, suggeriamo a X un film selezionato da un sottoinsieme di utenti con distanza da X ragionevolmente corta (scelta arbitrariamente);
         
  10. consigliaFilm <br>
      Metodo finale che integra tutti quelli precedentemente descritti

