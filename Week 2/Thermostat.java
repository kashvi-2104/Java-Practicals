public class Thermostat{
    private int temp;
    private String location;
    private static final int MIN=16;
    private static final int MAX=30;
    private static int activeCount=0;

    public Thermostat(String location, int startTemp){
        this.location=location;
        if(startTemp<MIN || startTemp>MAX){
            startTemp=22;
        }
        else{
            this.temp=startTemp;
        }
        activeCount++;
    }

    public Thermostat(String location){
        this(location, 22);
    }

    public void raise(){
        if(temp<MAX){
            temp++;
        }
        else{
            System.out.println("Temp already at maximum(30)");
        }   
    }

    public void lower(){
        if(temp>MIN){
            temp--;
        }
        else{
            System.out.println("Temp already at minimum(16)");
        }   
    }

    public void getTemp(){
        System.out.println("Temp= "+temp);
    }

    public void getactivecount(){
        System.out.println("Count= "+activeCount);
    }
    public static void main(String args[]){

        Thermostat t1=new Thermostat("Classroom");
        Thermostat t2=new Thermostat("Hostelroom");

        for(int i=0;i<10;i++){
            t1.raise();
            t1.getTemp();
        }

        for(int i=0;i<20;i++){
            t1.lower();
            t1.getTemp();
        }
        t1.getactivecount();
}
}
