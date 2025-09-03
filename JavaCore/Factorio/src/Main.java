
public class Main {
    public static void main(String[] args) throws InterruptedException {
        DetailFactory factory = new DetailFactory();
        Fraction world = new Fraction("world", factory);
        Fraction wednesday = new Fraction("wednesday", factory);
        factory.start();
        world.start();
        wednesday.start();

        factory.join();
        world.join();
        wednesday.join();
        int worldArmy = world.calculateArmy();
        int wednesdayArmy = wednesday.calculateArmy();
        System.out.println("World: "+ worldArmy + "   " + "\nWednesday: "+ wednesdayArmy);
        System.out.println(worldArmy>wednesdayArmy ? "World wins with army of " + worldArmy  + " robots" : "Wednesday wins with army of " + wednesdayArmy  + " robots");
    }
}