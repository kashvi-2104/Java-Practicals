public class parkinglot {
    private int twoWheelers;
    private int fourWheelers;
    private final int twoCap; //if you initialise the variable here it will show error in assigning the value inside constructor.
    private final int fourCap;
    private static long revenue=0;

    public parkinglot(int twoCap, int fourCap){
        this.twoCap=twoCap;
        this.fourCap=fourCap;
    }

    public void park(String type){
        if(type=="two"){
            if(twoWheelers<twoCap){
                revenue+=20;
                twoWheelers++;
                System.out.println("twowheeler Parked");
                System.out.println("revenue: "+revenue);
            }
            else{
                System.out.println("Full");
            }
        }
        else if(type=="four"){
            if(fourWheelers<fourCap){
                revenue+=40;
                fourWheelers++;
                System.out.println("fourwheeler Parked");
                System.out.println("revenue: "+revenue);
            }
            else{
                System.out.println("Full");
            }
        }  
    }
    public void leave(String type){
        if(type=="two"){
            if(twoWheelers>0){
                twoWheelers--;
                System.out.println(twoWheelers+ " twowheelers left");
            }
        }
        else if(type=="four"){
            if(fourWheelers>0){
                fourWheelers--;
                System.out.println(fourWheelers+ " fourwheelers left");
            }
        }
    }
    public static void main(String[] args){
        parkinglot p=new parkinglot(5, 2);

        p.park("two");
        p.park("two");

        p.leave("two");

        p.park("four");
    }
}

