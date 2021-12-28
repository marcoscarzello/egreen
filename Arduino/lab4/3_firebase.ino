#include <ESP8266Firebase.h>
#include <ESP8266WiFi.h>

#define _SSID "marco"       
#define _PASSWORD "asdasdasd"    
#define PROJECT_ID "did2021-f44c9-default-rtdb"  //lo trovo in project settings di Firebase

Firebase firebase(PROJECT_ID);

void setup(){
  Serial.begin(115200);
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW);
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  delay(1000);

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

  // Examples of setting String, integer and float values.
  firebase.setString("Example/setString", "It's Working");
  firebase.setInt("Example/setInt", 123);
  firebase.setFloat("Example/setFloat", 45.32);

  // Examples of pushing String, integer and float values.
  String key = firebase.pushString("push", "Hello");
  Serial.println(key.substring(9,29));
  
  // Example of data deletion.
  firebase.deleteData("Example");
}

void loop() {
  String key = firebase.pushString("push", "");
  key = key.substring(9,29);
  firebase.setString("push/"+key+"/id", "Nome misura");
  firebase.setFloat("push/"+key + "/val", 12.32);
  delay(1000);

  
}
