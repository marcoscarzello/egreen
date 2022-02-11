const char MAIN_page[] PROGMEM = R"=====(
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>

<body>

<h2><font color="green">E-green</font><h2>
<h3> Inserisci i dati della tua rete di casa per connettere il sistema al web! </h3>

<form action="/action_page">
  Rete:<br>
  <input type="text" name="nomerete" value="ssid">
  <br>
  Password:<br>
  <input type="text" name="password" value="pwd">
  <br><br>
  <input type="submit" value="Invia">
</form> 

</body>
</html>
)=====";
