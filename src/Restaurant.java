import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Semaphore;


/*
* Muhammad Saleh
* 20150211
* 
* Muhammdad Salah
* 20150212
*/
public class Restaurant {
    static Semaphore sem;
    static ArrayList<String> arrivedAndWaiting;
    static int tableNumber = 0;


    public static void main(String[] args) {

        arrivedAndWaiting = new ArrayList<>();
        System.out.print("What is number of Tables? ");
        int availableTables = new Scanner(System.in).nextInt();
        sem = new Semaphore(availableTables);
        HashMap<Integer, String> customer = new HashMap<>();

        System.out.print("Number of Customers : ");
        int numberOfCustomers = new Scanner(System.in).nextInt();

        for (int i = 0; i < numberOfCustomers; i++) {
            System.out.print("Customers Names : ");
            customer.put(i, new Scanner(System.in).nextLine());
        }


        for (int i = 0; i < numberOfCustomers; i++) {

            if (tableNumber < availableTables) {
                tableNumber++;
            } else {
                tableNumber = 1;
            }

            Arrive arrive = new Arrive(tableNumber, customer.get(i));
            arrive.start();
            if (i >= availableTables) {
                arrivedAndWaiting.add(customer.get(i));
            }
        }
        ArrivedAndWaiting arrivedAndWaiting = new ArrivedAndWaiting();
        arrivedAndWaiting.start();
    }

    static class Arrive extends Thread {
        String name = "";
        int TableNo;
        SitDown sitDown;

        public Arrive(int tableNo, String name) {
            this.name = name;
            this.TableNo = tableNo;
        }

        public void run() {

            try {
                if (!arrivedAndWaiting.contains(name)) {
                    System.out.println(name + " Arrived");
                }
                try {
                    Thread.sleep(2000);
                } finally {
                    sitDown = new SitDown(TableNo, name);
                    sitDown.start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
//    public synchronized void run() {
//
//        try {
//            if (!arrivedAndWaiting.contains(name)) {
//                System.out.println(name + " Arrived");
//            }
//            try {
//                Thread.sleep(2000);
//            } finally {
//                sitDown = new SitDown(TableNo, name);
//                sitDown.start();
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    static class ArrivedAndWaiting extends Thread {
        public void run() {

            try {

                try {
                    Thread.sleep(2000);
                } finally {
                    for (int i = 0; i < arrivedAndWaiting.size(); i++) {
                        System.out.println(arrivedAndWaiting.get(i) + " Arrived and Waiting");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    static class SitDown extends Thread {
        String name = "";
        int TableNo;
        Order order;

        public SitDown(int tableNo, String name) {
            this.name = name;
            this.TableNo = tableNo;
        }

        public void run() {

            try {
                sem.acquire();
                System.out.println("Table" + TableNo + ": " + name + " Sit down");
                try {
                    Thread.sleep(3000);
                } finally {
                    order = new Order(TableNo, name);
                    order.start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    static class Order extends Thread {
        String name = "";
        Eat eat;
        int TableNo;

        public Order(int table, String name) {
            this.name = name;
            this.TableNo = table;
        }

        public void run() {

            try {
                System.out.println(name + " Order Food");
                try {
                    Thread.sleep(5000);
                } finally {
                    eat = new Eat(TableNo, name);
                    eat.start();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    static class Eat extends Thread {
        String name = "";
        int TableNo;

        public Eat(int table, String name) {
            this.name = name;
            this.TableNo = table;
        }

        public void run() {

            try {
                System.out.println("Table" + TableNo + ": " + name + " Eat");
                try {

                    Thread.sleep(7000);
                } finally {
                    sem.release();
                    System.out.println(name + " Leave");


                }

            } catch (InterruptedException e) {
                e.printStackTrace();

            }

        }

    }

}

