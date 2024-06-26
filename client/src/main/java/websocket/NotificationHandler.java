package websocket;

import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {
  void receiveServerMessage(String message);

  void printPrompt();
}
