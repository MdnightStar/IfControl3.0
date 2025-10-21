# IFCONTROL3.0
# IFControl

**IFControl** é um sistema robusto de automação e monitoramento inteligente, desenvolvido como parte de um projeto de pesquisa do programa **PIBIC Jr** no **Instituto Federal do Amazonas (IFAM)**. O sistema visa transformar ambientes educacionais — como salas de aula e laboratórios — em espaços automatizados, eficientes e adaptáveis.

Este projeto é orientado por professores do IFAM e se destaca pela integração de tecnologias modernas em software, hardware e comunicação de rede. Ele está em constante evolução, com o objetivo de se tornar multiplataforma (desktop, web e mobile) e incorporar recursos avançados como controle por voz, visão computacional e integração com dispositivos diversos.

---

## 🧠 Visão Geral

O **IFControl** tem como foco principal a automação de ambientes escolares, permitindo que usuários autorizados possam:

- **Controlar remotamente** dispositivos físicos (luzes, ventiladores, cortinas, etc.).
- **Monitorar sensores** (presença, temperatura, luminosidade, som, etc.) em tempo real.
- **Registrar ações do usuário** e eventos do sistema com precisão.
- **Agendar rotinas automatizadas** (ligar/desligar aparelhos em horários programados).
- **Adaptar o sistema** a diferentes plataformas: desktop, web e mobile.

---

## 🔬 Tecnologias Utilizadas

### 🖥️ Software

- **Java (Swing + Java Sockets):**
  - Cliente: Interface gráfica para controle e visualização de dados.
  - Servidor: Responsável por receber comandos e repassá-los ao Arduino.
- **TCP/IP (Sockets):**
  - Comunicação em tempo real entre cliente, servidor e hardware..

### ⚙️ Hardware

- **Arduino UNO:**
  - Controle físico dos dispositivos e sensores conectados.
- **Sensores e atuadores:**
  - Presença (PIR), luminosidade (LDR), temperatura (DHT11/22), sonoros, relés para acionamento de dispositivos.
- **Protoboard, jumpers e fonte externa.**
- **Esquemático otimizado**, com preocupação na organização, eficiência e segurança da conexão dos módulos.

---

## 🧱 Estrutura do Projeto


---

## 🚀 Funcionalidades em Desenvolvimento

- [x] Controle e monitoramento em tempo real (desktop)
- [x] Registro de logs de ações e eventos
- [x] Comunicação estável via Sockets TCP/IP
- [ ] **Agendamento inteligente** de eventos automatizados
- [ ] **Controle por comandos de voz** (com NLP e reconhecimento offline)
- [ ] **Visão computacional** para reconhecimento de presença e gestos
- [ ] **Compatibilidade com dispositivos móveis**
- [ ] **Versão web integrada** com dashboards e controle remoto

---

## 📚 Orientação e Pesquisa

Este projeto é parte do programa **PIBIC Jr - IFAM**, sendo conduzido com orientação de **professores profissionais da área de TI, automação e engenharia**. A proposta se insere no contexto de **iniciação científica aplicada**, proporcionando ao estudante uma vivência prática com tecnologias modernas e pesquisa científica.

---

## 📈 Futuro e Aplicações

O IFControl está sendo planejado para se tornar um sistema **versátil e modular**, podendo ser aplicado em:

- Ambientes educacionais (salas de aula, laboratórios)

Com a expansão de funcionalidades como **controle por voz, visão computacional** e **agendamento automatizado**, o IFControl poderá se adaptar a diferentes contextos de uso com eficiência, usabilidade e segurança.

---


## 🤝 Agradecimentos

Agradecemos ao **IFAM** e aos professores orientadores pela confiança, apoio técnico e incentivo à pesquisa. Este projeto é reflexo da dedicação à ciência aplicada e ao impacto social da tecnologia.

---

## 📄 Licença

Este projeto está licenciado sob os termos da **MIT License** - veja o arquivo `LICENSE.md` para detalhes.

---

**Desenvolvido com propósito, dedicação e paixão pela inovação.**

