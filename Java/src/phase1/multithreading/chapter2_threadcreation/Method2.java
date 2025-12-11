package chapter2_threadcreation;

// Abstract Class
public class Method2 extends Thread {
  
  private String name;
  private Thread t;

  Method2(String name) {
    this.name = name;
    System.out.println("Creating " +  name );
  }

  @Override
  public void run() {
    // TODO Auto-generated method stub
    System.out.println("Running " + name);
    try {
      for (int i = 4; i > 0; i--) {
        System.out.println("Thread: " + name + ", " + i);
        // Let the thread sleep for a while.
        Thread.sleep(50);
      }
    } catch (InterruptedException e) {
      System.out.println("Thread " + name + " interrupted.");
    }
    System.out.println("Thread " + name + " exiting.");
  }

  public void start() {
    if (t == null) {
      t = new Thread(this,name);
    }
    t.start();
  }
  
  public static void main(String[] args) {
    Thread t = new Method2("Thread 2");
    Thread t1 = new Method2("Thread 1");
    t.start();
    t1.start();
  }
  
}
