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
 *  - Each DishExtra also carries a Chinese name (nameZh), printed on the
 *    kitchen copy of receipts.
 */
public final class ExtrasCatalog {

    private ExtrasCatalog() {}

    private static final List<DishExtra> MEAT_EXTRAS = List.of(
            new DishExtra("Extra Chicken", new BigDecimal("1.50"), "加鸡肉"),
            new DishExtra("Extra Beef", new BigDecimal("1.80"), "加牛肉"),
            new DishExtra("Extra Prawns", new BigDecimal("2.00"), "加虾"),
            new DishExtra("Extra Pork", new BigDecimal("1.50"), "加猪肉")
    );

    private static final List<DishExtra> PANCAKE_EXTRAS = List.of(
            new DishExtra("Extra pancake", new BigDecimal("1.00"), "加薄饼")
    );

    private static final List<DishExtra> SALT_PEPPER_EXTRAS = List.of(
            new DishExtra("Extra Large", new BigDecimal("2.00"), "加大"),
            new DishExtra("Extra Spicy", new BigDecimal("0.4"), "加辣"),
            new DishExtra("Extra Onion & Garlic", new BigDecimal("0.50"), "加葱蒜"),
            new DishExtra("No Spicy", new BigDecimal("0"), "不要辣")
    );

    private static final List<DishExtra> CHANGE_SAUCE_EXTRAS = List.of(
            new DishExtra("Sweet & Sour Sauce", new BigDecimal("0"), "甜酸汁"),
            new DishExtra("Curry Sauce", new BigDecimal("0"), "咖喱汁"),
            new DishExtra("BBQ Sauce", new BigDecimal("0"), "烧烤汁"),
            new DishExtra("Sweet Chilli Sauce", new BigDecimal("0"), "甜辣酱"),
            new DishExtra("Black Bean Sauce", new BigDecimal("1.50"), "豉汁"),
            new DishExtra("Kung Po Sauce", new BigDecimal("1.50"), "宫保汁")

    );

    private static final List<DishExtra> RICE_EXTRAS = List.of(
            new DishExtra("Extra Chicken", new BigDecimal("1.00"), "加鸡肉"),
            new DishExtra("Extra Beef", new BigDecimal("1.00"), "加牛肉"),
            new DishExtra("Extra Pork", new BigDecimal("1.00"), "加猪肉"),
            new DishExtra("Extra Prawn", new BigDecimal("2.00"), "加虾"),
            new DishExtra("Extra Shrimp", new BigDecimal("1.00"), "加虾仁"),
            new DishExtra("Extra Duck", new BigDecimal("2.00"), "加鸭肉"),
            new DishExtra("Extra peas", new BigDecimal("0.30"), "加豌豆"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Spicy with Chilli Oil", new BigDecimal("0.50"), "加辣椒油"),
            new DishExtra("Extra Soy Sauce", new BigDecimal("0.30"), "加酱油")
    );

    private static final List<DishExtra> COMMON_EXTRAS = List.of(
            new DishExtra("Extra Vegetables", new BigDecimal("1.00"), "加菜"),
            new DishExtra("Extra Egg", new BigDecimal("0.80"), "加蛋"),
            new DishExtra("Extra Sauce", new BigDecimal("0.50"), "加酱"),
            new DishExtra("Extra Spring Onion", new BigDecimal("0.50"), "加葱"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣")

    );

    private static final Map<String, List<DishExtra>> DISH_EXTRAS = Map.ofEntries(
            Map.entry("Quarter Crispy Aromatic Duck", combine(PANCAKE_EXTRAS)),
            Map.entry("Half Crispy Aromatic Duck", combine(PANCAKE_EXTRAS)),
            Map.entry("Whole Crispy Aromatic Duck", combine(PANCAKE_EXTRAS)),

            Map.entry("Butterfly King Prawn (8)", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Sweet & Sour Chicken Balls", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Sweet & Sour Pork Balls", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Sweet & Sour Prawn Balls", combine(CHANGE_SAUCE_EXTRAS)),
            
            Map.entry("Salt & Pepper Spare Ribs", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Salt & Pepper Squid", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Salt & Pepper Prawns (10)", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Salt & Pepper Tofu/Bean Curd", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Salt & Pepper Chicken Wings", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Salt & Pepper Pork", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Salt & peppers Chicken", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Salt & Pepper Chips", combine(CHANGE_SAUCE_EXTRAS)),

            Map.entry("Yeung Chow/Special Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Thai Style Special Friend Rice", combine(RICE_EXTRAS)),
            Map.entry("Chicken Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Thai Style Chicken Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Beef Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Thai Style Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Roast Pork Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Thai Style Pork Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("King Prawns Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Thai Style King Prawn Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Shrimp Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Singapore Style Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Egg Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Thai Style Egg Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Chicken & Pineapple Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Beef & Pineapple Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("Roast Pork & Pineapple Fried Rice", combine(RICE_EXTRAS)),
            Map.entry("King Prawn & Pineapple Fried Rice", combine(RICE_EXTRAS)),
 






            Map.entry("Kung Po Chicken", combine(MEAT_EXTRAS, COMMON_EXTRAS)),
            Map.entry("Sweet & Sour Pork HongKong Style", combine(MEAT_EXTRAS,  COMMON_EXTRAS)),
            Map.entry("Beef with Green Pepper in Black Bean Sauce", combine(MEAT_EXTRAS, COMMON_EXTRAS)),
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
