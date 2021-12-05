package dev.lightdream.originalpanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OriginalPanelApplication {

    public static void main(String[] args) {
        SpringApplication.run(OriginalPanelApplication.class, args);

        new Main();
    }

}
