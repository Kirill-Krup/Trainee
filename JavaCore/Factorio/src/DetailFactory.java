import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DetailFactory extends Thread {
    private List<Parts> allParts = new ArrayList<>();
    private int MAX_PARTS_PER_DAY = 10;
    private boolean isDay = true;
    private final Object lock = new Object();

    public void run(){
        for(int i = 0; i < 100; i++){
            synchronized(lock) {
                System.out.println("Делаем запчасти " + i + " повтор");
                doParts();
                isDay = false;
                System.out.println("Наступила ночь");
                lock.notifyAll();

                while (!allParts.isEmpty()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.println("Наступил день");
                isDay = true;
            }
        }
    }

    private void doParts(){
        allParts.clear();
        for(int i = 0; i < MAX_PARTS_PER_DAY; i++){
            allParts.add(generateRandomPart());
        }
    }

    private Parts generateRandomPart(){
        Parts[] parts = Parts.values();
        return parts[ThreadLocalRandom.current().nextInt(parts.length)];
    }

    public List<Parts> takeParts(int value) throws InterruptedException {
        synchronized(lock) {
            while (isDay || allParts.isEmpty()) {
                System.out.println("Броооо, жду, чтобы забрать запчасти");
                lock.wait();
            }

            List<Parts> partsForOrder = allParts.stream().limit(value).collect(Collectors.toList());
            allParts = allParts.stream().skip(value).collect(Collectors.toList());
            if (allParts.isEmpty()) {
                lock.notifyAll();
            }
            return partsForOrder;
        }
    }
}
