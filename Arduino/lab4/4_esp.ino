#include <Wire.h>
#include <ESP8266Firebase.h>
#include <ESP8266WiFi.h>

#define _SSID "marco"       
#define _PASSWORD "asdasdasd"    
#define PROJECT_ID "did2021-f44c9-default-rtdb"  //lo trovo in project settings di Firebase

Firebase firebase(PROJECT_ID);

void setup() {
  Serial.begin(115200);
  Wire.begin();   //Configura il dispositivo come MASTER

  // Connect to WiFi
  Serial.println();
  Serial.println();
  Serial.print("Connecting to: ");
  Serial.println(_SSID);
  WiFi.begin(_SSID, _PASSWORD);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print("-");
  }

  Serial.println("");
  Serial.println("WiFi Connected");

  // Print the IP address
  Serial.print("Use this URL to connect: ");
  Serial.print("http://");
  Serial.print(WiFi.localIP());
  Serial.println("/");
  digitalWrite(LED_BUILTIN, HIGH);

  //----

  
  }

  
void loop() {
  Serial.println("Requesting data....");
  Wire.requestFrom(1,4); // richiede 4 byte da dispositivo #1
  int i=0;
  byte data[4]; // buffer in cui leggere i dati
  while (Wire.available()) {
    data[i] = Wire.read();
    i++;
  }
  
  if (i==4) {
    int v1 = (data[1]<<8)+data[0];
    int v2 = (data[3]<<8)+data[2]; 
    
    firebase.setInt("Humidity", v1);
    firebase.setInt("Level", v2);

//  String key = firebase.pushInt("Humidity", v1);
    
//    Serial.print("humidity:");
//    Serial.println(v1);
//    Serial.print("level:");
//    Serial.println(v2);
//    } else {
//    Serial.println("Error");
//    }
    delay(1000);
  }
}
