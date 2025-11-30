package phase1.optional.advance;

import java.util.Optional;

enum NotificationType {
  EMAIL,
  SMS,
  IN_APP;
}

class User {
  private final String id;
  private final Optional<ContactInfo> contactInfo;
  // TODO: constructor, getters

  User(String id, Optional<ContactInfo> contactInfo) {
    this.id = id;
    this.contactInfo = contactInfo;
  }

  Optional<ContactInfo> getContactInfo() {
    return contactInfo;
  }

}

class ContactInfo {
  private final String email;
  private final String phoneNumber;

  // TODO: constructor, getters
  ContactInfo(String email, String phoneNumber) {
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  Optional<String> getEmail() {
    return Optional.ofNullable(email);
  }

  Optional<String> getNumber() {
    return Optional.ofNullable(phoneNumber);
  }

}

public class Notificationresolver {
  public static NotificationType resolvePreferredChannel(User user) {
    // Return:
    // 1. EMAIL — if email is non-null, non-blank, and contains '@'
    // 2. SMS — if phoneNumber is non-null, non-blank, and 10+ digits
    // 3. IN_APP — otherwise
    //
    // Rules:
    // ✅ Use ONLY: map, flatMap, filter, orElse, orElseGet
    // ❌ No if, no get(), no ternary, no exceptions in chain
    Optional<NotificationType> emailExist = user.getContactInfo().flatMap(ContactInfo::getEmail)
        .filter(email -> !email.isEmpty() && email.contains("@")).map(e -> NotificationType.EMAIL);
    
    Optional<NotificationType> smsExist = user.getContactInfo().flatMap(ContactInfo::getNumber)
        .filter(number -> number.length() >= 10 && number.matches("[0-9]+")).map(e -> NotificationType.SMS);
    

    return emailExist.orElseGet(()-> smsExist.orElse(NotificationType.IN_APP));

  }

  public static void main(String[] args) {
    User u1 = new User("1", Optional.of(new ContactInfo("alice@ex.com", "")));
    User u2 = new User("2", Optional.of(new ContactInfo("", "1234568900")));
    User u3 = new User("3", Optional.of(new ContactInfo("bad-email", "123"))); // invalid both
    User u4 = new User("4", Optional.empty()); // no contact info

    System.out.println(resolvePreferredChannel(u1)); // EMAIL
    System.out.println(resolvePreferredChannel(u2)); // SMS
    System.out.println(resolvePreferredChannel(u3)); // IN_APP
    System.out.println(resolvePreferredChannel(u4)); // IN_APP
  }
}
