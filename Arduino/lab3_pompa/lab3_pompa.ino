int base = 6;
int speed = 0;

void setup() {
pinMode(base,OUTPUT);
Serial.begin(9600);
}

void loop() {
  // Increment the speed of the motor in three   
  // steps, each for 3 seconds

    digitalWrite(base,1);
    delay(100);
    digitalWrite(base,0);
   delay(1000);
  }
