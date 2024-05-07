package io.github.kpodsiadlo7.transfermusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TransferMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransferMusicApplication.class, args);
    }

}
