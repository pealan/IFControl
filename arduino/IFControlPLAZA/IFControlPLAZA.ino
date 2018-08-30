/*
 *Autores: Guilherme Souza ( guilhermesdas@gmail.com )
           Jansen Júnior ( jj )
           Pedro Alan ( @pealan )
 *Descrisao: Codigo de um servidor web que aguarda receber uma requsição de um socket
 *Data: O dia em que a terra parou
 *Projeto: SCRAP
 */

// Bibliotecas
#include <SPI.h>
#include <Ethernet.h>
#include <IRremote.h>
#include <DHT.h>

// Pinos disponíveis:
// 2, 3, 5, 6, 7, 8, 9, A0, A1, A2, A3, A4, A5

// Variáveis e constantes para utilização do sensor de temperatura
#define DHTPIN A1 // pino que estamos conectado
#define DHTTYPE DHT11 // DHT 11
DHT dht(DHTPIN, DHTTYPE);

// Variáveis para conexão do Servidor
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED }; // Endereço MAC do Arduino
IPAddress ip(192, 168, 0, 102); // Endereço IP do Arduino
EthernetServer server(80);  // Criar um servidor na porta 80

// Temperatura de fezes
int t = 25;

// Presença
boolean temGente;
boolean presenca;
int falsos;

// Variáveis para recebimento do request
char c; // Requisição é recebida byte pot byte
String req; // String armazena toda a requisicao

// Variável do emissor infravermelho
IRsend irsend;

// Pinos
int pinSensor = 5; // sensor de presença e movimento
int pinRele = 8; // pino relé

void setup() {
  
  falsos = 0;
  presenca = true;
  
  Ethernet.begin(mac, ip);  // inicializa o Shield Ethernet
  server.begin();           // inicializa o servidor
  Serial.begin(9600);
  pinMode( pinSensor, INPUT ); // Inicializa o pino do sensor no modo para leitura digital
  pinMode( pinRele, OUTPUT ); // Inicializa o pino do relé
  Serial.print(t);
}

void loop() {
  
  temGente = digitalRead(pinSensor);
  if (temGente) {
    presenca = true;
    falsos = 0;
  } else {
    falsos++;
    if ( falsos > 30000 ) {
      falsos = 0;
      presenca = false;
      Serial.println("NAO TEM GENTE");
    } 
  }
  Serial.println(falsos);

  EthernetClient client = server.available();  // tenta estabeler uma conexão com o cliente

  if ( client ) { // se conexão foi estabelecida
    while ( client.connected () ) { // enquanto conexão estiver estabelecida

      if ( client.available() ) { // se possui algum dado para ler

        c = client.read(); // leitura dos dados byte por byte
        req += c; // incrementa dados recebidos na String

        if ( c == '.' ) { // fim da mensagem
          processarString(client, req); // função para analizar a mengagem recebida
          req = ""; // limpa dos dados da String
          break; // sai do while
        }

      } // fim do recebimento da mensagem

    } // fim da conexão com o cliente

    client.stop(); // quebra da conexão com o cliente

  }

} // fim do loop

void processarString( EthernetClient client, String str ) {

  if ( str.indexOf("AR") > -1 ) { // se solicitar uma operação com o ar condicionado
    if ( str.indexOf("ON") > -1 ) { // se a solicitação for para ligar o ar condicionado
      enviar(0xC1AA09F6);
    }
    else if ( str.indexOf("OFF") > -1 ) {
      enviar(0xC1AA09F6);
    }
    else if ( str.indexOf("20") > -1 ) {
      enviar(0xC1AAE11E);
    }
    else if ( str.indexOf("21") > -1 ) {
      enviar(0xC1AA916E);
    }
    else if ( str.indexOf("22") > -1 ) {
      enviar(0xC1AA19E6);
    }
    else if ( str.indexOf("23") > -1 ) {
      enviar(0xC1AA9966);
    }
    else if ( str.indexOf("24") > -1 ) {
      enviar(0xC1AA619E);
    }
    else if ( str.indexOf("25") > -1 ) {
      enviar(0xC1AAE11E);
    }

    client.print("OK");

  }
  else if ( str.indexOf("TEMP") > -1 ) { // se solicitar a temperatura
    float temp = dht.readTemperature(); // leitura da temperatura
    if ( ( isnan(temp) ) ) { // caso ocorra falha da obtenção dos valores
      client.print(0);
    }
    else {
      client.print(temp);
    }
  }
  else if ( str.indexOf("UMIDADE") > -1 ) {
    float h = dht.readHumidity();
    if ( ( isnan(h) ) ) { // caso ocorra falha da obtenção dos valores
      client.print(0);
    }
    else {
      client.print(h);
    }
  }
  else if ( str.indexOf("PRESENCA") > -1 ) {
    if ( presenca == HIGH )
      client.print("TRUE");
    else
      client.print("FALSE");
  }
  else if ( str.indexOf("LUZ") > -1 ) {
    if ( str.indexOf("ON") > -1 ) {
      digitalWrite( pinRele, HIGH );
      client.print("OK");
    }
    else {
      digitalWrite( pinRele, LOW );
      client.print("OK");
    }
  }
} // HAVE FUN do método processRequest

void enviar(unsigned long cod ) {
  //int i;
  //for ( i = 0; i < 3; i++ ) {
    irsend.sendNEC(cod, 32);
    //delay(50);
  //}
}

