#include <FastLED.h>

#define NUM_LEDS 5
#define LED_PIN 2

CRGB leds[NUM_LEDS];

int trigger=6;
int echo=10;
long durata=0;
float distanza;
int maxDist=54;
int Drounded;
bool daAccendere[5];
float parziale = 0;
float depth = 0;
int aus = 0;

void setup() {
  FastLED.addLeds<WS2812B, LED_PIN, RGB>(leds, NUM_LEDS);
  
  pinMode(trigger, OUTPUT);
  pinMode(echo, INPUT);
  Serial.begin(9600);
}

void loop() {
  digitalWrite(trigger, LOW);
  delayMicroseconds(2);
  digitalWrite(trigger, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigger, LOW);

  durata = pulseIn(echo, HIGH);
  distanza = durata/58;
  Serial.println(distanza);

  if (distanza > 30) distanza = 30;
  depth = 5 - ((distanza-3) / 6);

  accendi(depth);

}

void accendi(float depth){

  Drounded = (int) depth;
  parziale = (float)(depth - Drounded)*100; //percentuale di value dell'ultimo led da accendere

  for (int i = 0; i < Drounded; i++)
    {
      daAccendere[i] = true;
    }
  for (int i = 0; i < Drounded; i++){

      if (daAccendere[i]){
          leds[i] = CRGB::White;
          
          FastLED.show();
          delay(1);
          leds[i] = CRGB::Black;
           FastLED.show();
          delay(1);
         aus = i+1;
        }
         accendiParziale(aus, parziale);
    }
        
}
void accendiParziale(int aus, float parziale){
  
           
           leds[aus] = CHSV(255, 255, parziale);
           Serial.println(parziale);
           FastLED.show();
           delay(1);
          leds[aus] = CRGB::Black;
           FastLED.show();

  }

void spegniTutto(){
  leds[0] = CRGB::Black; 
  leds[1] = CRGB::Black; 
  leds[2] = CRGB::Black; 
  leds[3] = CRGB::Black; 
  leds[4] = CRGB::Black; 
  FastLED.show();
}
