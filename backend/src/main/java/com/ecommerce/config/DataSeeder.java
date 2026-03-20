package com.ecommerce.config;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private static final String U = "https://images.unsplash.com/photo-";
    private static final String Q = "?auto=format&fit=crop&w=600&q=80";

    @Override
    public void run(String... args) {
        seedAdminUser();
        seedProducts();
    }

    private void seedAdminUser() {
        if (userRepository.existsByEmail(adminEmail)) return;
        User admin = User.builder()
                .firstName("Admin").lastName("Shopzio")
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(User.Role.ADMIN).phone("0000000000").build();
        userRepository.save(admin);
        log.info("Admin seeded: {}", adminEmail);
    }

    private Product p(String name, String desc, String price, int stock, String cat,
                      double rating, int reviews, String... imgs) {
        List<String> imageList = new ArrayList<>(Arrays.asList(imgs));
        return Product.builder()
                .name(name).description(desc)
                .price(new BigDecimal(price))
                .stock(stock).category(cat)
                .rating(rating).reviewCount(reviews)
                .imageUrl(imgs[0]).images(imageList)
                .build();
    }

    private void seedProducts() {
        if (productRepository.count() >= 150) return;
        try { productRepository.deleteAllInBatch(); } catch (Exception e) { log.warn("Could not clear products: {}", e.getMessage()); }

        List<Product> products = new ArrayList<>();

        // ── Electronics ──────────────────────────────────────────────────────
        products.add(p("iPhone 15 Pro", "Apple iPhone 15 Pro with A17 Pro chip, titanium design, 48MP main camera with 5x optical zoom. Available in Natural Titanium.", "134900", 50, "Electronics", 4.8, 3240,
            U+"1695048133142-1a20484d2569"+Q, U+"1511707171634-5f897ff02aa9"+Q, U+"1574944985070-bcd4aae4b1b9"+Q));
        products.add(p("Samsung Galaxy S24 Ultra", "Galaxy S24 Ultra with Snapdragon 8 Gen 3, 200MP camera, built-in S Pen, 6.8-inch Dynamic AMOLED 2X display, 5000mAh battery.", "124999", 40, "Electronics", 4.7, 2100,
            U+"1610945415295-d9bbf067e59c"+Q, U+"1598327105666-5b89351aff97"+Q, U+"1580910051592-ebf46f9da0b2"+Q));
        products.add(p("MacBook Pro 14 M3", "Apple MacBook Pro with M3 Pro chip, 14-inch Liquid Retina XDR display, 18GB RAM, 512GB SSD, up to 18 hours battery life.", "199900", 25, "Electronics", 4.9, 1800,
            U+"1517336714731-489689fd1ca8"+Q, U+"1496181133206-80ce9b88a853"+Q, U+"1541807084-5c52b6b3adef"+Q));
        products.add(p("Sony WH-1000XM5 Headphones", "Industry-leading noise cancellation, 30-hour battery, multi-point connection, crystal clear hands-free calling with 8 microphones.", "29990", 60, "Electronics", 4.7, 4500,
            U+"1618366712010-f4ae9c647dcb"+Q, U+"1505740420928-5e560c06d30e"+Q, U+"1484704849700-f032a568e944"+Q));
        products.add(p("Apple iPad Pro 12.9", "iPad Pro with M2 chip, 12.9-inch Liquid Retina XDR display, ProMotion technology, supports Apple Pencil 2nd gen and Magic Keyboard.", "109900", 30, "Electronics", 4.8, 1560,
            U+"1544244015757-46f57f8a14cb"+Q, U+"1589739905210-3e5b27d0fb2f"+Q, U+"1623126908029-58cb08a2b272"+Q));
        products.add(p("Sony 55-inch 4K OLED TV", "BRAVIA XR OLED TV with Cognitive Processor XR, Dolby Vision, HDMI 2.1, Google TV smart platform, Acoustic Surface Audio+.", "149990", 15, "Electronics", 4.6, 870,
            U+"1593359677879-a4bb92f829d1"+Q, U+"1571506765797-1de5ee7c5b9c"+Q, U+"1548438294-1ad5d5f4f063"+Q));
        products.add(p("Apple AirPods Pro 2nd Gen", "Active Noise Cancellation, Adaptive Transparency, Spatial Audio with dynamic head tracking, MagSafe charging case, USB-C.", "24900", 80, "Electronics", 4.6, 5200,
            U+"1603351154351-5e2d0600bb77"+Q, U+"1572569511254-d8f925fe2cbb"+Q, U+"1585386959984-a4155224a1ad"+Q));
        products.add(p("Canon EOS R50 Camera", "24.2MP APS-C sensor mirrorless camera, 4K video recording, Eye AF with dual pixel CMOS AF II, compact and lightweight design.", "64995", 20, "Electronics", 4.5, 740,
            U+"1516035069371-29a1b244cc32"+Q, U+"1606606735330-4dcb6890b8d8"+Q, U+"1502982870125-f6a29a5d4c6d"+Q));
        products.add(p("Apple Watch Series 9", "Advanced health sensors with blood oxygen and ECG, Always-On Retina display, crash detection, carbon neutral, 18-hour battery.", "41900", 55, "Electronics", 4.8, 2300,
            U+"1523275335684-37898b6baf30"+Q, U+"1546868871-7041f2a55e12"+Q, U+"1617043786099-5da44d3aae2e"+Q));
        products.add(p("Dell XPS 15 Laptop", "Intel Core i7-13700H, 16GB RAM, 512GB SSD, NVIDIA RTX 4060, 15.6-inch OLED touch display, compact premium design.", "139990", 18, "Electronics", 4.7, 1100,
            U+"1593642632559-0c6d3fc62b89"+Q, U+"1588872657578-7efd1f1555ed"+Q, U+"1603302576837-37561b2e2302"+Q));
        products.add(p("PlayStation 5 Console", "Next-gen gaming with ultra-high speed SSD, ray tracing, 4K-TV gaming, haptic feedback DualSense controller, 3D Audio technology.", "54990", 12, "Electronics", 4.9, 8400,
            U+"1606144042614-b2417e99c4e3"+Q, U+"1592591452300-2adab0ea5eb3"+Q, U+"1617096200347-cb04ae810b1d"+Q));
        products.add(p("Amazon Echo Dot 5th Gen", "Compact smart speaker with Alexa, improved bass, temperature sensor, Motion Detection, works with smart home devices.", "4499", 120, "Electronics", 4.4, 12000,
            U+"1543512214-318c7553f230"+Q, U+"1558888401-3cc1de77652d"+Q, U+"1556438064-2d764616691a"+Q));
        products.add(p("Kindle Paperwhite 11th Gen", "6.8-inch display, adjustable warm light, glare-free screen, waterproof IPX8, 16GB storage, 10 weeks battery, USB-C charging.", "13999", 90, "Electronics", 4.7, 9800,
            U+"1512436991641-6745cdb1723f"+Q, U+"1544716278-ca5e3f4abd8c"+Q, U+"1481627834876-b7833e8f84c6"+Q));
        products.add(p("Samsung 32-inch 4K Monitor", "UHD 4K IPS panel, 144Hz refresh rate, AMD FreeSync Premium, 1ms response time, USB-C 65W charging, eye-care technology.", "34999", 28, "Electronics", 4.5, 660,
            U+"1527443224154-c4a573d5f5a6"+Q, U+"1547032175-7fc8c7bd15b3"+Q, U+"1527443060-ce8461a4f38d"+Q));
        products.add(p("GoPro HERO12 Black", "5.3K60 video, 27MP photos, HyperSmooth 6.0 stabilization, waterproof to 10m, front & rear LCD screens, Enduro battery.", "35500", 35, "Electronics", 4.6, 1450,
            U+"1607462109225-6b64ae2dd3cb"+Q, U+"1501281668745-f7f57925c3b4"+Q, U+"1548175898-b7e6c6eb9a48"+Q));
        products.add(p("JBL Charge 5 Portable Speaker", "Powerful JBL Pro Sound, 20 hours playtime, IP67 waterproof & dustproof, built-in power bank, PartyBoost multi-speaker pairing.", "14999", 70, "Electronics", 4.6, 3200,
            U+"1608043152269-423dbba4e7e1"+Q, U+"1608248543803-ba4f8c70ae0b"+Q, U+"1507003211169-0a1dd7228f2d"+Q));
        products.add(p("Bose QuietComfort 45", "Legendary Bose noise cancellation, 24-hour battery, multi-point connection, lightweight, foldable for travel, Aware mode.", "24900", 45, "Electronics", 4.6, 2800,
            U+"1505740420928-5e560c06d30e"+Q, U+"1484704849700-f032a568e944"+Q, U+"1524678606370-a47ad25cb82a"+Q));
        products.add(p("Logitech MX Master 3S", "Advanced ergonomic mouse, MagSpeed 8000 DPI scroll, 70-day battery, USB-C, works on any surface, multi-device support.", "7995", 85, "Electronics", 4.7, 4100,
            U+"1527864550417-7fd91fc51a46"+Q, U+"1585670083289-c1a2f8c08f17"+Q, U+"1601850494422-3cf05d43ca65"+Q));
        products.add(p("Nintendo Switch OLED", "7-inch OLED screen, enhanced audio, 64GB storage, wide adjustable stand, wired LAN port in dock, Joy-Con controllers included.", "29999", 22, "Electronics", 4.8, 6700,
            U+"1627856013091-fed6e4e30025"+Q, U+"1605901309584-818e25960a8f"+Q, U+"1593305841991-05e28df1b207"+Q));
        products.add(p("OnePlus 12 5G", "Snapdragon 8 Gen 3, Hasselblad camera system, 50MP triple camera, 100W SUPERVOOC charging, 5400mAh battery, 6.82-inch LTPO AMOLED.", "64999", 60, "Electronics", 4.7, 3400,
            U+"1574944985070-bcd4aae4b1b9"+Q, U+"1592899677977-9c10002396e2"+Q, U+"1598327105666-5b89351aff97"+Q));

        // ── Clothing ──────────────────────────────────────────────────────────
        products.add(p("Men's Classic White T-Shirt", "100% organic cotton unisex t-shirt. Comfortable, breathable, pre-shrunk. Ribbed crew neck and taped shoulder seams. Sizes XS–3XL.", "799", 500, "Clothing", 4.4, 1300,
            U+"1521572163474-6864f9cf17ab"+Q, U+"1583743814124-052374977b85"+Q, U+"1562157873-818f35b70f00"+Q));
        products.add(p("Slim Fit Stretch Jeans", "Premium denim with 2% elastane for comfort stretch. Slim fit through thigh, tapered leg. 5-pocket styling, machine washable.", "2499", 150, "Clothing", 4.3, 950,
            U+"1542272604-787c3835535d"+Q, U+"1542291026-7eec264c27ff"+Q, U+"1496217263-bd635269e26c"+Q));
        products.add(p("Men's Zip-Up Hoodie", "Soft fleece zip-up hoodie with kangaroo pocket and ribbed cuffs. Perfect for layering. Available in 8 colours.", "1999", 120, "Clothing", 4.5, 820,
            U+"1556821840-3a63f15732ce"+Q, U+"1578768060-8a5f0f9a9a0a"+Q, U+"1565084819059-abb556d1a2f7"+Q));
        products.add(p("Women's Floral Midi Dress", "Lightweight floral print midi dress with V-neckline, flutter sleeves, and adjustable tie waist. Lined skirt, side pockets.", "1799", 100, "Clothing", 4.4, 610,
            U+"1595777457583-95e059d581b8"+Q, U+"1515886933296-503851674ba0"+Q, U+"1576436784-b4ba9e2e47b0"+Q));
        products.add(p("Men's Genuine Leather Jacket", "Premium lambskin leather jacket with YKK zippers, multiple interior pockets, quilted satin lining. Slim European fit.", "7999", 40, "Clothing", 4.6, 380,
            U+"1551028719-00167b16eac5"+Q, U+"1520975661082-2f408455fd7c"+Q, U+"1611312449408-fceccf891b2e"+Q));
        products.add(p("Nike Air Max 270", "Iconic Nike Air Max cushioning with engineered mesh upper, large heel Air unit for max comfort all day. Rubber outsole for traction.", "8995", 80, "Clothing", 4.5, 2200,
            U+"1542291026-7eec264c27ff"+Q, U+"1556905055-8f358a7a47b2"+Q, U+"1560769629-975ec94e6a86"+Q));
        products.add(p("Women's High-Waist Yoga Pants", "4-way stretch fabric, squat-proof, moisture-wicking. Hidden waistband pocket, wide waistband for comfort. 7/8 length.", "1499", 200, "Clothing", 4.6, 3400,
            U+"1506629082-ea8b3c3c5b0e"+Q, U+"1571731206-e6c5de5faf0e"+Q, U+"1518310383802-640c2de311ef"+Q));
        products.add(p("Men's Slim Fit Formal Shirt", "100% cotton poplin formal shirt, non-iron finish, slim fit cut, button-down collar. Available in white, blue, and charcoal.", "1299", 180, "Clothing", 4.3, 720,
            U+"1596755094514-31ef4d6fee65"+Q, U+"1507003211169-0a1dd7228f2d"+Q, U+"1618354691373-d851c5c827a4"+Q));
        products.add(p("Women's Puffer Bomber Jacket", "Lightweight water-resistant puffer bomber. Removable hood, ribbed cuffs and hem, two-way zip. Packable into its own pocket.", "3499", 65, "Clothing", 4.5, 490,
            U+"1548624149-f5a8c03e4ac5"+Q, U+"1545291730-d1a1ffcd4e4a"+Q, U+"1483985988355-763728e1935b"+Q));
        products.add(p("Men's Running Shorts", "Lightweight quick-dry performance shorts with 5-inch inseam, built-in liner, reflective details, key pocket.", "799", 250, "Clothing", 4.4, 1100,
            U+"1554284126-aa88f22d8b74"+Q, U+"1571731206-e6c5de5faf0e"+Q, U+"1490645776040-e61ca6a451db"+Q));
        products.add(p("Women's Embroidered Kurti", "Ethnic cotton kurti with hand embroidery, mandarin collar, three-quarter sleeves. Perfect for casual and festive wear.", "1299", 150, "Clothing", 4.5, 2100,
            U+"1583391099995-7e8e5c4ba2c7"+Q, U+"1610030469983-d429e7f35462"+Q, U+"1595341888175-f5f2f7f7c6d0"+Q));
        products.add(p("Men's Premium Polo T-Shirt", "Egyptian cotton pique polo shirt with two-button placket and rib-knit collar. Subtle embroidered logo. Machine washable.", "999", 300, "Clothing", 4.4, 1500,
            U+"1562157873-818f35b70f00"+Q, U+"1521572163474-6864f9cf17ab"+Q, U+"1583743814124-052374977b85"+Q));
        products.add(p("Women's High-Rise Denim Shorts", "Distressed high-rise denim shorts with frayed hem, slim fit through hip and thigh. 98% cotton, 2% elastane.", "1199", 120, "Clothing", 4.3, 860,
            U+"1565084819059-abb556d1a2f7"+Q, U+"1496217263-bd635269e26c"+Q, U+"1542272604-787c3835535d"+Q));
        products.add(p("Men's Slim Cargo Pants", "Stretch cargo pants with multiple utility pockets, tapered leg, YKK zippers. Ripstop fabric, wrinkle resistant.", "1799", 90, "Clothing", 4.4, 670,
            U+"1594938298564-e3eb63e8a0e4"+Q, U+"1507003211169-0a1dd7228f2d"+Q, U+"1596755094514-31ef4d6fee65"+Q));
        products.add(p("Women's Cable Knit Sweater", "Chunky cable knit pullover sweater, crew neck, ribbed cuffs and hem. Soft acrylic blend. Available in 6 colours.", "1599", 110, "Clothing", 4.5, 940,
            U+"1576091160550-2173dba999ef"+Q, U+"1548624149-f5a8c03e4ac5"+Q, U+"1483985988355-763728e1935b"+Q));
        products.add(p("Men's Slim Chino Pants", "Wrinkle-resistant stretch chino trousers, slim fit, flat front. 98% cotton 2% elastane. Great for work or casual.", "1999", 130, "Clothing", 4.3, 880,
            U+"1594938298564-e3eb63e8a0e4"+Q, U+"1507003211169-0a1dd7228f2d"+Q, U+"1562157873-818f35b70f00"+Q));
        products.add(p("Women's Seamless Sports Bra", "Medium support seamless sports bra, moisture-wicking, 4-way stretch, removable cups. Great for yoga and gym.", "699", 300, "Clothing", 4.6, 2200,
            U+"1518310383802-640c2de311ef"+Q, U+"1506629082-ea8b3c3c5b0e"+Q, U+"1571731206-e6c5de5faf0e"+Q));
        products.add(p("Men's Slim Fit Blazer", "Single-breasted slim blazer, notch lapel, two-button front, flap pockets. Dry clean only. Available in navy and charcoal.", "4999", 50, "Clothing", 4.5, 430,
            U+"1507003211169-0a1dd7228f2d"+Q, U+"1618354691373-d851c5c827a4"+Q, U+"1596755094514-31ef4d6fee65"+Q));
        products.add(p("Women's Ankle Boots", "Genuine leather ankle boots with block heel, side zip, cushioned insole. Versatile style for work and weekend.", "2999", 70, "Clothing", 4.4, 760,
            U+"1560769629-975ec94e6a86"+Q, U+"1556905055-8f358a7a47b2"+Q, U+"1542291026-7eec264c27ff"+Q));
        products.add(p("Men's Packable Puffer Jacket", "Lightweight water-resistant puffer with 90% down fill. Folds into self-pocket, hood with adjustable drawstring.", "3999", 75, "Clothing", 4.6, 1200,
            U+"1545291730-d1a1ffcd4e4a"+Q, U+"1548624149-f5a8c03e4ac5"+Q, U+"1611312449408-fceccf891b2e"+Q));

        // ── Home & Kitchen ────────────────────────────────────────────────────
        products.add(p("Nespresso Vertuo Pop Coffee Machine", "Compact Nespresso machine with Centrifusion technology, brews 5 cup sizes, 37-second heat-up, auto power-off.", "8999", 35, "Home & Kitchen", 4.6, 3100,
            U+"1495474472287-4d71bcdd2085"+Q, U+"1509042236881-f0f98c2f3ce6"+Q, U+"1442975631134-b28b4c93e586"+Q));
        products.add(p("Instant Pot Duo 7-in-1 Pressure Cooker", "Pressure cooker, slow cooker, rice cooker, steamer, sauté pan, food warmer and yogurt maker — all in one 6-quart pot.", "6999", 45, "Home & Kitchen", 4.7, 6800,
            U+"1585515320310-259814833e62"+Q, U+"1556909114-f6e7ad7d3136"+Q, U+"1556909262-485e9d3e97a3"+Q));
        products.add(p("Philips Air Fryer XXL 7.3L", "Air fry, bake, grill, roast, and reheat with 95% less fat. 7.3L family-size capacity, 1725W, unique fat removal technology.", "12999", 30, "Home & Kitchen", 4.5, 2900,
            U+"1639667870348-1cc3e6f5ba51"+Q, U+"1556909114-f6e7ad7d3136"+Q, U+"1585515320310-259814833e62"+Q));
        products.add(p("Vitamix 5200 Professional Blender", "Variable speed control, self-cleaning in 30-60 seconds, hardened stainless steel blades, 64 oz low-profile container.", "34999", 20, "Home & Kitchen", 4.8, 1450,
            U+"1570197788417-0e82375c9371"+Q, U+"1567620905-5f3be23e7a9a"+Q, U+"1608248543803-ba4f8c70ae0b"+Q));
        products.add(p("Dyson V15 Detect Cordless Vacuum", "Laser reveals invisible dust, HEPA filtration, 60-minute battery, LCD screen shows run-time and filter maintenance.", "54900", 18, "Home & Kitchen", 4.7, 1890,
            U+"1558618666-fcd25c85cd64"+Q, U+"1527515637-462e94beb0e5"+Q, U+"1585771273612-3bab13a4b08c"+Q));
        products.add(p("KitchenAid Artisan Stand Mixer", "5-quart stainless steel bowl, 10-speed, 59-point planetary mixing action, tilt-head design, includes 3 attachments.", "44999", 15, "Home & Kitchen", 4.9, 2200,
            U+"1556909114-f6e7ad7d3136"+Q, U+"1509042236881-f0f98c2f3ce6"+Q, U+"1495474472287-4d71bcdd2085"+Q));
        products.add(p("Prestige Induction Cooktop 2000W", "Feather-touch control, auto-voltage regulator, preset Indian menu, child-lock, overheating protection, 2000W power.", "3499", 80, "Home & Kitchen", 4.4, 4500,
            U+"1556909262-485e9d3e97a3"+Q, U+"1585515320310-259814833e62"+Q, U+"1639667870348-1cc3e6f5ba51"+Q));
        products.add(p("Milton Stainless Steel Lunch Box 3-Tier", "100% food-grade stainless steel, leak-proof, air-tight, dishwasher safe, 3 containers in insulated carry bag.", "899", 200, "Home & Kitchen", 4.5, 6700,
            U+"1567620905-5f3be23e7a9a"+Q, U+"1567620905-5f3be23e7a9a"+Q, U+"1556909114-f6e7ad7d3136"+Q));
        products.add(p("Solimo Non-Stick Cookware Set 5-Piece", "Forged aluminium with PFOA-free non-stick coating, riveted stainless handles, induction compatible, dishwasher safe.", "1999", 60, "Home & Kitchen", 4.3, 2800,
            U+"1556909262-485e9d3e97a3"+Q, U+"1585515320310-259814833e62"+Q, U+"1495474472287-4d71bcdd2085"+Q));
        products.add(p("Bombay Dyeing Cotton Bedsheet King", "280 thread count 100% pure cotton, reactive dyes, includes 1 bedsheet + 2 pillow covers. Machine washable.", "1499", 100, "Home & Kitchen", 4.4, 3200,
            U+"1631049307264-da0ea5d16b6c"+Q, U+"1555041469-3ce3b0d18bd9"+Q, U+"1493663160519-5d8888f97a06"+Q));
        products.add(p("Cello Opalware Dinner Set 27-Piece", "Lightweight, chip-resistant opalware, microwave and dishwasher safe, includes plates, bowls and mugs.", "1299", 55, "Home & Kitchen", 4.3, 1800,
            U+"1567620905-5f3be23e7a9a"+Q, U+"1556909262-485e9d3e97a3"+Q, U+"1514190370-ed5035d9c3d9"+Q));
        products.add(p("Havells 2200W Dry Iron", "Non-stick soleplate, pilot indicator light, 360° cord rotation, thermal cut-off safety, 2200W for fast ironing.", "1299", 90, "Home & Kitchen", 4.3, 5100,
            U+"1527515637-462e94beb0e5"+Q, U+"1558618666-fcd25c85cd64"+Q, U+"1585771273612-3bab13a4b08c"+Q));
        products.add(p("Symphony Hicool 17L Air Cooler", "Unique i-Pure technology removes PM 2.5 particles, 17L tank, 3-speed control, auto-fill, cool flow dispenser.", "7999", 25, "Home & Kitchen", 4.4, 2400,
            U+"1585771273612-3bab13a4b08c"+Q, U+"1527515637-462e94beb0e5"+Q, U+"1558618666-fcd25c85cd64"+Q));
        products.add(p("Pigeon Aluminium Pressure Cooker 5L", "ISI marked hard anodised aluminium pressure cooker, extra thick base, gasket release system safety, auto locking.", "1499", 120, "Home & Kitchen", 4.5, 7800,
            U+"1556909262-485e9d3e97a3"+Q, U+"1556909114-f6e7ad7d3136"+Q, U+"1585515320310-259814833e62"+Q));
        products.add(p("Kent Grand Plus RO Water Purifier", "8L storage, multiple purification stages, UV + UF + RO + TDS controller, mineral ROTM technology, auto-flush.", "14999", 20, "Home & Kitchen", 4.5, 3600,
            U+"1585771273612-3bab13a4b08c"+Q, U+"1527515637-462e94beb0e5"+Q, U+"1558618666-fcd25c85cd64"+Q));
        products.add(p("Borosil Glass Storage Set 6-Piece", "Borosilicate glass containers, air-tight snap-lock lids, microwave and dishwasher safe, lead-free, stackable.", "1299", 70, "Home & Kitchen", 4.6, 2900,
            U+"1514190370-ed5035d9c3d9"+Q, U+"1567620905-5f3be23e7a9a"+Q, U+"1556909262-485e9d3e97a3"+Q));
        products.add(p("Whirlpool 20L Solo Microwave", "20L capacity, 700W power, 5 power levels, 8 auto-cook menus, easy-clean interior, child safety lock.", "7999", 30, "Home & Kitchen", 4.3, 3100,
            U+"1556909114-f6e7ad7d3136"+Q, U+"1585515320310-259814833e62"+Q, U+"1495474472287-4d71bcdd2085"+Q));
        products.add(p("Godrej Edge Pro 190L Refrigerator", "Single door, frost-free, 190L capacity, intelligent inverter compressor, toughened glass shelves, large vegetable crisper.", "16990", 12, "Home & Kitchen", 4.4, 1700,
            U+"1527515637-462e94beb0e5"+Q, U+"1558618666-fcd25c85cd64"+Q, U+"1585771273612-3bab13a4b08c"+Q));
        products.add(p("Bajaj New GX-1 1000W Mixer Grinder", "3 stainless steel jars, 3-speed with pulse, overload protector, anti-skid rubber feet, 2-year warranty.", "2499", 85, "Home & Kitchen", 4.4, 5200,
            U+"1570197788417-0e82375c9371"+Q, U+"1495474472287-4d71bcdd2085"+Q, U+"1509042236881-f0f98c2f3ce6"+Q));
        products.add(p("AmazonBasics 2-Slice Toaster", "Extra-wide slots for thick bread, 6 browning settings, defrost/cancel/reheat, removable crumb tray, 800W.", "1999", 100, "Home & Kitchen", 4.3, 2800,
            U+"1509042236881-f0f98c2f3ce6"+Q, U+"1495474472287-4d71bcdd2085"+Q, U+"1442975631134-b28b4c93e586"+Q));

        // ── Books ─────────────────────────────────────────────────────────────
        products.add(p("Clean Code by Robert C. Martin", "A handbook of agile software craftsmanship covering naming, functions, formatting, objects, unit tests, and systems design.", "2999", 100, "Books", 4.8, 8900,
            U+"1544716278-ca5e3f4abd8c"+Q, U+"1512436991641-6745cdb1723f"+Q, U+"1481627834876-b7833e8f84c6"+Q));
        products.add(p("Atomic Habits by James Clear", "An easy and proven way to build good habits and break bad ones. Four laws of behaviour change framework.", "499", 200, "Books", 4.9, 15600,
            U+"1589158522613-c8f18a2bd5d0"+Q, U+"1543002588-bfa74002ed7e"+Q, U+"1527799262852-d1b585a47291"+Q));
        products.add(p("The Pragmatic Programmer 20th Anniversary", "From journeyman to master — updated edition with new tips, new challenges, and thorough coverage of modern development.", "3499", 75, "Books", 4.7, 4300,
            U+"1543002588-bfa74002ed7e"+Q, U+"1544716278-ca5e3f4abd8c"+Q, U+"1512436991641-6745cdb1723f"+Q));
        products.add(p("Rich Dad Poor Dad by Robert Kiyosaki", "What the rich teach their kids about money that the poor and middle class do not. #1 personal finance book.", "299", 300, "Books", 4.6, 22000,
            U+"1592496431122-2349e0fbc666"+Q, U+"1527799262852-d1b585a47291"+Q, U+"1481627834876-b7833e8f84c6"+Q));
        products.add(p("The Psychology of Money by Morgan Housel", "Timeless lessons on wealth, greed, and happiness. 19 short stories about the strange ways people think about money.", "599", 180, "Books", 4.8, 11200,
            U+"1527799262852-d1b585a47291"+Q, U+"1543002588-bfa74002ed7e"+Q, U+"1592496431122-2349e0fbc666"+Q));
        products.add(p("Deep Work by Cal Newport", "Rules for focused success in a distracted world. Strategies to develop deep focus and produce your best work.", "799", 140, "Books", 4.7, 6800,
            U+"1481627834876-b7833e8f84c6"+Q, U+"1544716278-ca5e3f4abd8c"+Q, U+"1512436991641-6745cdb1723f"+Q));
        products.add(p("Zero to One by Peter Thiel", "Notes on startups, or how to build the future. Essential reading for entrepreneurs and innovators.", "699", 160, "Books", 4.6, 7400,
            U+"1543002588-bfa74002ed7e"+Q, U+"1527799262852-d1b585a47291"+Q, U+"1592496431122-2349e0fbc666"+Q));
        products.add(p("Sapiens: A Brief History of Humankind", "Yuval Noah Harari's landmark exploration of 70,000 years of human history, society, and culture.", "799", 200, "Books", 4.7, 18000,
            U+"1512436991641-6745cdb1723f"+Q, U+"1481627834876-b7833e8f84c6"+Q, U+"1544716278-ca5e3f4abd8c"+Q));
        products.add(p("The Alchemist by Paulo Coelho", "A magical fable about following your dreams. One of the best-selling books in history, translated into 80+ languages.", "299", 400, "Books", 4.7, 31000,
            U+"1589158522613-c8f18a2bd5d0"+Q, U+"1543002588-bfa74002ed7e"+Q, U+"1527799262852-d1b585a47291"+Q));
        products.add(p("Think and Grow Rich by Napoleon Hill", "The landmark bestseller on success and personal development. Based on interviews with 500 of the most successful people.", "399", 250, "Books", 4.6, 14500,
            U+"1592496431122-2349e0fbc666"+Q, U+"1481627834876-b7833e8f84c6"+Q, U+"1544716278-ca5e3f4abd8c"+Q));
        products.add(p("The Lean Startup by Eric Ries", "How constant innovation creates radically successful businesses. Build-Measure-Learn feedback loop for startups.", "799", 120, "Books", 4.6, 5600,
            U+"1527799262852-d1b585a47291"+Q, U+"1543002588-bfa74002ed7e"+Q, U+"1512436991641-6745cdb1723f"+Q));
        products.add(p("Ikigai: The Japanese Secret to Long Life", "The Japanese concept of finding purpose and meaning in life. Simple and inspiring guide to finding your reason for being.", "399", 220, "Books", 4.5, 9800,
            U+"1481627834876-b7833e8f84c6"+Q, U+"1589158522613-c8f18a2bd5d0"+Q, U+"1592496431122-2349e0fbc666"+Q));
        products.add(p("1984 by George Orwell", "A dystopian masterpiece set in a totalitarian society under constant surveillance. One of the most influential novels ever written.", "299", 300, "Books", 4.8, 25000,
            U+"1544716278-ca5e3f4abd8c"+Q, U+"1512436991641-6745cdb1723f"+Q, U+"1527799262852-d1b585a47291"+Q));
        products.add(p("Harry Potter Complete 7-Book Set", "All seven Harry Potter books by J.K. Rowling in a gorgeous hardcover box set. Perfect for new and existing fans.", "2999", 50, "Books", 4.9, 42000,
            U+"1589158522613-c8f18a2bd5d0"+Q, U+"1481627834876-b7833e8f84c6"+Q, U+"1543002588-bfa74002ed7e"+Q));
        products.add(p("The Power of Now by Eckhart Tolle", "A guide to spiritual enlightenment. Learn how to free yourself from the pain of the past and anxiety of the future.", "499", 160, "Books", 4.6, 8700,
            U+"1592496431122-2349e0fbc666"+Q, U+"1527799262852-d1b585a47291"+Q, U+"1481627834876-b7833e8f84c6"+Q));
        products.add(p("Educated: A Memoir by Tara Westover", "A remarkable memoir about a woman who grows up in a survivalist family, and her path to education and self-discovery.", "699", 90, "Books", 4.7, 12000,
            U+"1481627834876-b7833e8f84c6"+Q, U+"1543002588-bfa74002ed7e"+Q, U+"1512436991641-6745cdb1723f"+Q));
        products.add(p("Dune by Frank Herbert", "The greatest science fiction novel ever written. A sweeping epic of ecology, politics, and messianic religion.", "599", 130, "Books", 4.8, 16000,
            U+"1543002588-bfa74002ed7e"+Q, U+"1589158522613-c8f18a2bd5d0"+Q, U+"1544716278-ca5e3f4abd8c"+Q));
        products.add(p("Good to Great by Jim Collins", "Why some companies make the leap from good to great and others don't. A landmark management study of 1,435 companies.", "1099", 85, "Books", 4.6, 6200,
            U+"1527799262852-d1b585a47291"+Q, U+"1592496431122-2349e0fbc666"+Q, U+"1481627834876-b7833e8f84c6"+Q));
        products.add(p("The Great Gatsby by F. Scott Fitzgerald", "A classic novel set in the Jazz Age, exploring themes of decadence, idealism, social upheaval, and excess.", "199", 350, "Books", 4.5, 19000,
            U+"1481627834876-b7833e8f84c6"+Q, U+"1512436991641-6745cdb1723f"+Q, U+"1544716278-ca5e3f4abd8c"+Q));
        products.add(p("The 4-Hour Work Week by Tim Ferriss", "Escape the 9-5, live anywhere, and join the new rich. A guide to designing your ideal lifestyle through automation.", "899", 110, "Books", 4.4, 7800,
            U+"1592496431122-2349e0fbc666"+Q, U+"1543002588-bfa74002ed7e"+Q, U+"1527799262852-d1b585a47291"+Q));

        // ── Sports ────────────────────────────────────────────────────────────
        products.add(p("Manduka PRO Yoga Mat 6mm", "Premium yoga mat for professional practice. Closed-cell surface prevents moisture and bacteria, lifetime guarantee.", "3499", 120, "Sports", 4.6, 2750,
            U+"1601925228008-40a44f3f2c95"+Q, U+"1540497077202-7c8a3999166f"+Q, U+"1571019614242-c5c5dee9f50b"+Q));
        products.add(p("Adjustable Dumbbell Set 5-52 lbs", "Select weight in 2.5 lb increments by turning the dial. Replaces 15 sets of weights, compact storage tray.", "24999", 30, "Sports", 4.7, 3400,
            U+"1534438327276-14e5300c3a48"+Q, U+"1533681904393-9ab6eee7bdb0"+Q, U+"1574680096145-d05b474e2155"+Q));
        products.add(p("Spalding NBA Authentic Basketball", "Official NBA game ball, genuine Horween leather, size 7, hand-crafted, deep channel design for superior grip.", "4999", 60, "Sports", 4.5, 1200,
            U+"1546519638-68e109498ffc"+Q, U+"1574680096145-d05b474e2155"+Q, U+"1533681904393-9ab6eee7bdb0"+Q));
        products.add(p("Resistance Bands Set 5-Pack", "Heavy-duty natural latex resistance bands, stackable up to 150 lbs, includes door anchor, ankle straps, handles.", "999", 200, "Sports", 4.4, 5600,
            U+"1598289431512-b97b0917afac"+Q, U+"1540497077202-7c8a3999166f"+Q, U+"1571019614242-c5c5dee9f50b"+Q));
        products.add(p("Yonex Mavis 350 Shuttlecock (6 Pack)", "Nylon feather shuttlecock for intermediate players. Consistent flight, durable construction, speed 77.", "699", 300, "Sports", 4.5, 4200,
            U+"1571019614242-c5c5dee9f50b"+Q, U+"1533681904393-9ab6eee7bdb0"+Q, U+"1574680096145-d05b474e2155"+Q));
        products.add(p("Decathlon Kipsta Size 5 Football", "FIFA-inspected football, thermally bonded panels, reinforced butyl bladder, good for amateur and club matches.", "1299", 150, "Sports", 4.4, 3100,
            U+"1574680096145-d05b474e2155"+Q, U+"1546519638-68e109498ffc"+Q, U+"1598289431512-b97b0917afac"+Q));
        products.add(p("Nivia Dominator Cricket Bat", "English willow grade 4, full size (size 6), well-shaped toe, machine pressed, suitable for turf and practice.", "1999", 80, "Sports", 4.3, 1800,
            U+"1548438294-1ad5d5f4f063"+Q, U+"1533681904393-9ab6eee7bdb0"+Q, U+"1574680096145-d05b474e2155"+Q));
        products.add(p("Cosco Tennis Racket Hurricane", "Graphite-aluminium frame, 16×19 string pattern, shock absorber, PVC grip. Weight 275g, good for beginner to intermediate.", "2499", 60, "Sports", 4.4, 980,
            U+"1571019614242-c5c5dee9f50b"+Q, U+"1540497077202-7c8a3999166f"+Q, U+"1598289431512-b97b0917afac"+Q));
        products.add(p("Boldfit Gym Bag 45L", "Water-resistant polyester, separate shoe compartment, wet pocket, padded shoulder strap, multiple pockets, laptop sleeve.", "1499", 110, "Sports", 4.4, 2300,
            U+"1533681904393-9ab6eee7bdb0"+Q, U+"1534438327276-14e5300c3a48"+Q, U+"1574680096145-d05b474e2155"+Q));
        products.add(p("Cosco Jump Rope Pro", "Adjustable PVC jump rope, ergonomic foam handles, anti-skid bearings, suitable for boxing, CrossFit, cardio.", "499", 400, "Sports", 4.3, 6800,
            U+"1540497077202-7c8a3999166f"+Q, U+"1598289431512-b97b0917afac"+Q, U+"1571019614242-c5c5dee9f50b"+Q));
        products.add(p("Adidas Duramo SL Running Shoes", "Lightweight mesh upper, cushioned midsole, Duramo outsole with waffle pattern. Great for daily running and gym.", "6999", 90, "Sports", 4.5, 4500,
            U+"1542291026-7eec264c27ff"+Q, U+"1556905055-8f358a7a47b2"+Q, U+"1560769629-975ec94e6a86"+Q));
        products.add(p("Pull-Up & Dip Bar Door Frame", "Heavy-duty steel, no screws needed, fits door frames 70-100cm wide, supports 150kg. Includes foam grips.", "2999", 50, "Sports", 4.5, 1900,
            U+"1534438327276-14e5300c3a48"+Q, U+"1574680096145-d05b474e2155"+Q, U+"1533681904393-9ab6eee7bdb0"+Q));
        products.add(p("Nivia Storm Boxing Gloves 10oz", "Synthetic leather construction, PVC padding, hook-and-loop wrist support, pre-curved ergonomic design.", "1899", 70, "Sports", 4.4, 1400,
            U+"1574680096145-d05b474e2155"+Q, U+"1540497077202-7c8a3999166f"+Q, U+"1548438294-1ad5d5f4f063"+Q));
        products.add(p("Strauss EVA Foam Roller 45cm", "High-density EVA foam, textured surface for deep tissue massage, hollow core, supports up to 150kg.", "999", 130, "Sports", 4.4, 3200,
            U+"1598289431512-b97b0917afac"+Q, U+"1601925228008-40a44f3f2c95"+Q, U+"1571019614242-c5c5dee9f50b"+Q));
        products.add(p("Li-Ning Aeronaut 7000B Badminton Racket", "High-modulus graphite frame, torsional stiffness box frame, attack type, weight 3U, string tension 28lbs max.", "3999", 40, "Sports", 4.6, 1600,
            U+"1571019614242-c5c5dee9f50b"+Q, U+"1533681904393-9ab6eee7bdb0"+Q, U+"1574680096145-d05b474e2155"+Q));
        products.add(p("Quechua Hiking Backpack 40L", "Raincover included, hip belt with pockets, sternum strap, back ventilation system, fits up to 1.8m back length.", "3499", 35, "Sports", 4.6, 2100,
            U+"1533681904393-9ab6eee7bdb0"+Q, U+"1534438327276-14e5300c3a48"+Q, U+"1540497077202-7c8a3999166f"+Q));
        products.add(p("Speedo Futura Biofuse Swimming Goggles", "Anti-fog UV protection lenses, soft silicone seal, quick-adjust buckle, 4 interchangeable nose bridges.", "899", 150, "Sports", 4.5, 2800,
            U+"1540497077202-7c8a3999166f"+Q, U+"1571019614242-c5c5dee9f50b"+Q, U+"1598289431512-b97b0917afac"+Q));
        products.add(p("Cosco Table Tennis Bat Set", "2 bats + 3 balls, 5-ply blade, pimples-in rubber, all-round player type, includes storage case.", "1299", 90, "Sports", 4.3, 2400,
            U+"1574680096145-d05b474e2155"+Q, U+"1546519638-68e109498ffc"+Q, U+"1548438294-1ad5d5f4f063"+Q));
        products.add(p("Whey Protein Shaker Bottle 700ml", "BPA-free Tritan shaker, stainless steel BlenderBall wire whisk, leak-proof flip cap, measurement markings.", "499", 500, "Sports", 4.4, 8900,
            U+"1598289431512-b97b0917afac"+Q, U+"1540497077202-7c8a3999166f"+Q, U+"1601925228008-40a44f3f2c95"+Q));
        products.add(p("Nivia Storm Volleyball", "Synthetic leather cover, natural rubber bladder, machine stitched, standard size and weight, for indoor and outdoor.", "899", 100, "Sports", 4.3, 1200,
            U+"1546519638-68e109498ffc"+Q, U+"1574680096145-d05b474e2155"+Q, U+"1533681904393-9ab6eee7bdb0"+Q));

        // ── Beauty ────────────────────────────────────────────────────────────
        products.add(p("The Ordinary Niacinamide 10% + Zinc 1%", "High-strength vitamin and mineral blemish formula. Reduces appearance of pores and controls sebum. 30ml.", "590", 300, "Beauty", 4.5, 12000,
            U+"1556228720-195a672e8a03"+Q, U+"1512496015851-a90fb38ba796"+Q, U+"1558769132-cb1aea458c5e"+Q));
        products.add(p("MAC Matte Lipstick Ruby Woo", "Iconic MAC matte lipstick in retro matte formula. Bold, vivid pigment with long-lasting colour payoff. 3g.", "1800", 150, "Beauty", 4.7, 8200,
            U+"1512496015851-a90fb38ba796"+Q, U+"1522338242992-e1a54906a8da"+Q, U+"1556228720-195a672e8a03"+Q));
        products.add(p("Chanel Chance Eau de Parfum 100ml", "Floral, fresh and surprising fragrance. Top notes of pink pepper, jasmine, patchouli, and white musk. Long lasting.", "12000", 40, "Beauty", 4.8, 3400,
            U+"1541643600914-78b084683702"+Q, U+"1558769132-cb1aea458c5e"+Q, U+"1512496015851-a90fb38ba796"+Q));
        products.add(p("Dyson Supersonic Hair Dryer", "Intelligent heat control prevents extreme heat damage, 3 speed and 4 heat settings, magnetic attachments, 2-year warranty.", "37900", 25, "Beauty", 4.6, 2100,
            U+"1522338242992-e1a54906a8da"+Q, U+"1556228720-195a672e8a03"+Q, U+"1558769132-cb1aea458c5e"+Q));
        products.add(p("Maybelline Fit Me Matte Foundation", "Blurs pores, controls oil, natural matte finish. Lightweight buildable coverage. SPF 22. 30ml. 40+ shades.", "499", 250, "Beauty", 4.4, 14000,
            U+"1558769132-cb1aea458c5e"+Q, U+"1556228720-195a672e8a03"+Q, U+"1512496015851-a90fb38ba796"+Q));
        products.add(p("WOW Skin Science Vitamin C Face Serum", "10% Vitamin C with hyaluronic acid, brightens skin, reduces dark spots, boosts collagen. 30ml.", "599", 200, "Beauty", 4.4, 9800,
            U+"1556228720-195a672e8a03"+Q, U+"1558769132-cb1aea458c5e"+Q, U+"1541643600914-78b084683702"+Q));
        products.add(p("Forest Essentials Sandalwood Face Wash", "Ayurvedic pure rosewater, sandalwood and turmeric formula. Brightens, tones, and deeply cleanses skin. 150ml.", "895", 80, "Beauty", 4.6, 3200,
            U+"1584622650791-4bd0dc093d61"+Q, U+"1556228720-195a672e8a03"+Q, U+"1558769132-cb1aea458c5e"+Q));
        products.add(p("Nykaa 9 to 9 Matte Lipstick", "Ultra-matte finish with 12-hour stay. Nourishing formula with Vitamin E. Highly pigmented, non-drying. 3.8g.", "349", 300, "Beauty", 4.4, 11000,
            U+"1512496015851-a90fb38ba796"+Q, U+"1558769132-cb1aea458c5e"+Q, U+"1556228720-195a672e8a03"+Q));
        products.add(p("Philips BHS378 Hair Straightener", "ThermoProtect technology, ionic conditioning, 230°C salon-straight results, 1-hour auto shut-off, 2m swivel cord.", "2499", 60, "Beauty", 4.5, 7600,
            U+"1522338242992-e1a54906a8da"+Q, U+"1558769132-cb1aea458c5e"+Q, U+"1556228720-195a672e8a03"+Q));
        products.add(p("Mamaearth Onion Hair Oil 250ml", "Onion and redensyl formula, reduces hair fall, promotes hair growth, no harmful chemicals, clinically tested.", "349", 400, "Beauty", 4.3, 18000,
            U+"1558769132-cb1aea458c5e"+Q, U+"1556228720-195a672e8a03"+Q, U+"1584622650791-4bd0dc093d61"+Q));
        products.add(p("Cetaphil Moisturizing Cream 250g", "Dermatologist recommended, fragrance-free, non-comedogenic, 48-hour moisturisation, suitable for sensitive skin.", "699", 180, "Beauty", 4.6, 12500,
            U+"1556228720-195a672e8a03"+Q, U+"1584622650791-4bd0dc093d61"+Q, U+"1558769132-cb1aea458c5e"+Q));
        products.add(p("Biotique Bio Sunscreen SPF 50 PA+++", "100% organic botanical sunscreen with bio-blend of sea algae. Broad spectrum, water resistant, 50ml.", "249", 350, "Beauty", 4.3, 7800,
            U+"1584622650791-4bd0dc093d61"+Q, U+"1556228720-195a672e8a03"+Q, U+"1558769132-cb1aea458c5e"+Q));
        products.add(p("Lakme Eyeconic Kajal Twin Pack", "Long-lasting smudge-proof kajal with built-in sharpener. Rich black pigment, lasts 16 hours, water resistant.", "299", 400, "Beauty", 4.5, 21000,
            U+"1512496015851-a90fb38ba796"+Q, U+"1558769132-cb1aea458c5e"+Q, U+"1556228720-195a672e8a03"+Q));
        products.add(p("SUGAR Cosmetics Eye Told You So Eyeliner", "Smudge-proof, waterproof felt tip eyeliner. Micro-fine tip for precise lines. Lasts 18+ hours.", "549", 220, "Beauty", 4.5, 8900,
            U+"1558769132-cb1aea458c5e"+Q, U+"1512496015851-a90fb38ba796"+Q, U+"1556228720-195a672e8a03"+Q));
        products.add(p("L'Oreal Paris 6.0 Dark Blonde Hair Color", "Ammonia-free hair colour, up to 100% grey coverage, conditioning formula with goji oil and vitamin E. 72ml.", "399", 200, "Beauty", 4.4, 9200,
            U+"1522338242992-e1a54906a8da"+Q, U+"1558769132-cb1aea458c5e"+Q, U+"1556228720-195a672e8a03"+Q));
        products.add(p("Dove Original Body Lotion 400ml", "24-hour moisturisation, 1/4 moisturising cream formula, lightweight non-greasy texture, dermatologist tested.", "299", 300, "Beauty", 4.5, 14500,
            U+"1556228720-195a672e8a03"+Q, U+"1584622650791-4bd0dc093d61"+Q, U+"1558769132-cb1aea458c5e"+Q));
        products.add(p("Colorbar Smokey Eye Mini Palette", "4 professional eyeshadow shades, buildable intensity, velvety texture, long-lasting formula, travel-friendly.", "1299", 90, "Beauty", 4.4, 4600,
            U+"1558769132-cb1aea458c5e"+Q, U+"1512496015851-a90fb38ba796"+Q, U+"1556228720-195a672e8a03"+Q));
        products.add(p("Plum Bright Years Cell Renewal Serum", "0.3% retinol + 0.5% niacinamide, reduces fine lines, improves skin texture, fast-absorbing, paraben-free. 30ml.", "699", 110, "Beauty", 4.5, 5600,
            U+"1584622650791-4bd0dc093d61"+Q, U+"1556228720-195a672e8a03"+Q, U+"1558769132-cb1aea458c5e"+Q));
        products.add(p("Neutrogena Ultra Sheer Sunscreen SPF 100", "Ultra-light, non-greasy formula, Helioplex technology, broad spectrum UVA/UVB protection, water resistant 80 min.", "699", 160, "Beauty", 4.5, 11000,
            U+"1584622650791-4bd0dc093d61"+Q, U+"1558769132-cb1aea458c5e"+Q, U+"1556228720-195a672e8a03"+Q));
        products.add(p("VLCC Diamond Bleach Cream 60g", "Brightens and whitens skin tone, reduces dark spots and blemishes, with coconut oil and almond extracts.", "299", 250, "Beauty", 4.2, 6700,
            U+"1556228720-195a672e8a03"+Q, U+"1584622650791-4bd0dc093d61"+Q, U+"1558769132-cb1aea458c5e"+Q));

        // ── Health & Wellness ─────────────────────────────────────────────────
        products.add(p("ON Gold Standard 100% Whey Protein 2lb", "24g protein per serving, 5.5g BCAAs, fast-absorbing whey isolate, 2lbs/30 servings, Double Rich Chocolate.", "2899", 80, "Health & Wellness", 4.7, 14500,
            U+"1593095948071-474c5cc2989d"+Q, U+"1584308666744-24d5c474f2ae"+Q, U+"1591311629554-41e5e9b2e18b"+Q));
        products.add(p("Fitbit Charge 6 Fitness Tracker", "Built-in GPS, heart rate monitoring, sleep tracking, SpO2 sensor, 7-day battery, works with Google Maps.", "14999", 45, "Health & Wellness", 4.4, 4100,
            U+"1575311373937-040b8e1fd5b6"+Q, U+"1523275335684-37898b6baf30"+Q, U+"1546868871-7041f2a55e12"+Q));
        products.add(p("Theragun Prime Massage Gun", "4 built-in speeds (1750-2400 PPM), 16mm amplitude, 4 attachments, whisper-quiet motor, 2-hour battery.", "24999", 22, "Health & Wellness", 4.6, 2800,
            U+"1591311629554-41e5e9b2e18b"+Q, U+"1534438327276-14e5300c3a48"+Q, U+"1593095948071-474c5cc2989d"+Q));
        products.add(p("Omron HEM 7120 Blood Pressure Monitor", "Fully automatic, clinically validated, IntelliSense technology, large cuff (22-32cm), 30-reading memory.", "2499", 60, "Health & Wellness", 4.5, 9800,
            U+"1584308666744-24d5c474f2ae"+Q, U+"1593095948071-474c5cc2989d"+Q, U+"1575311373937-040b8e1fd5b6"+Q));
        products.add(p("Abbott Ensure NutriVigor 400g Vanilla", "Complete balanced nutrition for adults 50+, HMB for muscle health, 28 vitamins & minerals, low fat.", "1299", 70, "Health & Wellness", 4.5, 5400,
            U+"1593095948071-474c5cc2989d"+Q, U+"1584308666744-24d5c474f2ae"+Q, U+"1591311629554-41e5e9b2e18b"+Q));
        products.add(p("Wellbeing Nutrition Daily Greens Tablets", "45+ superfoods including spirulina, wheatgrass, moringa, vitamins D3, B12, C. Vegan, 60 tablets.", "699", 200, "Health & Wellness", 4.4, 3200,
            U+"1584308666744-24d5c474f2ae"+Q, U+"1593095948071-474c5cc2989d"+Q, U+"1575311373937-040b8e1fd5b6"+Q));
        products.add(p("Xiaomi Mi Air Purifier 4 Compact", "3-layer filtration, HEPA filter, removes PM2.5, formaldehyde, OLED display, 360m³/h CADR, quiet 28dB.", "8999", 30, "Health & Wellness", 4.5, 3800,
            U+"1585771273612-3bab13a4b08c"+Q, U+"1527515637-462e94beb0e5"+Q, U+"1558618666-fcd25c85cd64"+Q));
        products.add(p("Dr. Morepen Pulse Oximeter PO-05", "Medical grade accuracy, OLED display, 1-click measurement, perfusion index, plethysmograph waveform, auto power-off.", "899", 150, "Health & Wellness", 4.4, 12000,
            U+"1584308666744-24d5c474f2ae"+Q, U+"1575311373937-040b8e1fd5b6"+Q, U+"1593095948071-474c5cc2989d"+Q));
        products.add(p("MuscleBlaze Creatine Monohydrate 250g", "Micronised for better absorption, increases strength and power output, unflavoured, mixes easily, 83 servings.", "699", 100, "Health & Wellness", 4.6, 7600,
            U+"1593095948071-474c5cc2989d"+Q, U+"1584308666744-24d5c474f2ae"+Q, U+"1591311629554-41e5e9b2e18b"+Q));
        products.add(p("Himalaya Ashwagandha 60 Tablets", "Supports healthy stress response, improves energy and vitality, promotes restful sleep. Pure Ashwagandha root extract.", "299", 300, "Health & Wellness", 4.4, 8900,
            U+"1584308666744-24d5c474f2ae"+Q, U+"1593095948071-474c5cc2989d"+Q, U+"1575311373937-040b8e1fd5b6"+Q));
        products.add(p("Beurer FT 09 Digital Thermometer", "Fast 10-second measurement, fever alarm, last reading memory, waterproof, flexible tip, auto power-off.", "499", 200, "Health & Wellness", 4.5, 6700,
            U+"1584308666744-24d5c474f2ae"+Q, U+"1575311373937-040b8e1fd5b6"+Q, U+"1593095948071-474c5cc2989d"+Q));
        products.add(p("Organic India Tulsi Holy Basil 60 Caps", "100% certified organic Tulsi (Holy Basil), adaptogenic herb, supports immune system, stress relief, energy.", "299", 250, "Health & Wellness", 4.4, 5600,
            U+"1584308666744-24d5c474f2ae"+Q, U+"1593095948071-474c5cc2989d"+Q, U+"1591311629554-41e5e9b2e18b"+Q));
        products.add(p("Horlicks Protein+ Chocolate 400g", "23g protein per 100g, 24 essential vitamins and minerals, gluten-free, no added sugar option, muscle growth.", "699", 90, "Health & Wellness", 4.3, 4200,
            U+"1593095948071-474c5cc2989d"+Q, U+"1584308666744-24d5c474f2ae"+Q, U+"1575311373937-040b8e1fd5b6"+Q));
        products.add(p("Tynor Knee Cap Large (Pair)", "Neoprene knee support with open patella design, provides compression, warmth and support, universal size.", "399", 180, "Health & Wellness", 4.4, 7800,
            U+"1591311629554-41e5e9b2e18b"+Q, U+"1534438327276-14e5300c3a48"+Q, U+"1574680096145-d05b474e2155"+Q));
        products.add(p("Lipton Green Tea Honey Lemon 100 Bags", "Natural antioxidants, light refreshing taste, zero calories, individually wrapped tea bags. Box of 100.", "299", 400, "Health & Wellness", 4.4, 15000,
            U+"1556679343-c7306c1976bc"+Q, U+"1442975631134-b28b4c93e586"+Q, U+"1509042236881-f0f98c2f3ce6"+Q));
        products.add(p("HealthSense Glucometer Gluco Smart", "Fast 5-second testing, large display, 300-reading memory, no coding required, includes 10 test strips and lancets.", "999", 80, "Health & Wellness", 4.4, 5600,
            U+"1584308666744-24d5c474f2ae"+Q, U+"1575311373937-040b8e1fd5b6"+Q, U+"1593095948071-474c5cc2989d"+Q));
        products.add(p("Himalaya Face Wash Purifying Neem 150ml", "Neem and turmeric formula, removes impurities and bacteria, prevents pimples, oil-free, pH balanced.", "179", 500, "Health & Wellness", 4.4, 22000,
            U+"1584622650791-4bd0dc093d61"+Q, U+"1556228720-195a672e8a03"+Q, U+"1558769132-cb1aea458c5e"+Q));
        products.add(p("Zandu Balm Ultra Power 25ml", "Pain relief balm with Pudina satva and Gandhapatri oil. Relieves headache, cold, body ache, muscular pain.", "99", 600, "Health & Wellness", 4.5, 11000,
            U+"1584308666744-24d5c474f2ae"+Q, U+"1593095948071-474c5cc2989d"+Q, U+"1591311629554-41e5e9b2e18b"+Q));
        products.add(p("Boldfit Foam Roller High Density 45cm", "Extra-firm high-density foam, grid texture for deep tissue massage, ideal for back, legs, glutes, calves.", "699", 150, "Health & Wellness", 4.4, 3800,
            U+"1598289431512-b97b0917afac"+Q, U+"1601925228008-40a44f3f2c95"+Q, U+"1540497077202-7c8a3999166f"+Q));
        products.add(p("Yoga Bolster Rectangular Support Pillow", "Firm cotton-filled rectangular bolster, removable machine-washable cover, ideal for restorative yoga poses.", "1299", 60, "Health & Wellness", 4.5, 1800,
            U+"1601925228008-40a44f3f2c95"+Q, U+"1571019614242-c5c5dee9f50b"+Q, U+"1540497077202-7c8a3999166f"+Q));

        // ── Grocery ───────────────────────────────────────────────────────────
        products.add(p("Tata Tea Premium 1kg", "Rich and aromatic premium blend from the finest Assam and Darjeeling tea gardens. Perfect morning brew.", "399", 500, "Grocery", 4.5, 8900,
            U+"1556679343-c7306c1976bc"+Q, U+"1442975631134-b28b4c93e586"+Q, U+"1509042236881-f0f98c2f3ce6"+Q));
        products.add(p("Cadbury Celebrations Gift Box 200g", "Assorted Cadbury chocolates — Dairy Milk, 5 Star, Perk, Gems and Eclairs in a festive gift box.", "349", 300, "Grocery", 4.6, 5600,
            U+"1587132137056-bfbf0166836e"+Q, U+"1474979266404-7eaacbcd87c5"+Q, U+"1556679343-c7306c1976bc"+Q));
        products.add(p("Fortune Kachi Ghani Mustard Oil 5L", "Cold pressed mustard oil with natural pungency. 100% pure, no added preservatives, rich in omega-3.", "799", 150, "Grocery", 4.3, 2100,
            U+"1474979266404-7eaacbcd87c5"+Q, U+"1556679343-c7306c1976bc"+Q, U+"1587132137056-bfbf0166836e"+Q));
        products.add(p("Aashirvaad Atta Whole Wheat 10kg", "Made from whole wheat, rich in fibre and nutrients, ideal for soft rotis, nutritious whole grain flour.", "599", 200, "Grocery", 4.6, 12000,
            U+"1574979266404-7eaacbcd87c5"+Q, U+"1509042236881-f0f98c2f3ce6"+Q, U+"1556679343-c7306c1976bc"+Q));
        products.add(p("India Gate Basmati Rice Classic 5kg", "Extra long grains, aromatic, non-sticky, aged for over a year. Premium basmati rice for biryani and pulao.", "899", 180, "Grocery", 4.7, 9800,
            U+"1574979266404-7eaacbcd87c5"+Q, U+"1556679343-c7306c1976bc"+Q, U+"1442975631134-b28b4c93e586"+Q));
        products.add(p("Amul Pure Ghee 1L Tin", "Made from fresh pasteurised milk, traditional Bilona process, rich aroma, granular texture. 1-litre tin.", "699", 200, "Grocery", 4.7, 14000,
            U+"1509042236881-f0f98c2f3ce6"+Q, U+"1556679343-c7306c1976bc"+Q, U+"1474979266404-7eaacbcd87c5"+Q));
        products.add(p("Kissan Mixed Fruit Jam 500g", "Made with real fruit pulp, 12 fruits blend, no artificial preservatives, ideal for bread and chapati.", "175", 400, "Grocery", 4.4, 7200,
            U+"1587132137056-bfbf0166836e"+Q, U+"1556679343-c7306c1976bc"+Q, U+"1509042236881-f0f98c2f3ce6"+Q));
        products.add(p("Maggi 2-Minute Noodles Masala 12-Pack", "India's #1 instant noodles. Original masala flavour, cooks in 2 minutes, pack of 12 x 70g pouches.", "168", 600, "Grocery", 4.5, 32000,
            U+"1574979266404-7eaacbcd87c5"+Q, U+"1509042236881-f0f98c2f3ce6"+Q, U+"1587132137056-bfbf0166836e"+Q));
        products.add(p("Bru Instant Coffee Gold 200g", "Rich and aromatic instant coffee blend, smooth taste with natural coffee aroma. Perfect for filter and instant brewing.", "299", 250, "Grocery", 4.5, 9400,
            U+"1442975631134-b28b4c93e586"+Q, U+"1495474472287-4d71bcdd2085"+Q, U+"1509042236881-f0f98c2f3ce6"+Q));
        products.add(p("Parle-G Original Glucose Biscuits 800g", "India's #1 selling biscuit brand. Crispy glucose biscuits with original wheat flavour. Great with tea.", "75", 800, "Grocery", 4.5, 45000,
            U+"1587132137056-bfbf0166836e"+Q, U+"1474979266404-7eaacbcd87c5"+Q, U+"1556679343-c7306c1976bc"+Q));
        products.add(p("MDH Garam Masala 100g", "Classic blend of 15 aromatic spices, used in gravies, biryanis and snacks. No artificial colour or flavour.", "99", 500, "Grocery", 4.6, 11000,
            U+"1474979266404-7eaacbcd87c5"+Q, U+"1556679343-c7306c1976bc"+Q, U+"1509042236881-f0f98c2f3ce6"+Q));
        products.add(p("Real Juice Mixed Fruit 1L Tetra Pack", "100% real fruit juice with vitamins, no added preservatives, no artificial colour, contains 8 fruits.", "89", 300, "Grocery", 4.3, 6800,
            U+"1587132137056-bfbf0166836e"+Q, U+"1474979266404-7eaacbcd87c5"+Q, U+"1556679343-c7306c1976bc"+Q));
        products.add(p("Haldiram's Aloo Bhujia 400g", "Crispy potato and spice snack, zero trans fat, no artificial preservatives. India's most popular namkeen.", "149", 400, "Grocery", 4.5, 18000,
            U+"1587132137056-bfbf0166836e"+Q, U+"1556679343-c7306c1976bc"+Q, U+"1509042236881-f0f98c2f3ce6"+Q));
        products.add(p("Saffola Gold Cooking Oil 5L", "Dual seed oil with rice bran and sunflower oil. Low saturated fats, good for heart health.", "1099", 120, "Grocery", 4.4, 7600,
            U+"1474979266404-7eaacbcd87c5"+Q, U+"1509042236881-f0f98c2f3ce6"+Q, U+"1556679343-c7306c1976bc"+Q));
        products.add(p("Everest Kitchen King Masala 100g", "Blend of 23 spices including cumin, coriander, turmeric, garam masala, red chilli. Makes curries rich and aromatic.", "69", 600, "Grocery", 4.5, 9200,
            U+"1474979266404-7eaacbcd87c5"+Q, U+"1556679343-c7306c1976bc"+Q, U+"1587132137056-bfbf0166836e"+Q));
        products.add(p("Amul Butter Pasteurised 500g", "Made from fresh pasteurised cream, rich taste, no artificial colouring, perfect for cooking and spreading.", "275", 350, "Grocery", 4.7, 21000,
            U+"1509042236881-f0f98c2f3ce6"+Q, U+"1442975631134-b28b4c93e586"+Q, U+"1474979266404-7eaacbcd87c5"+Q));
        products.add(p("Nestlé KitKat 9 Fingers Multipack", "9 individually wrapped KitKat fingers, crispy wafer chocolate. Perfect gift or snack. Pack of 9 x 18g.", "150", 500, "Grocery", 4.6, 14000,
            U+"1587132137056-bfbf0166836e"+Q, U+"1474979266404-7eaacbcd87c5"+Q, U+"1556679343-c7306c1976bc"+Q));
        products.add(p("Daawat Extra Long Basmati Rice 5kg", "Premium extra long grain basmati, aromatic, naturally aged 2 years. Grains elongate 2x when cooked.", "999", 150, "Grocery", 4.7, 8700,
            U+"1574979266404-7eaacbcd87c5"+Q, U+"1556679343-c7306c1976bc"+Q, U+"1442975631134-b28b4c93e586"+Q));
        products.add(p("Tata Salt Iodised 1kg", "Vacuum evaporated iodised salt, free-flowing, fine grain, rich in essential minerals, vacuum-packed freshness.", "24", 1000, "Grocery", 4.5, 35000,
            U+"1556679343-c7306c1976bc"+Q, U+"1509042236881-f0f98c2f3ce6"+Q, U+"1474979266404-7eaacbcd87c5"+Q));
        products.add(p("Borges Extra Virgin Olive Oil 500ml", "Cold extracted from fresh olives, unrefined, rich in polyphenols and antioxidants, ideal for salads and cooking.", "699", 80, "Grocery", 4.6, 5400,
            U+"1474979266404-7eaacbcd87c5"+Q, U+"1509042236881-f0f98c2f3ce6"+Q, U+"1556679343-c7306c1976bc"+Q));

        // ── Toys & Games ─────────────────────────────────────────────────────
        products.add(p("LEGO Technic Lamborghini Sián 42115", "1:8 scale Lamborghini Sián FKP 37 replica. 3,696 pieces, working gearbox, full opening doors, rear spoiler.", "34999", 20, "Toys & Games", 4.9, 1850,
            U+"1587654780291-39c9404d746b"+Q, U+"1581263016934-f129c6c0e91c"+Q, U+"1503023345310-0d08d2b18b68"+Q));
        products.add(p("Monopoly Classic Board Game", "Classic property trading game for 2-8 players. Includes 8 tokens, 28 title deeds, 2 dice, play money.", "1299", 100, "Toys & Games", 4.5, 6700,
            U+"1611996575749-79a3a250f948"+Q, U+"1610041604659-7f3a5b1db6c3"+Q, U+"1541781229-d6eb6c2c4f3a"+Q));
        products.add(p("PS5 DualSense Wireless Controller", "Haptic feedback, adaptive triggers, built-in microphone, motion sensor, USB-C charging, Tempest 3D AudioTech.", "5990", 60, "Toys & Games", 4.8, 9400,
            U+"1606813907291-d86efa9b94db"+Q, U+"1617096200347-cb04ae810b1d"+Q, U+"1592591452300-2adab0ea5eb3"+Q));
        products.add(p("Hot Wheels 20-Car Gift Pack", "20 die-cast Hot Wheels cars in 1:64 scale, assorted styles and colours, great gift for kids aged 3+.", "1299", 150, "Toys & Games", 4.6, 8900,
            U+"1503023345310-0d08d2b18b68"+Q, U+"1581263016934-f129c6c0e91c"+Q, U+"1611996575749-79a3a250f948"+Q));
        products.add(p("LEGO City Police Station 60316", "927-piece police station set with 5 minifigures, police car, motorcycle, helicopter, jail cell.", "8999", 25, "Toys & Games", 4.8, 2300,
            U+"1581263016934-f129c6c0e91c"+Q, U+"1587654780291-39c9404d746b"+Q, U+"1503023345310-0d08d2b18b68"+Q));
        products.add(p("Nerf Elite 2.0 Shockwave RD-15 Blaster", "Rotating drum holds 15 darts, fires up to 27m, slam-fire action, compatible with all Elite 2.0 darts.", "1999", 80, "Toys & Games", 4.5, 3800,
            U+"1503023345310-0d08d2b18b68"+Q, U+"1611996575749-79a3a250f948"+Q, U+"1610041604659-7f3a5b1db6c3"+Q));
        products.add(p("Barbie Dreamhouse Playset", "65+ accessories, 3 stories, 8 rooms, pool, elevator, working shower, furniture, accessories and 1 doll.", "9999", 15, "Toys & Games", 4.7, 4200,
            U+"1581263016934-f129c6c0e91c"+Q, U+"1503023345310-0d08d2b18b68"+Q, U+"1611996575749-79a3a250f948"+Q));
        products.add(p("Skillmatics Educational Card Game", "No stress, no prep activity games for family game nights. Includes 120+ activity cards for ages 6-99.", "799", 200, "Toys & Games", 4.6, 5600,
            U+"1610041604659-7f3a5b1db6c3"+Q, U+"1611996575749-79a3a250f948"+Q, U+"1541781229-d6eb6c2c4f3a"+Q));
        products.add(p("Ravensburger 1000 Piece Puzzle", "Premium quality 1000-piece jigsaw puzzle, Softclick technology for perfect fit, durable pieces, anti-glare.", "1499", 60, "Toys & Games", 4.7, 3200,
            U+"1541781229-d6eb6c2c4f3a"+Q, U+"1610041604659-7f3a5b1db6c3"+Q, U+"1611996575749-79a3a250f948"+Q));
        products.add(p("Play-Doh Ultimate Color Collection 65-Pack", "65 cans of Play-Doh modelling compound, 2-ounce cans, non-toxic, fun sensory play for ages 2+.", "999", 100, "Toys & Games", 4.6, 7400,
            U+"1503023345310-0d08d2b18b68"+Q, U+"1581263016934-f129c6c0e91c"+Q, U+"1587654780291-39c9404d746b"+Q));
        products.add(p("UNO Card Game Classic", "2-10 players, ages 7+. Classic card game with action cards — Skip, Reverse, Draw Two, Wild and Wild Draw Four.", "299", 400, "Toys & Games", 4.6, 21000,
            U+"1611996575749-79a3a250f948"+Q, U+"1610041604659-7f3a5b1db6c3"+Q, U+"1541781229-d6eb6c2c4f3a"+Q));
        products.add(p("Jenga Classic Block Stacking Game", "54 hardwood blocks, stacking tower game for 2+ players, ages 6+. Includes stacking sleeve for setup.", "799", 150, "Toys & Games", 4.6, 8800,
            U+"1541781229-d6eb6c2c4f3a"+Q, U+"1611996575749-79a3a250f948"+Q, U+"1610041604659-7f3a5b1db6c3"+Q));
        products.add(p("Marvel Avengers 6-Figure Action Set", "6 articulated action figures — Iron Man, Thor, Hulk, Captain America, Black Widow, and Hawkeye. 6-inch scale.", "1499", 80, "Toys & Games", 4.5, 4600,
            U+"1581263016934-f129c6c0e91c"+Q, U+"1503023345310-0d08d2b18b68"+Q, U+"1611996575749-79a3a250f948"+Q));
        products.add(p("RC Car Off-Road 4WD 1:18 Scale", "2.4GHz remote, 4-wheel drive, 30 km/h speed, shock absorbers, rechargeable Li-ion battery, 360° rotation.", "2499", 50, "Toys & Games", 4.4, 3100,
            U+"1503023345310-0d08d2b18b68"+Q, U+"1610041604659-7f3a5b1db6c3"+Q, U+"1581263016934-f129c6c0e91c"+Q));
        products.add(p("Funskool Scrabble Classic Board Game", "Classic word game for 2-4 players, 100 letter tiles, rotating game board, tile holder, age 8+.", "999", 90, "Toys & Games", 4.6, 5200,
            U+"1610041604659-7f3a5b1db6c3"+Q, U+"1541781229-d6eb6c2c4f3a"+Q, U+"1611996575749-79a3a250f948"+Q));
        products.add(p("Wooden Building Blocks Set 100 Pieces", "Natural beechwood, smooth sanded edges, multiple shapes and sizes, promotes creativity and STEM learning.", "999", 120, "Toys & Games", 4.6, 2800,
            U+"1581263016934-f129c6c0e91c"+Q, U+"1503023345310-0d08d2b18b68"+Q, U+"1587654780291-39c9404d746b"+Q));
        products.add(p("Funskool Candy Land Board Game", "Classic colour-coded path game for 2-4 players, ages 3+. No reading required, colourful characters and board.", "699", 130, "Toys & Games", 4.5, 3400,
            U+"1611996575749-79a3a250f948"+Q, U+"1610041604659-7f3a5b1db6c3"+Q, U+"1541781229-d6eb6c2c4f3a"+Q));
        products.add(p("Orion Toys Junior Cricket Set", "Includes plastic bat, ball, stumps and bails. Lightweight, ideal for kids 5-12. Great outdoor play set.", "799", 100, "Toys & Games", 4.3, 2100,
            U+"1548438294-1ad5d5f4f063"+Q, U+"1574680096145-d05b474e2155"+Q, U+"1533681904393-9ab6eee7bdb0"+Q));
        products.add(p("Fisher-Price Baby Musical Piano", "5 light-up keys, 6 playful songs, 3 learning modes — music, letters, numbers. Suitable for ages 6 months+.", "1499", 70, "Toys & Games", 4.6, 4800,
            U+"1503023345310-0d08d2b18b68"+Q, U+"1581263016934-f129c6c0e91c"+Q, U+"1610041604659-7f3a5b1db6c3"+Q));
        products.add(p("Nintendo Mario Kart 8 Deluxe Switch", "42 courses, 48 characters, 4-player local co-op, online racing, Battle Mode. The ultimate kart racer.", "4999", 30, "Toys & Games", 4.9, 12000,
            U+"1627856013091-fed6e4e30025"+Q, U+"1605901309584-818e25960a8f"+Q, U+"1606813907291-d86efa9b94db"+Q));

        productRepository.saveAll(products);
        log.info("Seeded {} products across 9 categories", products.size());
    }
}
