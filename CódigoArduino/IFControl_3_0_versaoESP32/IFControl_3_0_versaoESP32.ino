/*
 *Autores: Jeison Andres, Cauã Adriel e Axel Miguel (Orientador: Jucimar)
 *Descrisao: Codigo de um servidor web que aguarda receber uma requsição de um socket
 *Data: 08/2025
 *Projeto: IFControl 3.1(versão ESP32)
 */

#include <WiFi.h> // Biblioteca para a conexão com WiFi.
#define RXD2 16
#define TXD2 17

// WiFi:
const char *ssid = "IfControlNet";
const char *password = "IfControl";
WiFiServer server(808);

// Recebimento do request:
char c;      // Requisição é recebida byte pot byte.
String req;  // String armazena toda a requisicao.

void setup() {
  Serial.begin(115200);
  Serial2.begin(9600, SERIAL_8N1, RXD2, TXD2);


  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected.");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  server.begin();
  Serial.println("Servidor TCP iniciado. Aguardando conexões...");
}

void loop() {
  delay(1000);
  receberReq();
}

void receberReq() {
  WiFiClient client = server.available();// tenta estabeler uma conexão com o cliente
  if (client) {     
    Serial.println("Cliente conectado!");              // se conexão foi estabelecida
    while (client.connected()) {  // enquanto conexão estiver estabelecida

      if (client.available()) {  // se possui algum dado para ler

        c = client.read();  // leitura dos dados byte por byte
        req += c;           // incrementa dados recebidos na String

        if (c == '.') {                  // fim da mensagem
          Serial.println("Recebido do servidor: "+req);
          enviarArduino(req);  // função para analizar a mengagem recebida
          String resp=receberArduino();
          client.print(resp);
          req = "";                      // limpa dos dados da String
          break;                         // sai do while
        }else{
          if(c==':'){
            client.print("");
            break;
          }
        }

      }  // fim do recebimento da mensagem

    }  // fim da conexão com o cliente
    client.stop();
  }
    // quebra da conexão com o cliente
}


String receberArduino(){
   if (Serial2.available()) {
    String recebido = Serial2.readStringUntil('\n');
    Serial.print("Recebido do Arduino: ");
    Serial.println(recebido);
    return recebido;
  }
}

void enviarArduino(String msg) {
  Serial2.println(msg);
  delay(1000);
}
