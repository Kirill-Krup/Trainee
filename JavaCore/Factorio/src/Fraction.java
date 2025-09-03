import java.util.HashMap;
import java.util.List;

public class Fraction extends Thread{
    private String name;
    private DetailFactory factory;
    private HashMap<Parts, Integer> myInventory = new HashMap<>();

    public Fraction(String name, DetailFactory factory) {
        this.name = name;
        this.factory = factory;
    }

    public void run(){
        for(int i=0; i < 100; i++){
            List<Parts> newParts;
            try {
                newParts = factory.takeParts(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            addToInventory(newParts);
        }
    }

    private void addToInventory(List<Parts> newParts) {
        for(Parts p : newParts){
            myInventory.merge(p, 1, Integer::sum);
        }
    }

    public int calculateArmy(){
        int heads = myInventory.getOrDefault(Parts.Head,0);
        int torso = myInventory.getOrDefault(Parts.torso, 0);
        int hands = myInventory.getOrDefault(Parts.hand, 0)/2;
        int feet = myInventory.getOrDefault(Parts.feet, 0)/2;
        return Math.min(Math.min(heads, torso), Math.min(hands, feet));
    }
}
