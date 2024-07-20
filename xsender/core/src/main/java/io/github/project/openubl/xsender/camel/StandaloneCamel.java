package io.github.project.openubl.xsender.camel;

import lombok.Synchronized;
import org.apache.camel.main.Main;

public final class StandaloneCamel {

    private static StandaloneCamel instance;
    private final Main mainCamel;

    private StandaloneCamel() throws Exception {
        mainCamel = new Main(StandaloneCamel.class);
        mainCamel.start();
    }

    @Synchronized
    public static StandaloneCamel getInstance() {
        if (instance == null) {
            try {
                instance = new StandaloneCamel();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    public Main getMainCamel() {
        return mainCamel;
    }
}
