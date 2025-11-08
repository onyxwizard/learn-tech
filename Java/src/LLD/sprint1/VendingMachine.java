package LLD.sprint1;

enum DrinkType{
    COKE(25,2),
    PEPSI(20,2),
    WATER(10,2);

    private final int bottleCost, drinkCount;
    DrinkType(int cCost, int count) {
        bottleCost = cCost;
        drinkCount = count;
    }

    public int getBottlePrice(){ return bottleCost;}
    public int getBottleCount(){ return drinkCount;}
    public int setBottleCount(int i){ return drinkCount-i;}

}

enum CashType {TEN(10),TWENTY(20),FIFTY(50);
    private int cash;
    CashType(int c) {
        cash = c;
    }

    public int getCash(){
        return cash;
    }
}

public class VendingMachine {
    private int money;
    private DrinkType d;
    private CashType type;
    private int bottleCount;

    public VendingMachine(int change,CashType t, DrinkType drink, int count){

        if(t.getCash() != change){
            System.out.println("Not valid");
            return;
        }
        this.bottleCount = count;
        this.d = drink;
        this.money = change;
        this.type = t;
    }

    public boolean checkBalance(){
        if(this.money < this.d.getBottlePrice())
        {
            System.out.println("INSERT MORE MONEY");
            return false;
        }
        int cal = this.money - this.d.getBottlePrice();
        System.out.println("Balance Money : " + cal);
        return true;
    }

    public boolean checkBottleAvailability(){
        if(this.d.getBottleCount() <= this.bottleCount)
        {
            System.out.println("NO Stock");
            return false;
        }
        this.d.setBottleCount(this.bottleCount);
        return true;
    }
}
