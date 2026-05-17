#   Data Seeder (Automatizare Import Produse)

Acest modul este responsabil pentru popularea inițială a bazei de date cu un catalog de produse (electronice) în momentul în care aplicația este rulată pentru prima dată.

##   Scopul Modulului
Într-un mediu de dezvoltare și testare, o bază de date goală nu permite validarea corectă a funcționalităților (filtrare, coș de cumpărături, paginare). Pentru a evita introducerea manuală a zecilor de înregistrări, am implementat un script automat care:
1. Verifică starea bazei de date.
2. Extrage date reale (imagini, prețuri, descrieri) de pe un REST API public.
3. De-serializează datele JSON în obiecte Java.
4. Salvează persistent informațiile în MySQL.

##  Arhitectura Tehnică

Modulul utilizează interfața `CommandLineRunner` oferită de framework-ul Spring Boot. Aceasta garantează execuția codului o singură dată, imediat după ce contextul Spring (ApplicationContext) a fost complet încărcat, dar înainte de a accepta cereri HTTP de la utilizatori.

### Fluxul de Date (Data Flow)
1. **Verificare (Safety Check):** Se execută o interogare `SELECT COUNT(*)` pe tabelul `products`. Importul se declanșează doar dacă numărul de rezultate este `0`. Aceasta previne duplicarea datelor la reporniri succesive ale aplicației.
2. **HTTP Request:** Este folosit `RestTemplate` pentru a iniția un `GET` request asincron către API-ul extern (`dummyjson.com/products`).
3. **Data Transfer Object (DTO):** Răspunsul JSON este mapat automat pe clase intermediare (`DummyJsonResponse` și `DummyJsonProductDto`) folosind librăria **Jackson**. Această abordare decuplează formatul extern al datelor de structura internă a bazei de date.
4. **Transformare și Persistență:** O expresie Java 8 Streams (`.stream().map(...)`) mapează fiecare DTO pe o Entitate JPA (`Product`), adăugând atribute extra (ex: stoc fictiv). La final, metoda `saveAll()` din Spring Data JPA inserează produsele eficient, folosind comenzi SQL *batch*.

##  Sursa de Date
Pentru a simula un magazin de electronice autentic, datele sunt extrase de la **DummyJSON API**.
* **Endpoint folosit:** `https://dummyjson.com/products/category/smartphones`
* **Date extrase:** ID extern, Titlu, Descriere, Preț, Categorie, Brand, și un URL valid către o imagine de tip *thumbnail*.

##  Configurare
Nu este necesară nicio intervenție manuală pentru a rula seeder-ul. Este suficient ca aplicația să fie conectată la o bază de date validă (vezi `application.properties`).

*Notă:* Dacă se dorește re-importul datelor sau actualizarea catalogului, se poate forța recrearea tabelelor schimbând proprietatea JPA:
```properties
spring.jpa.hibernate.ddl-auto=create-drop
``` 

###  Infrastructură și Bază de Date (Docker)

Pentru a asigura un mediu de dezvoltare curat, izolat și ușor de reprodus pe orice sistem de operare, persistența datelor este gestionată printr-un container **Docker** rulând imaginea oficială de `mysql:8.0`.

Această abordare elimină necesitatea instalării manuale a serverului MySQL pe mașina gazdă (host) și automatizează crearea schemei bazei de date.

### Pornirea Mediului (Containerului)

Pentru a inițializa serverul de baze de date, asigurați-vă că motorul Docker rulează și executați următoarea comandă în terminal:

```bash
docker run --name store_db -e MYSQL_ROOT_PASSWORD=parolamea -e MYSQL_DATABASE=magazin_db -p 3307:3306 -d mysql:8.0
```

## Explicația parametrilor utilizați:

`--name store_db`: Atribuie un nume specific containerului pentru a putea fi oprit/pornit cu ușurință ulterior.

`-e MYSQL_ROOT_PASSWORD=parolamea`: Variabilă de mediu care setează automat parola pentru utilizatorul root.

`-e MYSQL_DATABASE=magazin_db`: Creează schema/baza de date magazin_db la prima inițializare a containerului.

`-p 3307:3306`: Port Forwarding. Mapăm portul intern al containerului (3306) către portul 3307 al mașinii gazdă pentru a evita conflictele cu alte servicii MySQL instalate nativ.

`-d mysql:8.0`: Rulează containerul în modul detached (în fundal), folosind imaginea MySQL versiunea 8.0.


##  Concluzie și Utilizare Ulterioară

Prin integrarea mediului Docker cu mecanismul automat de Data Seeding, proiectul beneficiază de o infrastructură solidă și independentă.

Odată ce containerul MySQL este pornit și aplicația Spring Boot a fost rulată cu succes, întregul proces se desfășoară autonom. Datele sunt descărcate, structurate și salvate persistent în baza de date (`magazin_db`), fiind imediat disponibile.
  