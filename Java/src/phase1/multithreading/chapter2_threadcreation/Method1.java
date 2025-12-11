package chapter2_threadcreation;

// create using interface
public class Method1 implements Runnable {
  private String name;
  private Thread t;

  Method1(String name) {
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

  public static void main(String[] args) throws InterruptedException {
    Method1 runnable = new Method1("Thread 2");
    Method1 runnable1 = new Method1("Thread 1");
    /**
     * Do not run this type:
     * runnable.start(); // Not actually true cocurrency
     */
    
    //This is what actually true concurrency
    Thread t = new Thread(runnable, "Thread 2");
    Thread t1 = new Thread(runnable1, "Thread 1");
    t.start();
    t1.start();
    
  }

}