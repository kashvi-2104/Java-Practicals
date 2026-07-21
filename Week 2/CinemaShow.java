public class CinemaShow {
    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked=0;

    public CinemaShow(String title, int capacity){
        this.title=title;
        this.capacity=capacity;
        seatsAvailable=capacity;
    }

    public CinemaShow(String title){
        this(title,100);
    }

    public boolean book(int n){
        if(n<=seatsAvailable){
            seatsAvailable-=n;
            totalBooked+=n;
            return true;
        }
        return false;
    }

    public void cancel(int n){
        seatsAvailable+=n;
        if(seatsAvailable>capacity){
            seatsAvailable=capacity;
        }
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public static int getTotalBooked() {
        return totalBooked;
    }

    public static void main(String[] args){
        CinemaShow c1=new CinemaShow("Spiderman",50);

        System.out.println("Book 10: " + c1.book(10));
        System.out.println("Seats Available: " + c1.getSeatsAvailable());

        c1.cancel(5);
        System.out.println("After Cancel 5");
        System.out.println("Seats Available: " + c1.getSeatsAvailable());

        System.out.println("Book 50: " + c1.book(50));
        System.out.println("Seats Available: " + c1.getSeatsAvailable());

        System.out.println("Total Booked: " + CinemaShow.getTotalBooked());
    }
}
