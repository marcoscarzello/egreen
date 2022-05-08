
#include <ESP8266Firebase.h>
#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <WiFiClient.h>
#include <Wire.h>
#include "index.h" //file html parallelo

String nomeRete = "default";     
String password = "default";

String userCode = "defaultUser";
String plantName = "defaultPlant";


int humidityValues [5] = { 0, 0, 0, 0, 0 };
String 	humidityPath = "/users/" + userCode + "/plants/" + plantName + "/params/last5humidity/";	


#define PROJECT_ID "did2021-f44c9-default-rtdb"

ESP8266WebServer server(80); 

int motor_pin = 3; //output pwm pompa
int um = 10;
int lv = 10;
int tempo;
String page = MAIN_page;


Firebase firebase(PROJECT_ID);

void handleRoot() { //pagina web con form
  server.send(200, "text/html", page);
}


void handleForm() {   //accade quando si clicca Invia nel form
 nomeRete = server.arg("nomerete"); 
 password = server.arg("password"); 
 plantName = server.arg("plantname"); 
 userCode = server.arg("usercode"); 


 Serial.print("nome rete: ");
 Serial.println(nomeRete);

 Serial.print("Password: ");
 Serial.println(password);
 
 String s = "<a href='/'> Go Back </a>";
 server.send(200, "text/html", s); 
 setup();                           //prova a connettersi ripartendo dal setup
}

void accessPoint() {
   WiFi.mode(WIFI_AP);
    Serial.println("switching to AP");
    WiFi.softAP("schedina", "qweqweqwe", 1, false, 8);
    //hConn = WiFi.onSoftAPModeStationConnected(conn);
   // hDisc = WiFi.onSoftAPModeStationDisconnected(disc);
    IPAddress myIP = WiFi.softAPIP(); //Get IP address

    Serial.print("HotSpt IP:");
    Serial.println(myIP);
 
    server.on("/", handleRoot);      //Which routine to handle at root location
    server.on("/action_page", handleForm);
    server.begin();                  //Start server
    Serial.println("HTTP server started");
  
  }
  
void setup(){
  Serial.begin(115200);
  Wire.begin();   //Configura il dispositivo come MASTER
  
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(1000);

  // Connect to WiFi
  Serial.println();
  Serial.println();
  Serial.print("Connecting to: ");
  Serial.println(nomeRete);
  WiFi.begin(nomeRete, password);

  tempo = 0;
  
  while (WiFi.status() != WL_CONNECTED && tempo < 10000) {
    delay(500);
    tempo = tempo + 500;
    Serial.print("-");
  }
  
  if (WiFi.status() == WL_CONNECTED){
  Serial.println("");
  Serial.println("WiFi Connected");
  

  // Print the IP address
  Serial.print("Use this URL to connect: ");
  Serial.print("http://");
  Serial.print(WiFi.localIP());
  Serial.println("/");
  digitalWrite(LED_BUILTIN, HIGH);
   }
   
  else {
      accessPoint();
    }

}

void loop() {
 if (WiFi.status() == WL_CONNECTED){
  
 
   Serial.println("Requesting data....");
  Wire.requestFrom(1,2); // richiede 4 byte da dispositivo #1
  int i=0;
  byte data[2]; // buffer in cui leggere i dati
  while (Wire.available()) {
    data[i] = Wire.read();
    i++;
  }
  
  if (i==2) {
     um = data[0];
     lv = data[1];  
  }
  
  //valori ultimi caricati in un ramo iniziale per debug
   firebase.setInt("Humidity", um);
   firebase.setInt("Level", lv);
   
   
   
   
   //vero upload 
	
	   humidityValues[4] = humidityValues[3];
	   humidityValues[3] = humidityValues[2];
	   humidityValues[2] = humidityValues[1];
	   humidityValues[1] = humidityValues[0];
	   humidityValues[0] = um;
	   
	   firebase.setInt(humidityPath + "a", humidityValues[0]);
	   firebase.setInt(humidityPath + "b", humidityValues[1]);
	   firebase.setInt(humidityPath + "c", humidityValues[2]);
	   firebase.setInt(humidityPath + "d", humidityValues[3]);
	   firebase.setInt(humidityPath + "e", humidityValues[4]);
	   
   
   /*
    if (um < 40) {  //maggior parte delle piante devono stare tra 20% e 60% di umidità, quindi è stato impostato 40% come soglia
    analogWrite(motor_pin, 255); //max potenza
    delay(10000); //bagna per 10 secondi
    analogWrite(motor_pin, 0); 
    Serial.print("Pianta innaffiata!");
    delay(10000); //tempo di attesa perché l'acqua si distribuisca bene, prima di riprendere le misure > non più valido se si mette su esp
    }   
    */
   delay(1000);

  // Come scrivere cose 
  // String key = firebase.pushString("push", "");
  // key = key.substring(9,29);
  // firebase.setString("push/"+key+"/id", "Nome misura");
  // firebase.setFloat("push/"+key + "/val", 12.32);
  // delay(1000);
 }
 else{
  server.handleClient();
 }

}
