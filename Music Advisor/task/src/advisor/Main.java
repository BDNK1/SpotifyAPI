package advisor;

import java.util.HashMap;
import java.util.Map;


public class Main {

    public static void main(String[] args) {
        Map<String, String> argsMap = new HashMap<>();
        if (args.length >= 2) {
            for (int i = 0; i < args.length - 1; i++) {
                argsMap.put(args[i], args[i + 1]);
            }
        }
        MusicApp app = new MusicApp(argsMap);
        app.run();

    }
}