

#include <ESP8266WiFi.h>
#include <ESP8266WebServer.h>
#include <WiFiClient.h>



const char* ssid = "ssid";
const char* password = "pass";
ESP8266WebServer server(80); 

void handleRoot() {
  server.send(200, "text/plain", "hello from esp8266!");
}


void setup(){
  Serial.begin(115200);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
    
    Serial.print("Connecting to ");
    Serial.print(ssid);
    Serial.print("...");

  int time = 0;
  while (WiFi.status() != WL_CONNECTED && time <= 10000){
    delay(500);
          Serial.print("trying...");

    time += 500;
  }
  if (WiFi.status() == WL_CONNECTED){
  IPAddress myAddress = WiFi.localIP();  //per sapere quale indirizzo mi è stato assegnato
      
      Serial.print("Successfully connected to ");
      Serial.println(ssid);
      Serial.print("IP Address: ");
      Serial.println(myAddress);  
  }
  else {

    WiFi.mode(WIFI_AP);
    Serial.println("switching to AP");
    WiFi.softAP("schedina", "asdasdasd", 1, false, 8);
    //hConn = WiFi.onSoftAPModeStationConnected(conn);
   // hDisc = WiFi.onSoftAPModeStationDisconnected(disc);
    IPAddress myIP = WiFi.softAPIP(); //Get IP address

    Serial.print("HotSpt IP:");
    Serial.println(myIP);
 
    server.on("/", handleRoot);      //Which routine to handle at root location

    server.begin();                  //Start server
    Serial.println("HTTP server started");

    }
}

void conn(const WiFiEventSoftAPModeStationConnected& e) {

}
void disc(const WiFiEventSoftAPModeStationDisconnected& e) {
}

void loop(){
  server.handleClient();
  }

 // nel metodo loop posso fare richieste di vario tipo, 
 //es. trasformare un dns in ip in modo da potermi connettere 
