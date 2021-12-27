int soil_pin = A0; // AOUT pin on sensor



void setup() {

  // put your setup code here, to run once:



  Serial.begin(9600);

  analogReference(EXTERNAL);

}



void loop() {

  // put your main code here, to run repeatedly:



  Serial.print("Soil Moisture Sensor Voltage: ");

  Serial.print((float(analogRead(soil_pin))/1023.0)*3.3); // read sensor

  Serial.println(" V");

  delay(100); // slight delay between readings



}
