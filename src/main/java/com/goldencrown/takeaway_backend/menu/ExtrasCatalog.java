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


    private static final List<DishExtra> Noodles_EXTRAS = List.of(
            new DishExtra("Extra Chicken", new BigDecimal("1.00"), "加鸡肉"),
            new DishExtra("Extra Beef", new BigDecimal("1.00"), "加牛肉"),
            new DishExtra("Extra Pork", new BigDecimal("1.00"), "加猪肉"),
            new DishExtra("Extra Prawn", new BigDecimal("2.00"), "加虾"),
            new DishExtra("Extra Shrimp", new BigDecimal("1.00"), "加虾仁"),
            new DishExtra("Extra Duck", new BigDecimal("2.00"), "加鸭肉"),
            new DishExtra("Switch to Rice Noodles", new BigDecimal("0.50"), "换米粉"),
            new DishExtra("Extra Beansprout", new BigDecimal("0.30"), "加豆芽"),
            new DishExtra("Extra onions", new BigDecimal("0.30"), "加洋葱"),
            new DishExtra("Extra Veg", new BigDecimal("0.50"), "加菜"),
            new DishExtra("Extra Sauce", new BigDecimal("0"), "加酱"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Spicy with Chilli Oil", new BigDecimal("0.50"), "加辣椒油"),
            new DishExtra("Extra Soy Sauce", new BigDecimal("0.30"), "加酱油"),
            new DishExtra("No Veg", new BigDecimal("0.00"), "不要菜"),
            new DishExtra("No Onions", new BigDecimal("0.00"), "不要洋葱")
    );


    private static final List<DishExtra> CHICKEN_EXTRAS = List.of(
            new DishExtra("Extra Chicken", new BigDecimal("1.50"), "加鸡肉"),
            new DishExtra("Extra Large", new BigDecimal("1.50"), "加大"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Mushrooms", new BigDecimal("0.70"), "加蘑菇"),
            new DishExtra("Extra Onion", new BigDecimal("0.30"), "加洋葱"),
            new DishExtra("Extra Sauce", new BigDecimal("0.30"), "加酱"),
            new DishExtra("Extra CashewNut", new BigDecimal("1.00"), "加腰果"),
            new DishExtra("Extra Veg", new BigDecimal("0.80"), "加菜"),
            new DishExtra("No Veg", new BigDecimal("0.00"), "不要菜"),
            new DishExtra("No Onions", new BigDecimal("0.00"), "不要洋葱")
    );

    private static final List<DishExtra> BEEF_EXTRAS = List.of(
            new DishExtra("Extra Beef", new BigDecimal("2.00"), "加牛肉"),
            new DishExtra("Extra Large", new BigDecimal("2.00"), "加大"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Mushrooms", new BigDecimal("0.70"), "加蘑菇"),
            new DishExtra("Extra Onion", new BigDecimal("0.30"), "加洋葱"),
            new DishExtra("Extra Sauce", new BigDecimal("0.30"), "加酱"),
            new DishExtra("Extra CashewNut", new BigDecimal("1.00"), "加腰果"),
            new DishExtra("Extra Veg", new BigDecimal("0.80"), "加菜"),
            new DishExtra("No Veg", new BigDecimal("0.00"), "不要菜"),
            new DishExtra("No Onions", new BigDecimal("0.00"), "不要洋葱")
    );

    private static final List<DishExtra> PRAWN_EXTRAS = List.of(
            new DishExtra("Extra Prawn", new BigDecimal("3.00"), "加虾"),
            new DishExtra("Extra Large", new BigDecimal("3.00"), "加大"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Mushrooms", new BigDecimal("0.70"), "加蘑菇"),
            new DishExtra("Extra Onion", new BigDecimal("0.30"), "加洋葱"),
            new DishExtra("Extra Sauce", new BigDecimal("0.30"), "加酱"),
            new DishExtra("Extra CashewNut", new BigDecimal("1.00"), "加腰果"),
            new DishExtra("Extra Veg", new BigDecimal("0.80"), "加菜"),
            new DishExtra("No Veg", new BigDecimal("0.00"), "不要菜"),
            new DishExtra("No Onions", new BigDecimal("0.00"), "不要洋葱")
    );

    private static final List<DishExtra> PORK_EXTRAS = List.of(
            new DishExtra("Extra Pork", new BigDecimal("1.50"), "加猪肉"),
            new DishExtra("Extra Large", new BigDecimal("1.50"), "加大"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Mushrooms", new BigDecimal("0.70"), "加蘑菇"),
            new DishExtra("Extra Onion", new BigDecimal("0.30"), "加洋葱"),
            new DishExtra("Extra Sauce", new BigDecimal("0.30"), "加酱"),
            new DishExtra("Extra CashewNut", new BigDecimal("1.00"), "加腰果"),
            new DishExtra("Extra Veg", new BigDecimal("0.80"), "加菜"),
            new DishExtra("No Veg", new BigDecimal("0.00"), "不要菜"),
            new DishExtra("No Onions", new BigDecimal("0.00"), "不要洋葱")
    );

    private static final List<DishExtra> LAMB_EXTRAS = List.of(
            new DishExtra("Extra LAMB", new BigDecimal("3.00"), "加羊肉"),
            new DishExtra("Extra Large", new BigDecimal("3.00"), "加大"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Mushrooms", new BigDecimal("0.70"), "加蘑菇"),
            new DishExtra("Extra Onion", new BigDecimal("0.30"), "加洋葱"),
            new DishExtra("Extra Sauce", new BigDecimal("0.30"), "加酱"),
            new DishExtra("Extra Veg", new BigDecimal("0.80"), "加菜"),
            new DishExtra("No Veg", new BigDecimal("0.00"), "不要菜"),
            new DishExtra("No Onions", new BigDecimal("0.00"), "不要洋葱")
    );

    private static final List<DishExtra> DUCK_EXTRAS = List.of(
            new DishExtra("Extra DUCK", new BigDecimal("3.00"), "加鸭肉"),
            new DishExtra("Extra Large", new BigDecimal("3.00"), "加大"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Mushrooms", new BigDecimal("0.70"), "加蘑菇"),
            new DishExtra("Extra Onion", new BigDecimal("0.30"), "加洋葱"),
            new DishExtra("Extra Sauce", new BigDecimal("0.30"), "加酱"),
            new DishExtra("Extra Veg", new BigDecimal("0.80"), "加菜"),
            new DishExtra("No Veg", new BigDecimal("0.00"), "不要菜"),
            new DishExtra("No Onions", new BigDecimal("0.00"), "不要洋葱")
    );

    private static final List<DishExtra> VEG_EXTRAS = List.of(
            new DishExtra("Extra Large", new BigDecimal("1.00"), "加大"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Mushrooms", new BigDecimal("0.70"), "加蘑菇"),
            new DishExtra("Extra Onion", new BigDecimal("0.30"), "加洋葱"),
            new DishExtra("Extra Sauce", new BigDecimal("0.30"), "加酱"),
            new DishExtra("Extra Veg", new BigDecimal("0.80"), "加菜"),
            new DishExtra("Extra Broccoli", new BigDecimal("1.00"), "加西兰花"),
            new DishExtra("No Veg", new BigDecimal("0.00"), "不要菜"),
            new DishExtra("No Onions", new BigDecimal("0.00"), "不要洋葱")
    );

     private static final List<DishExtra> CURRY_EXTRAS = List.of(
            new DishExtra("Extra Meat", new BigDecimal("2.50"), "加肉"),
            new DishExtra("Add Small Boiled Rice", new BigDecimal("3.30"), "加小白饭"),
            new DishExtra("Add Small Egg Fried Rice", new BigDecimal("3.90"), "加小蛋炒饭"),
            new DishExtra("Extra Spicy", new BigDecimal("0.30"), "加辣"),
            new DishExtra("Extra Mushrooms", new BigDecimal("0.70"), "加蘑菇"),
            new DishExtra("Extra Onion", new BigDecimal("0.30"), "加洋葱"),
            new DishExtra("Extra Sauce", new BigDecimal("0.30"), "加酱"),
            new DishExtra("Extra Veg", new BigDecimal("0.80"), "加菜"),
            new DishExtra("Extra Peas", new BigDecimal("0.30"), "加豌豆"),
            new DishExtra("No Veg", new BigDecimal("0.00"), "不要菜"),
            new DishExtra("No Onions", new BigDecimal("0.00"), "不要洋葱")
    );



    private static final Map<String, List<DishExtra>> DISH_EXTRAS = Map.ofEntries(
            Map.entry("Quarter Crispy Aromatic Duck", combine(PANCAKE_EXTRAS)),
            Map.entry("Half Crispy Aromatic Duck", combine(PANCAKE_EXTRAS)),
            Map.entry("Whole Crispy Aromatic Duck", combine(PANCAKE_EXTRAS)),

            Map.entry("Butterfly King Prawn (8)", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Sweet & Sour Chicken Balls", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Sweet & Sour Pork Balls", combine(CHANGE_SAUCE_EXTRAS)),
            Map.entry("Sweet & Sour Prawn Balls", combine(CHANGE_SAUCE_EXTRAS)),
            
            Map.entry("Salt & Pepper Spare Ribs", combine(SALT_PEPPER_EXTRAS)),
            Map.entry("Salt & Pepper Squid", combine(SALT_PEPPER_EXTRAS)),
            Map.entry("Salt & Pepper Prawns (10)", combine(SALT_PEPPER_EXTRAS)),
            Map.entry("Salt & Pepper Tofu/Bean Curd", combine(SALT_PEPPER_EXTRAS)),
            Map.entry("Salt & Pepper Chicken Wings", combine(SALT_PEPPER_EXTRAS)),
            Map.entry("Salt & Pepper Pork", combine(SALT_PEPPER_EXTRAS)),
            Map.entry("Salt & peppers Chicken", combine(SALT_PEPPER_EXTRAS)),
            Map.entry("Salt & Pepper Chips", combine(SALT_PEPPER_EXTRAS)),

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
 
            Map.entry("Roast Duck ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("House Special on ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Yeung Chow/Special ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Chicken ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Beef ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Roast Pork ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("King Prawn ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Shrimp ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Singapore Style ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Beansprouts ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Mushrooms ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Mixed Vegetables on ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Singapore Style Fried Rice Noodles/Vermicelli", combine(Noodles_EXTRAS)),
            Map.entry("Chicken Singapore Style Friend Rice Noodles/Vermicelli", combine(Noodles_EXTRAS)),
            Map.entry("Vegetarian Singapore Style Fried Rice Noodles/Vermicelli", combine(Noodles_EXTRAS)),
            Map.entry("Seafood ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Crispy Noodles with Chicken in Black Bean Sauce", combine(Noodles_EXTRAS)),
            Map.entry("Crispy Noodles with Beef in Black Bean Sauce", combine(Noodles_EXTRAS)),
            Map.entry("Crispy Noodles with Roast Duck in Black Bean Sauce", combine(Noodles_EXTRAS)),
            Map.entry("Crispy Noodles with Mixed Vegetables in Black Bean Sauce", combine(Noodles_EXTRAS)),
            Map.entry("Seafood Fried Ho Fun", combine(Noodles_EXTRAS)),
            Map.entry("Singapore Style Fried Ho Fun", combine(Noodles_EXTRAS)),
            Map.entry("Chicken Fried Ho Fun", combine(Noodles_EXTRAS)),
            Map.entry("Beef Fried Ho Fun", combine(Noodles_EXTRAS)),
            Map.entry("Ho Fun with CHicken in Black Bean Sauce", combine(Noodles_EXTRAS)),
            Map.entry("Ho Fun with Beef in Black Bean Sauce", combine(Noodles_EXTRAS)),
            Map.entry("Ho Fun with Roast Duck in Black Bean Sauce", combine(Noodles_EXTRAS)),

            Map.entry("Satay Yeung Chow/Special ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Satay Chicken ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Satay Beef ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Satay Roast Pork ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Satay King Prawn ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Satay Shrimp ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Satay Beansprouts ChowMein", combine(Noodles_EXTRAS)),
            Map.entry("Satay Mushroom ChowMein", combine(Noodles_EXTRAS)),

            Map.entry("Chinese Style Chicken", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken with Mushroom", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken with Bamboo Shoots & Water Chestnuts", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken with Pineapple", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken with Mixed Vegetables", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken Chop Suey", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken with Cashew Nuts", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken with Green Ppepper in Black Bean Sauce", combine(CHICKEN_EXTRAS)),
            Map.entry("Szechuan Style Chicken", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken in Oyster Sauce", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken in Garlic Sauce", combine(CHICKEN_EXTRAS)),
            Map.entry("Satay Chicken", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken with Ginger & Spring Onion", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken in Yellow Bean Sauce", combine(CHICKEN_EXTRAS)),
            Map.entry("Thai Style Chicken", combine(CHICKEN_EXTRAS)),
            Map.entry("Chicken in Black Pepper Sauce", combine(CHICKEN_EXTRAS)),
            Map.entry("Kung Po Chicken", combine(CHICKEN_EXTRAS)),
            Map.entry("Crispy Shredded Chicken", combine(CHICKEN_EXTRAS)),
            Map.entry("Crispy Chicken", combine(CHICKEN_EXTRAS)),
            Map.entry("Lemon Chicken", combine(CHICKEN_EXTRAS)),


            Map.entry("Crispy Shredded Beef", combine(BEEF_EXTRAS)),
            Map.entry("Beef with Mushroom", combine(BEEF_EXTRAS)),
            Map.entry("Beef with Bamboo Shoots & Water Chestnuts", combine(BEEF_EXTRAS)),
            Map.entry("Beef with Pineapple", combine(BEEF_EXTRAS)),            
            Map.entry("Beef with Green Pepper in Black Bean Sauce", combine(BEEF_EXTRAS)),
            Map.entry("Beef Chop Suey", combine(BEEF_EXTRAS)),
            Map.entry("Szechuan Style Beef", combine(BEEF_EXTRAS)),
            Map.entry("Satay Beef", combine(BEEF_EXTRAS)),            
            Map.entry("Beef with Mixed Vegetables", combine(BEEF_EXTRAS)),
            Map.entry("Beef with Cashew Nuts", combine(BEEF_EXTRAS)),
            Map.entry("Beef with Ginger & Spring Onion", combine(BEEF_EXTRAS)),
            Map.entry("Thai Style Beef", combine(BEEF_EXTRAS)),            
            Map.entry("Beef in Black Pepper Sauce", combine(BEEF_EXTRAS)),
            Map.entry("Beef in Oyster Sauce", combine(BEEF_EXTRAS)),
            
            Map.entry("King Prawn in Peking Sauce", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn with Cashew Nuts", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn in Garlic Sauce", combine(PRAWN_EXTRAS)),
            Map.entry("Kung Po King Prawn", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn with Green Pepper in Black Bean Sauce", combine(PRAWN_EXTRAS)),
            Map.entry("Satay King Prawn", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn with Pineapple", combine(PRAWN_EXTRAS)),
            Map.entry("Szechuan Style King Prawn", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn with Bamboo Shoot & Water Chestnuts", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn with Mushroom", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn with Mixed Vegetables", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn Chop Suey", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn in Yellow Bean Sauce", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn in Oyster Sauce", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn with Ginger & Spring Onion", combine(PRAWN_EXTRAS)),
            Map.entry("King Prawn in Coconut Sauce", combine(PRAWN_EXTRAS)),
            Map.entry("Thai Style Seafood", combine(PRAWN_EXTRAS)),


            Map.entry("Chinese Style Roast Pork", combine(PORK_EXTRAS)),
            Map.entry("Roast Pork with Green Pepper in Black Bean Sauce", combine(PORK_EXTRAS)),
            Map.entry("Roast Pork in Yellow Bean Sauce", combine(PORK_EXTRAS)),
            Map.entry("Roast Pork with Mixed Vegetables", combine(PORK_EXTRAS)),
            Map.entry("Roast Pork Chop Suey", combine(PORK_EXTRAS)),
            Map.entry("Roast Pork with Ginger & Spring Onion", combine(PORK_EXTRAS)),
            Map.entry("Roast Pork in Coconut Sauce", combine(PORK_EXTRAS)),
            Map.entry("Kung Po Pork", combine(PORK_EXTRAS)),
            Map.entry("Crispy Pork", combine(PORK_EXTRAS)),


            Map.entry("Lamb with Ginger & Spring Onion", combine(LAMB_EXTRAS)),
            Map.entry("Lamb with Green Pepper in Black Bean Sauce", combine(LAMB_EXTRAS)),
            Map.entry("Lamb in Black Pepper Sauce", combine(LAMB_EXTRAS)),
            Map.entry("Lamb in Oyster Sauce", combine(LAMB_EXTRAS)),
            Map.entry("Szechuan Style Lamb", combine(LAMB_EXTRAS)),



            Map.entry("Special Curry", combine(CURRY_EXTRAS)),
            Map.entry("Chicken Curry", combine(CURRY_EXTRAS)),
            Map.entry("Beef Curry", combine(CURRY_EXTRAS)),
            Map.entry("Roast Pork Curry", combine(CURRY_EXTRAS)),
            Map.entry("Lamb Curry", combine(CURRY_EXTRAS)),
            Map.entry("King Prawn Curry", combine(CURRY_EXTRAS)),
            Map.entry("Mixed Vegetables Curry", combine(CURRY_EXTRAS)),
            Map.entry("Roast Duck Curry", combine(CURRY_EXTRAS)),

            Map.entry("Lamb Thai Red Curry", combine(CURRY_EXTRAS)),
            Map.entry("Lamb Thai Green Curry", combine(CURRY_EXTRAS)),
            Map.entry("King Prawn Thai Red Curry", combine(CURRY_EXTRAS)),
            Map.entry("King Prawn Thai Green Curry", combine(CURRY_EXTRAS)),
            Map.entry("Beef Thai Red Curry", combine(CURRY_EXTRAS)),
            Map.entry("Beef Thai Green Curry", combine(CURRY_EXTRAS)),
            Map.entry("Chicken Thai Red Curry", combine(CURRY_EXTRAS)),
            Map.entry("Chicken Thai Green Curry", combine(CURRY_EXTRAS)),
            Map.entry("Roast Pork Thai Red Curry", combine(CURRY_EXTRAS)),
            Map.entry("Roast Pork Thai Green Curry", combine(CURRY_EXTRAS)),
            Map.entry("Shrimp Thai Red Curry", combine(CURRY_EXTRAS)),
            Map.entry("Shrimp Thai Green Curry", combine(CURRY_EXTRAS)),
            Map.entry("Mushroom Thai Red Curry", combine(CURRY_EXTRAS)),
            Map.entry("Mushroom Thai Green Curry", combine(CURRY_EXTRAS)),
            Map.entry("Mixed Vegetables Thai Red Curry", combine(CURRY_EXTRAS)),
            Map.entry("Mixed Vegetables Thai Green Curry", combine(CURRY_EXTRAS)),
            

            Map.entry("Duck with Plum Sauce", combine(DUCK_EXTRAS)),
            Map.entry("Chinese Style Duck", combine(DUCK_EXTRAS)),
            Map.entry("Roast Duck with Pineapple", combine(DUCK_EXTRAS)),
            Map.entry("Roast Duck in Garlic Sauce", combine(DUCK_EXTRAS)),
            Map.entry("Roast Duck with Ginger & Spring Onion", combine(DUCK_EXTRAS)),
            Map.entry("Roast Duck with Green Pepper in Black Bean Sauce", combine(DUCK_EXTRAS)),

            Map.entry("Stir Fried Mixed Vegetables", combine(VEG_EXTRAS)),
            Map.entry("Fried Mushroom", combine(VEG_EXTRAS)),
            Map.entry("Mixed Vegetables in Black Bean Sauce", combine(VEG_EXTRAS)),
            Map.entry("Fried Bamboo Shoot & Water Chestnuts", combine(VEG_EXTRAS)),
            Map.entry("Fried Beansprouts", combine(VEG_EXTRAS)),
            Map.entry("Szechuan Style Mixed Vegetables", combine(VEG_EXTRAS)),
            Map.entry("Mixed Vegetables in Yellow Bean Sauce", combine(VEG_EXTRAS)),
            Map.entry("Mixed Vegetables with Cashew Nuts", combine(VEG_EXTRAS)),
            Map.entry("Broccoli with Garlic", combine(VEG_EXTRAS))

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
