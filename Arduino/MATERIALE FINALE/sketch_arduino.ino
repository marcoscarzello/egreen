#include <Wire.h>

int soil_pin = A0; //input sensore
int motor_pin = 3; //output pwm pompa
float umiditySensor;
float umidity;
byte byteUmidity;
byte byteLevel = 100;
float inverseUmiditySensor;
float level = 10; //livello acqua

void setup() {
  Serial.begin(9600);
  analogReference(EXTERNAL);
  pinMode(motor_pin, OUTPUT);

  Wire.begin(1);    //imposta i2c come SLAVE su address 1
  Wire.onRequest(sendData); //invia i dati quando master lo richiede
}

void sendData(){
  Wire.write(byteUmidity);
  Wire.write(byteLevel);
}


void loop() {

  umiditySensor = float(analogRead(soil_pin))*3.3/1023.0;
  inverseUmiditySensor = 1/umiditySensor;
  umidity = 3.2*inverseUmiditySensor*inverseUmiditySensor - 1.26*inverseUmiditySensor + 0.0504;
  byteUmidity = umidity * 100;
  
  Serial.print("Voltaggio sensore umidità: ");
  Serial.print(umiditySensor); 
  Serial.println(" V");

  Serial.print("Umidità calcolata: ");
  Serial.print(byteUmidity); 
  Serial.println(" % V/V");

  
  
  delay(1000); //tempo tra una misurazione del sensore di umidità e l'altra
}
