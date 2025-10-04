package concepts.multithreading;

public class SimpleThreadClass extends Thread{
    @Override
    public void run() {
        System.out.println("inside a custome thread");
    try {
        Thread.sleep(1000);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread1= new SimpleThreadClass();
        Thread thread2 = new Thread("thread2");
       
        System.out.println("group name 1 " +thread1.getThreadGroup());
       
        System.out.println("group name 2 "+thread2.getThreadGroup());
         thread2.start();
         thread1.start();
        // thread1.wait();
         thread1.join(100);

         virtual
    }
}
