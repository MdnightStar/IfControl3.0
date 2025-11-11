/*
 *Autores: Jeison Andres, Cauã Adriel e Axel Miguel (Orientador: Jucimar)
 *Descrisao: Codigo de um servidor web que aguarda receber uma requsição de um socket
 *Data: 11/2024
 *Projeto: IFControl 3.1
 */

// BIBLIOTECAS
#include <Arduino.h>        // Bibliteca predeterminada do arduino.
#include <SPI.h>            // Biblioteca a descobrir...
#include <DHT.h>            // Biblioteca para o sensor de temperatura/humidade.
#include <SoftwareSerial.h> // Biblioteca para comunicação com o ESP32.

// Bibliotecas destinadas ao envio do sinal IR:
// Ar-condicionado:
#include <HeatpumpIR.h>

// Pinos disponíveis:
// 2, 3, 5, 6, 7, 8, 9, A0, A1, A2, A3, A4, A5

// VARIÁVEIS, CONSTANTES E DEFINIÇÕES:


// Sensor de temperatura/humidade:
#define DHTPIN A0     // pino que estamos conectado
#define DHTTYPE DHT11  // DHT 11
DHT dht(DHTPIN, DHTTYPE);

// Temperatura limite:
int t = 25;

// Sensor de presenca:
boolean temGente;  // Boolean para determinar a presença no momento da leitura.
boolean presenca;  // Boolean para determinar a presença a longo prazo.
int falsos;        // Contador em milisegundos.

// Pinos:
int pinSensor = 6;  // sensor de presença e movimento.
int pinRele = 8;    // pino relé.

//Destinados para o led IR (Corrigir incongruencias, o arquivo também define o pino do led IR para o data-show).
#ifndef ESP8266
IRSenderPWM irSender(9);  // IR led on Arduino digital pin 9, using Arduino PWM.
//IRSenderBlaster irSender(3); // IR led on Arduino digital pin 3, using IR Blaster (generates the 38 kHz carrier).
#else
IRSenderBitBang irSender(D1);  // IR led on Wemos D1 mini pin 'D1'.
#endif

// Variáveis de configuração
int IR_ONE_SPACE;
int IR_ZERO_SPACE;
int IR_BIT_MARK;
int IR_PAUSE_SPACE;
int IR_HEADER_MARK;
int IR_HEADER_SPACE;

// Código IR
String cod;

// Serial
SoftwareSerial mySerial(10, 11); // RX, TX (invertido fisicamente)

void setup() {
  Serial.begin(115200);
  mySerial.begin(9600);
  // Sensor de presença:
  falsos = 0;
  presenca = true;                           

  // Sensor de temperatura/humidade:
  dht.begin();

  // Relé:
  pinMode(pinRele, OUTPUT);  // Inicializa o pino do relé.

  // Sensor de movimento:
  pinMode(pinSensor, INPUT);  //Define sensorPin como entrada
}

void loop() {
  delay(1000);
  receberEsp32();

}  // fim do loop

void receberEsp32(){
  if (mySerial.available()) {
    String recebido = mySerial.readStringUntil('\n');
    if(recebido.length()>0){
    Serial.print("Recebido do ESP32: ");
    Serial.println(recebido);
    processarString(recebido);
    }
  }
}

void processarString(String str) {

  if (str.indexOf("CODIR") > -1) {  // Se solicitar uma operação com um dis
    sendRaw(extrairConfECod(str));
     mySerial.print("OK");
  } else if (str.indexOf("TEMP") > -1) {  // se solicitar a temperatura
    float temp = dht.readTemperature();   // leitura da temperatura
    if ((isnan(temp))) {  // caso ocorra falha da obtenção dos valores
      mySerial.print(0);
    } else {
      mySerial.print(temp);
    }
  } else if (str.indexOf("UMIDADE") > -1) {
    float h = dht.readHumidity();
    if ((isnan(h))) {  // caso ocorra falha da obtenção dos valores
      mySerial.print(0);
    } else {
      mySerial.print(h);
    }
  } else if (str.indexOf("PRESENCA") > -1) {
    if (presenca == HIGH)
      mySerial.print("TRUE");
    else
      mySerial.print("FALSE");
  } else if (str.indexOf("LZ") > -1) {
     if (str.indexOf("ON") > -1) {
      digitalWrite(pinRele, HIGH);
      mySerial.print("OK");
    } else {
      digitalWrite(pinRele, LOW);
      mySerial.print("OK");
    }
  }
}  // HAVE FUN do método processRequest

char* extrairConfECod(String entrada){
  int colcheteInicio = entrada.lastIndexOf('[');
  int colcheteFim = entrada.lastIndexOf(']');

  // Extrair config como string
  String configStr = entrada.substring(colcheteInicio + 1, colcheteFim);
  String valores[6];
  int idx = 0;

  while (configStr.length() > 0 && idx < 6) {

    
    int sep = configStr.indexOf(',');
    if (sep == -1) {
      valores[idx++] = configStr;
      break;
    } else {
      valores[idx++] = configStr.substring(0, sep);
      configStr = configStr.substring(sep + 1);
    }
  }

  // Converter para inteiros
  IR_ONE_SPACE    = valores[0].toInt();
  IR_ZERO_SPACE   = valores[1].toInt();
  IR_BIT_MARK     = valores[2].toInt();
  IR_PAUSE_SPACE  = valores[3].toInt();
  IR_HEADER_MARK  = valores[4].toInt();
  IR_HEADER_SPACE = valores[5].toInt();

  // Extrair código IR (parte depois do colchete fechado + vírgula)
  cod = entrada.substring(colcheteFim + 2); // pula o "]" e a vírgula

  // Cria um buffer com o tamanho da string + 1 (para o caractere nulo '\0')
  char* symbols = new char[cod.length() + 1];  // Alocação dinâmica

  // Copia o conteúdo da String para o buffer
  cod.toCharArray(symbols, cod.length() + 1);

  return symbols;
}

void sendRaw(char *symbols) {
  irSender.space(0);
  irSender.setFrequency(38);

  while (char symbol = *symbols++) {
    switch (symbol) {
      case '1':
        irSender.space(IR_ONE_SPACE);
        irSender.mark(IR_BIT_MARK);
        break;
      case '0':
        irSender.space(IR_ZERO_SPACE);
        irSender.mark(IR_BIT_MARK);
        break;
      case 'W':
        irSender.space(IR_PAUSE_SPACE);
        irSender.mark(IR_BIT_MARK);
        break;
      case 'H':
        irSender.mark(IR_HEADER_MARK);
        break;
      case 'h':
        irSender.space(IR_HEADER_SPACE);
        irSender.mark(IR_BIT_MARK);
        break;
    }
  }

  irSender.space(0);
}

