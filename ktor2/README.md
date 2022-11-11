Zadanie nr 2: ktor

funkcjonalnosci wykonane na 4.0 (CRUD dla dwoch modeli) zcommitowane przed terminem (03.11.2022)
poprawki po terminie:
    funkcjonalnosci wykonane na 4.5: 
        - dockerfile dla ktora oraz docker-compose, ktory tworzy dwa kontenery (ngrok i ktor). Adres ngrokowy mozna podejrzec pod localhost:4040
    funkcjonalnosci wykonane na 5.0:
        - CORS dla dwoch hostow (delete i put, reszta wedlug dokumentacji jest wlaczona domyslnie),
        - test CORSa: stworzony statyczny html dostepny pod localhost:8080/static2/index.html i w subdomenie sub1.localhost:8080/static/index.html, w ktorym umieszczony zostal skrypt JS (yikes), ktory uruchamia request DELETE, ktory bez allowHost i allowMethod w CORSie nie dziala, w przypadku uruchomienia go z adresu subdomeny (inny origin requestu)
