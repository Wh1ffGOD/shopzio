package com.ecommerce.config;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedProducts();
    }

    private void seedAdminUser() {
        if (userRepository.existsByEmail("rohansinghofficial13@gmail.com")) return;

        User admin = User.builder()
                .firstName("Rohan")
                .lastName("Singh")
                .email("rohansinghofficial13@gmail.com")
                .password(passwordEncoder.encode("Letme@52"))
                .role(User.Role.ADMIN)
                .phone("0000000000")
                .build();

        userRepository.save(admin);
        log.info("Admin user seeded → email: rohansinghofficial13@gmail.com");
    }

    private void seedProducts() {
        if (productRepository.count() > 0) return;

        List<Product> products = List.of(

            // ── Electronics ──────────────────────────────────────────────
            Product.builder().name("iPhone 15 Pro").description("Apple iPhone 15 Pro with A17 Pro chip, titanium design, 48MP main camera with 5x optical zoom.")
                .price(new BigDecimal("134900")).stock(50).category("Electronics").rating(4.8).reviewCount(3240)
                .imageUrl("https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Samsung Galaxy S24 Ultra").description("Galaxy S24 Ultra with Snapdragon 8 Gen 3, 200MP camera, built-in S Pen, 6.8-inch display.")
                .price(new BigDecimal("124999")).stock(40).category("Electronics").rating(4.7).reviewCount(2100)
                .imageUrl("https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=500&auto=format&fit=crop").build(),

            Product.builder().name("MacBook Pro 14 M3").description("Apple MacBook Pro with M3 Pro chip, 14-inch Liquid Retina XDR display, 18GB RAM, 512GB SSD.")
                .price(new BigDecimal("199900")).stock(25).category("Electronics").rating(4.9).reviewCount(1800)
                .imageUrl("https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Sony WH-1000XM5 Headphones").description("Industry-leading noise cancelling wireless headphones, 30-hour battery, multi-point connection.")
                .price(new BigDecimal("29990")).stock(60).category("Electronics").rating(4.7).reviewCount(4500)
                .imageUrl("https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Apple iPad Pro 12.9").description("iPad Pro with M2 chip, 12.9-inch Liquid Retina XDR display, supports Apple Pencil and Magic Keyboard.")
                .price(new BigDecimal("109900")).stock(30).category("Electronics").rating(4.8).reviewCount(1560)
                .imageUrl("https://images.unsplash.com/photo-1544244015757-46f57f8a14cb?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Sony 55-inch 4K OLED TV").description("BRAVIA XR OLED TV with Cognitive Processor XR, Dolby Vision, HDMI 2.1, Google TV.")
                .price(new BigDecimal("149990")).stock(15).category("Electronics").rating(4.6).reviewCount(870)
                .imageUrl("https://images.unsplash.com/photo-1593359677879-a4bb92f829d1?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Apple AirPods Pro 2nd Gen").description("Active Noise Cancellation, Adaptive Transparency, Spatial Audio, MagSafe Charging Case.")
                .price(new BigDecimal("24900")).stock(80).category("Electronics").rating(4.6).reviewCount(5200)
                .imageUrl("https://images.unsplash.com/photo-1603351154351-5e2d0600bb77?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Canon EOS R50 Camera").description("24.2MP APS-C sensor mirrorless camera, 4K video, Eye AF, dual pixel CMOS AF II.")
                .price(new BigDecimal("64995")).stock(20).category("Electronics").rating(4.5).reviewCount(740)
                .imageUrl("https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=500&auto=format&fit=crop").build(),

            // ── Clothing ─────────────────────────────────────────────────
            Product.builder().name("Men's Classic White T-Shirt").description("100% organic cotton unisex t-shirt. Comfortable, breathable, pre-shrunk. Sizes XS–3XL.")
                .price(new BigDecimal("799")).stock(500).category("Clothing").rating(4.4).reviewCount(1300)
                .imageUrl("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Slim Fit Stretch Jeans").description("Premium denim with 2% elastane for comfort. Slim fit through thigh, tapered leg. Machine washable.")
                .price(new BigDecimal("2499")).stock(150).category("Clothing").rating(4.3).reviewCount(950)
                .imageUrl("https://images.unsplash.com/photo-1542272604-787c3835535d?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Men's Zip-Up Hoodie").description("Soft fleece hoodie with zip closure, kangaroo pocket, and ribbed cuffs. Perfect for layering.")
                .price(new BigDecimal("1999")).stock(120).category("Clothing").rating(4.5).reviewCount(820)
                .imageUrl("https://images.unsplash.com/photo-1556821840-3a63f15732ce?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Women's Floral Midi Dress").description("Lightweight floral print midi dress with V-neckline, flutter sleeves, and tie waist.")
                .price(new BigDecimal("1799")).stock(100).category("Clothing").rating(4.4).reviewCount(610)
                .imageUrl("https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Men's Leather Jacket").description("Genuine lambskin leather jacket with YKK zippers, multiple pockets, quilted lining.")
                .price(new BigDecimal("7999")).stock(40).category("Clothing").rating(4.6).reviewCount(380)
                .imageUrl("https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Nike Air Max 270").description("Iconic Nike Air Max cushioning, engineered mesh upper, large heel Air unit for all-day comfort.")
                .price(new BigDecimal("8995")).stock(80).category("Clothing").rating(4.5).reviewCount(2200)
                .imageUrl("https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500&auto=format&fit=crop").build(),

            // ── Home & Kitchen ────────────────────────────────────────────
            Product.builder().name("Nespresso Vertuo Pop").description("Compact Nespresso coffee machine with Centrifusion technology, 5 cup sizes, 37-second heat-up.")
                .price(new BigDecimal("8999")).stock(35).category("Home & Kitchen").rating(4.6).reviewCount(3100)
                .imageUrl("https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Instant Pot Duo 7-in-1").description("Pressure cooker, slow cooker, rice cooker, steamer, sauté, yogurt maker, warmer — 6 quart.")
                .price(new BigDecimal("6999")).stock(45).category("Home & Kitchen").rating(4.7).reviewCount(6800)
                .imageUrl("https://images.unsplash.com/photo-1585515320310-259814833e62?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Philips Air Fryer XXL").description("Air fry, bake, grill, roast, and reheat. 7.3L capacity, 1725W, fat removal technology.")
                .price(new BigDecimal("12999")).stock(30).category("Home & Kitchen").rating(4.5).reviewCount(2900)
                .imageUrl("https://images.unsplash.com/photo-1639667870348-1cc3e6f5ba51?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Vitamix 5200 Blender").description("Professional-grade blender, variable speed control, self-cleaning, hardened stainless steel blades.")
                .price(new BigDecimal("34999")).stock(20).category("Home & Kitchen").rating(4.8).reviewCount(1450)
                .imageUrl("https://images.unsplash.com/photo-1570197788417-0e82375c9371?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Dyson V15 Detect Vacuum").description("Laser reveals microscopic dust, HEPA filtration, 60-minute battery, LCD screen shows suction power.")
                .price(new BigDecimal("54900")).stock(18).category("Home & Kitchen").rating(4.7).reviewCount(1890)
                .imageUrl("https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=500&auto=format&fit=crop").build(),

            // ── Books ─────────────────────────────────────────────────────
            Product.builder().name("Clean Code by Robert C. Martin").description("A handbook of agile software craftsmanship. Essential reading for every professional developer.")
                .price(new BigDecimal("2999")).stock(100).category("Books").rating(4.8).reviewCount(8900)
                .imageUrl("https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Atomic Habits by James Clear").description("Proven framework for getting 1% better every day. Build good habits, break bad ones.")
                .price(new BigDecimal("499")).stock(200).category("Books").rating(4.9).reviewCount(15600)
                .imageUrl("https://images.unsplash.com/photo-1589158522613-c8f18a2bd5d0?w=500&auto=format&fit=crop").build(),

            Product.builder().name("The Pragmatic Programmer").description("20th anniversary edition. From journeyman to master — timeless advice for software developers.")
                .price(new BigDecimal("3499")).stock(75).category("Books").rating(4.7).reviewCount(4300)
                .imageUrl("https://images.unsplash.com/photo-1543002588-bfa74002ed7e?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Rich Dad Poor Dad").description("Robert Kiyosaki's #1 personal finance book. What the rich teach their kids about money.")
                .price(new BigDecimal("299")).stock(300).category("Books").rating(4.6).reviewCount(22000)
                .imageUrl("https://images.unsplash.com/photo-1592496431122-2349e0fbc666?w=500&auto=format&fit=crop").build(),

            // ── Sports ────────────────────────────────────────────────────
            Product.builder().name("Manduka PRO Yoga Mat").description("Non-slip 6mm thick premium yoga mat with alignment lines, closed-cell surface, includes carrying strap.")
                .price(new BigDecimal("3499")).stock(120).category("Sports").rating(4.6).reviewCount(2750)
                .imageUrl("https://images.unsplash.com/photo-1601925228008-40a44f3f2c95?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Adjustable Dumbbell Set 5-52 lbs").description("Replaces 15 sets of weights. Select weight by turning dial. Includes storage tray.")
                .price(new BigDecimal("24999")).stock(30).category("Sports").rating(4.7).reviewCount(3400)
                .imageUrl("https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Spalding NBA Official Basketball").description("Official NBA game ball, genuine Horween leather, size 7, hand-crafted for indoor play.")
                .price(new BigDecimal("4999")).stock(60).category("Sports").rating(4.5).reviewCount(1200)
                .imageUrl("https://images.unsplash.com/photo-1546519638-68e109498ffc?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Resistance Bands Set (5 Pack)").description("Heavy-duty latex resistance bands for strength training. Stackable up to 150 lbs with door anchor.")
                .price(new BigDecimal("999")).stock(200).category("Sports").rating(4.4).reviewCount(5600)
                .imageUrl("https://images.unsplash.com/photo-1598289431512-b97b0917afac?w=500&auto=format&fit=crop").build(),

            // ── Beauty & Personal Care ─────────────────────────────────────
            Product.builder().name("The Ordinary Niacinamide 10%").description("High-strength vitamin & mineral blemish formula. Reduces pore size and controls sebum. 30ml.")
                .price(new BigDecimal("590")).stock(300).category("Beauty").rating(4.5).reviewCount(12000)
                .imageUrl("https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500&auto=format&fit=crop").build(),

            Product.builder().name("MAC Matte Lipstick").description("Iconic MAC matte lipstick in Ruby Woo. Long-wearing formula with bold pigment payoff.")
                .price(new BigDecimal("1800")).stock(150).category("Beauty").rating(4.7).reviewCount(8200)
                .imageUrl("https://images.unsplash.com/photo-1512496015851-a90fb38ba796?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Chanel Chance Eau de Parfum 100ml").description("Floral, fresh fragrance with top notes of pink pepper, jasmine, and white musk. Long lasting.")
                .price(new BigDecimal("12000")).stock(40).category("Beauty").rating(4.8).reviewCount(3400)
                .imageUrl("https://images.unsplash.com/photo-1541643600914-78b084683702?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Dyson Supersonic Hair Dryer").description("Intelligent heat control, 3 speed settings, 4 heat settings, magnetic attachments, 2-year warranty.")
                .price(new BigDecimal("37900")).stock(25).category("Beauty").rating(4.6).reviewCount(2100)
                .imageUrl("https://images.unsplash.com/photo-1522338242992-e1a54906a8da?w=500&auto=format&fit=crop").build(),

            // ── Toys & Games ──────────────────────────────────────────────
            Product.builder().name("LEGO Technic Lamborghini Sián 42115").description("1:8 scale Lamborghini Sián FKP 37. 3,696 pieces, full opening doors, working gearbox.")
                .price(new BigDecimal("34999")).stock(20).category("Toys & Games").rating(4.9).reviewCount(1850)
                .imageUrl("https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Monopoly Classic Board Game").description("Classic property trading game for 2-8 players. Includes 8 tokens, 28 title deeds, play money.")
                .price(new BigDecimal("1299")).stock(100).category("Toys & Games").rating(4.5).reviewCount(6700)
                .imageUrl("https://images.unsplash.com/photo-1611996575749-79a3a250f948?w=500&auto=format&fit=crop").build(),

            Product.builder().name("PlayStation 5 DualSense Controller").description("Haptic feedback, adaptive triggers, built-in microphone, USB-C charging, 12-hour battery.")
                .price(new BigDecimal("5990")).stock(60).category("Toys & Games").rating(4.8).reviewCount(9400)
                .imageUrl("https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=500&auto=format&fit=crop").build(),

            // ── Health & Wellness ─────────────────────────────────────────
            Product.builder().name("Optimum Nutrition Gold Standard Whey Protein").description("24g protein per serving, 5.5g BCAAs, double rich chocolate, 2 lbs. No added sugar.")
                .price(new BigDecimal("2899")).stock(80).category("Health & Wellness").rating(4.7).reviewCount(14500)
                .imageUrl("https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Wellbeing Nutrition Multivitamin").description("Daily multivitamin with 45+ nutrients. Vitamin D3, B12, Zinc, Iron. 60 capsules, 2-month supply.")
                .price(new BigDecimal("699")).stock(200).category("Health & Wellness").rating(4.4).reviewCount(3200)
                .imageUrl("https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Fitbit Charge 6 Fitness Tracker").description("Built-in GPS, heart rate monitor, sleep tracking, 7-day battery, Google Maps & Wallet.")
                .price(new BigDecimal("14999")).stock(45).category("Health & Wellness").rating(4.4).reviewCount(4100)
                .imageUrl("https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Theragun Prime Massage Gun").description("4 attachments, 16mm amplitude, 5 speeds, whisper-quiet motor, 120-minute battery life.")
                .price(new BigDecimal("24999")).stock(22).category("Health & Wellness").rating(4.6).reviewCount(2800)
                .imageUrl("https://images.unsplash.com/photo-1591311629554-41e5e9b2e18b?w=500&auto=format&fit=crop").build(),

            // ── Grocery & Gourmet ─────────────────────────────────────────
            Product.builder().name("Tata Tea Premium 1kg").description("Rich & aromatic premium blend. Made from the finest Assam and Darjeeling tea leaves.")
                .price(new BigDecimal("399")).stock(500).category("Grocery").rating(4.5).reviewCount(8900)
                .imageUrl("https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Cadbury Celebrations Gift Box").description("Assorted Cadbury chocolate gift box with Dairy Milk, 5 Star, Gems & Perk. 200g.")
                .price(new BigDecimal("349")).stock(300).category("Grocery").rating(4.6).reviewCount(5600)
                .imageUrl("https://images.unsplash.com/photo-1587132137056-bfbf0166836e?w=500&auto=format&fit=crop").build(),

            Product.builder().name("Fortune Kachi Ghani Mustard Oil 5L").description("Cold pressed mustard oil with natural pungency. 100% pure, no added preservatives. 5 litre.")
                .price(new BigDecimal("799")).stock(150).category("Grocery").rating(4.3).reviewCount(2100)
                .imageUrl("https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=500&auto=format&fit=crop").build()
        );

        productRepository.saveAll(products);
        log.info("Seeded {} products across 9 categories", products.size());
    }
}
