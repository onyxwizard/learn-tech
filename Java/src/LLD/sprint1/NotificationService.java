package LLD.sprint1;

interface NotificationSender {
    void send(String recipient, String message);
}

class EmailSender implements NotificationSender {
    public void send(String to, String msg) {
        System.out.println("Email to " + to + ": " + msg);
    }
}

class SmsSender implements NotificationSender {
    public void send(String to, String msg) {
        System.out.println("SMS to " + to + ": " + msg);
    }
}

class NotificationService {
    private NotificationSender sender;

    public NotificationService(NotificationSender sender) {
        this.sender = sender;
    }

    public void send(String recipient, String message) {
        sender.send(recipient, message);
    }
}