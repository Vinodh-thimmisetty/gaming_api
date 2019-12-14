package com.sutherland;

import com.sutherland.domain.GamingInfo;
import com.sutherland.entity.AuthKeys;
import com.sutherland.entity.GamingEntity;
import com.sutherland.repository.AuthRepository;
import com.sutherland.repository.GamingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * @author vinodh kumar thimmisetty
 */
@SpringBootApplication
@Slf4j
public class GamingApiApplication {

    @Value("${csv.file.path}")
    private String filePath;

    public static void main(String[] args) {
        SpringApplication.run(GamingApiApplication.class, args);
    }

    @Bean
    public ApplicationRunner loadCSVDate(final GamingRepository gamingRepository,
                                         final AuthRepository authRepository) {
        return (x) -> {
            Set<GamingEntity> games = new HashSet<>();
            try {
                Scanner sc = new Scanner(this.getClass().getResourceAsStream(filePath));
                // skip header info
                sc.nextLine();
                while (sc.hasNextLine()) {
                    games.add(convertToEntity(sc.nextLine()));
                }
            } catch (Exception e) {
                log.error("CSV File not exists in desired path {}", e.getMessage());
                e.printStackTrace();
            }
            gamingRepository.saveAll(games);

            // Sample Keys
            authRepository.save(AuthKeys.builder().key("vinodhkumar9898").build());
            authRepository.save(AuthKeys.builder().key("vinodhkumar4200").build());
            authRepository.save(AuthKeys.builder().key("vinodhkumar8080").build());
        };
    }

    /**
     * This will convert the CSV data to DB Entity.
     * It will look for any field internally having the COMMA and save it properly.
     *
     * @param input
     * @return
     */
    private GamingEntity convertToEntity(final String input) {
        String[] split = new String[5];
        final String[] current = input.split(",");
        if (current.length == 5) {
            split = current;
        } else {
            List<String> temp = new ArrayList<>(5);
            final char[] chars = input.toCharArray();
            int index = 0, start = 0;
            boolean isDoubleQuoteExist = false;

            for (final char s : chars) {
                // Check for comma, if it is inside quote, don't split it
                if (s == ',' &&
                        !isDoubleQuoteExist) {
                    temp.add(input.substring(start, index));
                    start = index + 1;
                }
                if (s == '"') {
                    isDoubleQuoteExist = !isDoubleQuoteExist;
                }
                index++;
            }
            temp.add(input.substring(start, index));

            split = temp.stream().map(x -> x.replace("\"", "")).toArray(String[]::new);

        }
        return new GamingEntity(new GamingInfo(split));
    }
}
