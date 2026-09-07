package com.goldencrown.takeaway_backend.menu;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class MenuSeeder implements CommandLineRunner {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuSeeder(MenuItemRepository menuItemRepository, CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (menuItemRepository.count() > 0) {
            return;
        }

        Map<String, Category> categoryCache = new HashMap<>();

        try (Reader reader = new InputStreamReader(
                new ClassPathResource("menu-seed.csv").getInputStream(), StandardCharsets.UTF_8)) {

            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build();

            CSVParser parser = format.parse(reader);

            for (CSVRecord record : parser) {
                String categoryName = record.get("category");

                Category category = categoryCache.get(categoryName);
                if (category == null) {
                    category = categoryRepository.findByName(categoryName)
                            .orElseGet(() -> categoryRepository.save(
                                    new Category(categoryName, categoryCache.size())));
                    categoryCache.put(categoryName, category);
                }

                MenuItem item = new MenuItem(
                        record.get("name"),
                        record.get("description"),
                        new BigDecimal(record.get("price")),
                        category
                );
                item.setVegetarian(Boolean.parseBoolean(record.get("vegetarian")));
                item.setSpicy(Boolean.parseBoolean(record.get("spicy")));
                item.setContainsNuts(Boolean.parseBoolean(record.get("containsNuts")));

                String imageUrl = record.get("imageUrl");
                if (imageUrl != null && !imageUrl.isBlank()) {
                    item.setImageUrl(imageUrl);
                }

                String nameZh = record.get("nameZh");
                if (nameZh != null && !nameZh.isBlank()) {
                    item.setNameZh(nameZh);
                }

                menuItemRepository.save(item);
            }
        }
    }
}
