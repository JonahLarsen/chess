package webSocketMessages.serverMessages;

import com.google.gson.Gson;

public class Notification extends ServerMessage {
  private final String message;

  public Notification (String notification) {
    super(ServerMessageType.NOTIFICATION);
    this.message = notification;
  }

  public String getMessage() {
    return message;
  }

}
