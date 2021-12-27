//impostare scheda arduino e porta arduino.
//fa partire sketch
//settare scheda esp e porta nello sketch esp 
//far partire esp

#include <Wire.h>

int humidity = 0;
int level = 0;

void sendData(){
  Wire.write((const byte*)(&humidity),2);
  Wire.write((const byte*)(&level),2);
}

void setup() {
  Serial.begin(115200);
  Wire.begin(1);    //imposta i2c come SLAVE su address 1
  Wire.onRequest(sendData); //invia i dati quando master lo richiede

}

void loop() {
  // aggiorna le variabili globali con dati simulati
  humidity = (humidity+16)%1024;
  level = (level+16)%1024;

  // stampa l'attuale valore
  Serial.println(humidity);
  Serial.println(level);
  Serial.println("---");
  delay(1000);

}
