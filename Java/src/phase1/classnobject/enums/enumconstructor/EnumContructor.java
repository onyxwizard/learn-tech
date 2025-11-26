package phase1.classnobject.enums.enumconstructor;

enum HttpStatus {
  OK(200, "ok"),
  NOT_FOUND(404, "Not Found"),
  INTERNAL_ERROR(500, "Not Found");

  private final int code;
  private final String msg;

  HttpStatus(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }
  
  public int code() {
    return code;
  }

  public String msg() {
    return msg;
  }

  public static HttpStatus fromCode(int code) {
    for (HttpStatus status : values()) {
      if (status.code == code) return status;
    }
    throw new IllegalArgumentException("Unknown Status"+code);
  }


}

public class EnumContructor {
  
}
