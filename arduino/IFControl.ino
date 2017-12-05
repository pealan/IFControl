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
// 2, 3, 5, 6, 7, 8, 9, A0, A1, A2, A3, A4,

// Variável do emissor infravermelho
IRsend irsend; // pino tals é o 3

void setup() {
         // inicializa o servidor
  Serial.begin(9600);

}

void loop() {
  

        unsigned int raw[92] = {4400,4300, 600,1550, 550,550, 550,1550, 550,1600, 550,500, 550,550, 550,1500, 650,500, 550,500, 600,1500, 650,450, 550,550, 550,1550, 600,1550, 600,500, 550,1550, 600,450, 600,1550, 600,1550, 550,1550, 600,1600, 600,450, 600,1550, 550,1550, 600,1550, 600,450, 550,550, 600,450, 600,450, 600,1550, 600,500, 550,500, 600,1550, 550,1550, 600,1550, 600,500, 550,500, 600,450, 600,500, 600,450, 600,500, 600,450, 600,450, 650,1500, 600,1550, 600,1500, 650,1500, 650,1500, 600,5050, 4400,4300, 600,1550, 550,500, 650,1500, 550,1600, 550,500, 600,450, 600,1550, 550,550, 550,500, 650,1500, 550,500, 550,550, 600,1500, 600,1550, 600,450, 600,1550, 600,450, 650,1500, 600,1550, 600,1500, 600,1550, 600,500, 550,1550, 600,1550, 600,1550, 550,500, 600,500, 600,450, 550,550, 600,1500, 600,500, 600,450, 600,1550, 550,1600, 600,1500, 600,500, 600,400, 650,450, 600,500, 600,450, 550,550, 600,450, 550,550, 550,1600, 550,1550, 550,1600, 550,1600, 550,1550, 600};        enviar(raw);
   
} // HAVE FUN do método processRequest

void enviar(unsigned int raw[92]) {
  int i;
  for ( i = 0; i < 3; i++ ) {
    irsend.sendRaw(raw, 92, 38);
    delay(50);
  }
}

