import java.util.ArrayList;
import java.util.List;
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
                doParts();
                isDay = false;
                lock.notifyAll();

                while (!allParts.isEmpty()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
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
