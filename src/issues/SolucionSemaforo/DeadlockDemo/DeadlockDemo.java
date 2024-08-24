package issues.SolucionSemaforo.DeadlockDemo;




import java.util.concurrent.Semaphore;

public class DeadlockDemo {

    public static void main(String[] args) throws InterruptedException {
        //se crean los objetos
        Object ob1 = new Object();
        Object ob2 = new Object();
        Object ob3 = new Object();
        //semaforo se inicia en 1
        Semaphore semaforo = new Semaphore(1);


        //se crean los hilos con patron diferenye
        Thread t1 = new Thread(new SyncThread(ob1, ob2, semaforo), "hilo1");
        Thread t2 = new Thread(new SyncThread(ob2, ob3, semaforo), "hilo2");
        Thread t3 = new Thread(new SyncThread(ob3, ob1, semaforo), "hilo3");

        //inicia
        t1.start();
        Thread.sleep(1000);
        t2.start();
        Thread.sleep(1000);
        t3.start();

        //se espera que termine
        t1.join();
        t2.join();
        t3.join();

        System.out.println("Finalizado");


    }
}

class SyncThread implements Runnable {

    public Object ob1;
    public Object ob2;
    private Semaphore semaforo;

    // Constructor inicia y semaforo
    public SyncThread(Object ob1, Object ob2, Semaphore semaforo) {
        this.ob1 = ob1;
        this.ob2 = ob2;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " generando lock en " + ob1 );

        try {
            semaforo.acquire();//adquiere permiso
            synchronized (ob1) {//adquiere bloqueo
                System.out.println(name + " lock generado en " +  ob1);
                work();//simula el trabajo
                System.out.println(name + " generando lock en " + ob2);
                synchronized (ob2) {
                    System.out.println(name + " lock generado en " +  ob2);
                    work();
                }
                System.out.println(name + " lock liberado en " +  ob2);

            }
            semaforo.release();
        }catch (InterruptedException ie){
            System.out.println("Sucedió un InterruptedException: " + ie.getMessage());
        }

        System.out.println(name + " lock liberado en " +  ob1 );
        System.out.println("Finalizó ejecución");


    }

    private void work(){
        try {
            Thread.sleep(7000);

        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}