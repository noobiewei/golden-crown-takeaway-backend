package com.goldencrown.takeaway_backend.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Hand-maintained list of extras customers can add to specific dishes.
 * There's no database table or admin screen for this — edit the entries
 * below directly and restart the app to apply changes.
 *
 * How to edit:
 *  - Reusable groups of extras (MEAT_EXTRAS, RICE_NOODLE_EXTRAS, ...) are
 *    defined once below and can be shared across dishes.
 *  - DISH_EXTRAS maps a dish's exact name (must match menu-seed.csv) to
 *    the list of extras it offers. Combine groups with combine(...), or
 *    write a one-off List.of(...) for a dish that needs something unique.
 *  - A dish with no entry here simply won't show a "Customise" button.
 */
public final class ExtrasCatalog {

    private ExtrasCatalog() {}

    private static final List<DishExtra> MEAT_EXTRAS = List.of(
            new DishExtra("Extra Chicken", new BigDecimal("1.50")),
            new DishExtra("Extra Beef", new BigDecimal("1.80")),
            new DishExtra("Extra Prawns", new BigDecimal("2.00")),
            new DishExtra("Extra Pork", new BigDecimal("1.50"))
    );

    private static final List<DishExtra> RICE_NOODLE_EXTRAS = List.of(
            new DishExtra("Extra Rice", new BigDecimal("2.00")),
            new DishExtra("Extra Noodles", new BigDecimal("2.00"))
    );

    private static final List<DishExtra> COMMON_EXTRAS = List.of(
            new DishExtra("Extra Vegetables", new BigDecimal("1.00")),
            new DishExtra("Extra Egg", new BigDecimal("0.80")),
            new DishExtra("Extra Sauce", new BigDecimal("0.50")),
            new DishExtra("Extra Spring Onion", new BigDecimal("0.50"))
    );

    private static final Map<String, List<DishExtra>> DISH_EXTRAS = Map.ofEntries(
            Map.entry("Kung Po Chicken", combine(MEAT_EXTRAS, RICE_NOODLE_EXTRAS, COMMON_EXTRAS)),
            Map.entry("Sweet & Sour Pork", combine(MEAT_EXTRAS, RICE_NOODLE_EXTRAS, COMMON_EXTRAS)),
            Map.entry("Beef in Black Bean Sauce", combine(MEAT_EXTRAS, RICE_NOODLE_EXTRAS, COMMON_EXTRAS)),
            Map.entry("Chicken & Sweetcorn Soup", COMMON_EXTRAS),
            Map.entry("Mixed Hors D'Oeuvres", COMMON_EXTRAS)
    );

    @SafeVarargs
    private static List<DishExtra> combine(List<DishExtra>... groups) {
        return Stream.of(groups).flatMap(List::stream).toList();
    }

    public static List<DishExtra> forDish(String dishName) {
        return DISH_EXTRAS.getOrDefault(dishName, List.of());
    }

    public static Map<String, List<DishExtra>> all() {
        return DISH_EXTRAS;
    }
}
