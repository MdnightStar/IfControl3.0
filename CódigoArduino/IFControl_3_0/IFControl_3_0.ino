/*
 *Autores: Jeison Andres, Cauã Adriel e Axel Miguel (Orientador: Jucimar)
 *Descrição: Código do Arduino que recebe comandos do ESP32, processa mensagens
 *           longas e envia respostas confiáveis.
 *Data: 2025
 *Projeto: IFControl 3.1 (Arduino)
 */

// BIBLIOTECAS
#include <Arduino.h>
#include <SPI.h>
#include <DHT.h>
#include <SoftwareSerial.h>
#include <HeatpumpIR.h>

// ---------------------------- CONFIGURAÇÕES ----------------------------

// Sensor DHT11
#define DHTPIN  A0
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

// Sensor de presença
int pinSensor = 6;
boolean temGente;
boolean presenca;
int falsos;

// Relé
int pinRele = 8;

// Infra Vermelho
#ifndef ESP8266
IRSenderPWM irSender(9);
#else
IRSenderBitBang irSender(D1);
#endif

// Buffer IR
int IR_ONE_SPACE;
int IR_ZERO_SPACE;
int IR_BIT_MARK;
int IR_PAUSE_SPACE;
int IR_HEADER_MARK;
int IR_HEADER_SPACE;

String cod;

// Comunicação ESP32
SoftwareSerial mySerial(10, 11); // RX, TX (invertido fisicamente)

// Buffer de recepção seguro
String bufferRx = "";
unsigned long lastReadTime = 0;

// Timeout
const unsigned long SERIAL_TIMEOUT = 500;  // 0.5s sem dados = mensagem finalizada


// ======================================================================
// SETUP
// ======================================================================
void setup() {
  Serial.begin(115200);
  mySerial.begin(9600);

  dht.begin();
  presenca = true;

  pinMode(pinRele, OUTPUT);
  pinMode(pinSensor, INPUT);

  Serial.println("Arduino iniciado e aguardando mensagens do ESP32...");
}


// ======================================================================
// LOOP
// ======================================================================
void loop() {
  receberEsp32();
  delay(10);
  Serial.print("RAM Livre: ");
  Serial.println(freeMemory());
}
int freeMemory() {
  extern int __heap_start, *__brkval;
  int v;
  return (int) &v - (__brkval == 0 ? (int) &__heap_start : (int) __brkval);
}


// ======================================================================
// 📌 RECEBE MENSAGENS LONGAS DO ESP32 (ROBUSTO E SEGURO)
// ======================================================================
void receberEsp32() {
  while (mySerial.available()) {
    char c = mySerial.read();
    bufferRx += c;
    lastReadTime = millis();
  }

  if (bufferRx.length() > 0 && millis() - lastReadTime > SERIAL_TIMEOUT) {
    Serial.print("[Arduino] Mensagem completa recebida: ");
    Serial.println(bufferRx);

    processarString(bufferRx);
    bufferRx = "";  // limpa para próxima mensagem
  }
}


// ======================================================================
// 📌 PROCESSA A MENSAGEM RECEBIDA
// ======================================================================
void processarString(String str) {

  if (str.startsWith("DIR")) {
    mySerial.print("OK");
    sendRaw((char*)extrairConfECod(str));

  } else if (str.startsWith("TEMP")) {

    float temp = dht.readTemperature();
    if (isnan(temp)) mySerial.print("0");
    else mySerial.print(temp);

  } else if (str.startsWith("UMIDADE")) {

    float h = dht.readHumidity();
    if (isnan(h)) mySerial.print("0");
    else mySerial.print(h);

  } else if (str.startsWith("PRESENCA")) {

    if (presenca) mySerial.print("TRUE");
    else mySerial.print("FALSE");

  } else if (str.startsWith("LZ")) {

    if (str.indexOf("ON") > -1) {
      digitalWrite(pinRele, HIGH);
      mySerial.print("OK");
    } else {
      digitalWrite(pinRele, LOW);
      mySerial.print("OK");
    }
  }
}


// ======================================================================
// 📌 EXTRAI CONFIGURAÇÕES + CÓDIGO IR DO COMANDO
// ======================================================================
char* extrairConfECod(String entrada) {
  int ini = entrada.indexOf('[');
  int fim = entrada.indexOf(']');

  String configStr = entrada.substring(ini + 1, fim);
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

  // Converte valores
  IR_ONE_SPACE    = valores[0].toInt();
  IR_ZERO_SPACE   = valores[1].toInt();
  IR_BIT_MARK     = valores[2].toInt();
  IR_PAUSE_SPACE  = valores[3].toInt();
  IR_HEADER_MARK  = valores[4].toInt();
  IR_HEADER_SPACE = valores[5].toInt();

  // Extrai string com códigos IR
  cod = entrada.substring(fim + 2);

  // Buffer estático: ajuste tamanho conforme necessário
  static char symbols[600];
  // Garante zero-termination e evita overflow
  cod.toCharArray(symbols, sizeof(symbols));

  return symbols;

}


// ======================================================================
// 📌 ENVIA O CÓDIGO IR RAW PARA O AR-CONDICIONADO
// ======================================================================
void sendRaw(char *symbols) {
  irSender.space(0);
  irSender.setFrequency(38);

  while (char s = *symbols++) {
    switch (s) {
      case '1': irSender.space(IR_ONE_SPACE);  irSender.mark(IR_BIT_MARK); break;
      case '0': irSender.space(IR_ZERO_SPACE); irSender.mark(IR_BIT_MARK); break;
      case 'W': irSender.space(IR_PAUSE_SPACE); irSender.mark(IR_BIT_MARK); break;
      case 'H': irSender.mark(IR_HEADER_MARK); break;
      case 'h': irSender.space(IR_HEADER_SPACE); irSender.mark(IR_BIT_MARK); break;
    }
  }

  irSender.space(0);
}






