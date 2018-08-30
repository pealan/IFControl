//int pinoAzul = 6;
//int pinoVermelho = 5;
int pinoSensor = 2;
int acionamento;

void setup() {
 // pinMode(pinoVermelho, OUTPUT);
 // pinMode(pinoAzul, OUTPUT);
  pinMode(pinoSensor, INPUT); 
  Serial.begin(9600);
}

void loop() {
  acionamento = digitalRead(pinoSensor);
  if (acionamento == LOW ) {
    //digitalWrite(13, LOW);
    Serial.println("nao tem gente");
    //digitalWrite(pinoVermelho, LOW);
   // digitalWrite(pinoAzul, HIGH);
  }
  else {
   Serial.println( "tem gente");   // digitalWrite(pinoVermelho, HIGH);
   //digitalWrite(13, HIGH);
   // digitalWrite(pinoAzul, LOW);
  }
  delay(5000);
  
}
