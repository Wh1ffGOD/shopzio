# Ecommerce Clone

A full-stack ecommerce application built with **Java Spring Boot** (backend) and **React + TypeScript** (frontend), with **Razorpay** payment integration.

---

## Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Spring Boot 3.2 | Application framework |
| Spring Web (REST) | REST API layer |
| Spring Data JPA | Database ORM |
| Spring Security | Authentication & Authorization |
| JWT (jjwt 0.11) | Stateless token-based auth |
| Razorpay Java SDK 1.4.3 | Payment processing |
| Lombok | Boilerplate reduction |
| H2 | File-based DB (development) |
| MySQL 8 | Relational DB (production) |
| Maven | Build & dependency management |

### Frontend
| Technology | Purpose |
|---|---|
| React 18 | UI library |
| TypeScript | Type safety |
| Vite | Build tool & dev server |
| Tailwind CSS | Utility-first styling |
| React Router v6 | Client-side routing |
| Axios | HTTP client |
| Redux Toolkit | Global state (cart, auth) |
| Razorpay Checkout (CDN) | Payment popup (loaded dynamically) |

### DevOps
| Technology | Purpose |
|---|---|
| Docker | Containerization |
| Docker Compose | Multi-service orchestration |

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                          CLIENT BROWSER                             │
│                    React + TypeScript (port 5173)                   │
│                                                                     │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌────────────────────┐  │
│  │  Pages   │  │Components│  │  Redux   │  │   Axios API Layer  │  │
│  │ /home    │  │ Navbar   │  │  Store   │  │ /api/auth          │  │
│  │ /products│  │ ProductCard  cartSlice │  │ /api/products      │  │
│  │ /cart    │  │ Footer   │  │ authSlice│  │ /api/orders        │  │
│  │ /checkout│  │          │  │          │  │ /api/payments      │  │
│  └──────────┘  └──────────┘  └──────────┘  └────────┬───────────┘  │
└───────────────────────────────────────────────────────┼─────────────┘
                                                        │ HTTP/JSON
                                               Vite proxy → :8080
                                                        │
┌───────────────────────────────────────────────────────▼─────────────┐
│                     SPRING BOOT BACKEND (port 8080)                  │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │                      Security Layer                           │   │
│  │           JWT Auth Filter → UserDetailsService               │   │
│  └──────────────────────────┬─────────────────────────────────-─┘   │
│                              │                                       │
│  ┌───────────────────────────▼──────────────────────────────────┐   │
│  │                     Controller Layer                          │   │
│  │  AuthController │ ProductController │ OrderController         │   │
│  │  CartController │ PaymentController │ UserController          │   │
│  └───────────────────────────┬──────────────────────────────────┘   │
│                              │                                       │
│  ┌───────────────────────────▼──────────────────────────────────┐   │
│  │                      Service Layer                            │   │
│  │  AuthService │ ProductService │ OrderService                  │   │
│  │  CartService │ PaymentService │ UserService                   │   │
│  └───────────────────────────┬──────────────────────────────────┘   │
│                              │                     │                 │
│  ┌───────────────────────────▼───────────┐         │                 │
│  │         Repository Layer (JPA)        │         ▼                 │
│  │  UserRepo │ ProductRepo │ OrderRepo   │  ┌──────────────┐        │
│  │  CartRepo │ PaymentRepo               │  │ Razorpay API │        │
│  └───────────────────────────┬───────────┘  │  (external)  │        │
│                              │              └──────────────┘        │
│  ┌───────────────────────────▼───────────┐                          │
│  │             Model Layer               │                          │
│  │  User │ Product │ Order │ OrderItem   │                          │
│  │  Cart │ CartItem │ Payment            │                          │
│  └───────────────────────────┬───────────┘                          │
└──────────────────────────────┼───────────────────────────────────────┘
                               │ JDBC
┌──────────────────────────────▼───────────────────────────────────────┐
│                           DATABASE                                    │
│               H2 file-based (dev)  │  MySQL 8 (prod)                 │
└───────────────────────────────────────────────────────────────────────┘
```

---

## Payment Flow (Razorpay)

```
1. User fills shipping address → clicks "Pay"
         │
         ▼
2. POST /api/orders  (backend creates order with status PENDING)
         │
         ▼
3. POST /api/payments/create-order  (backend creates Razorpay order via SDK)
         │                           returns { razorpayOrderId, keyId, amount }
         ▼
4. Frontend opens Razorpay popup using razorpayOrderId + keyId
         │
         ▼
5. User pays via UPI / card / netbanking / wallet
         │
         ├── Success → Razorpay returns { razorpay_order_id, razorpay_payment_id, razorpay_signature }
         │                    │
         │                    ▼
         │    POST /api/payments/verify
         │    Backend verifies HMAC-SHA256 signature
         │    Order status → CONFIRMED | Payment status → PAID
         │
         └── Cancelled → Order stays PENDING (user can pay later)

Test card: 4111 1111 1111 1111 | Any future date | Any CVV
```

---

## Project Structure

```
ecommerce-clone/
├── backend/                              ← Spring Boot Maven project
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/ecommerce/
│   │   │   │   ├── config/               ← SecurityConfig, OpenApiConfig, DataSeeder
│   │   │   │   ├── controller/           ← REST API endpoints
│   │   │   │   │   ├── AuthController
│   │   │   │   │   ├── ProductController
│   │   │   │   │   ├── CartController
│   │   │   │   │   ├── OrderController
│   │   │   │   │   ├── PaymentController
│   │   │   │   │   └── UserController
│   │   │   │   ├── dto/                  ← Request & Response DTOs
│   │   │   │   ├── exception/            ← Custom exceptions & global handler
│   │   │   │   ├── model/                ← JPA entities
│   │   │   │   │   ├── User
│   │   │   │   │   ├── Product
│   │   │   │   │   ├── Order + OrderItem
│   │   │   │   │   ├── Cart + CartItem
│   │   │   │   │   └── Payment
│   │   │   │   ├── repository/           ← Spring Data JPA interfaces
│   │   │   │   ├── security/             ← JwtUtil, JwtAuthFilter, UserDetailsService
│   │   │   │   └── service/              ← Business logic
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── application-dev.properties   ← H2 file-based config
│   │   │       └── application-prod.properties  ← MySQL config
│   │   └── test/
│   └── pom.xml
│
├── frontend/                             ← React + Vite + TypeScript
│   ├── src/
│   │   ├── api/                          ← Axios instance & API calls
│   │   ├── assets/                       ← Images, icons
│   │   ├── components/                   ← Reusable UI components
│   │   ├── pages/                        ← Route-level pages
│   │   ├── store/                        ← Redux store & slices
│   │   └── types/                        ← Shared TypeScript interfaces
│   ├── index.html
│   ├── package.json
│   ├── vite.config.ts                    ← Dev proxy to :8080
│   ├── tailwind.config.js
│   └── tsconfig.json
│
├── docker-compose.yml                    ← MySQL + Backend + Frontend
├── .gitignore
└── README.md
```

---

## API Endpoints

### Auth
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Login & get JWT | Public |

### Products
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/products` | Get all products (paginated) | Public |
| GET | `/api/products/{id}` | Get product by ID | Public |
| GET | `/api/products/category/{cat}` | Filter by category | Public |
| GET | `/api/products/search?keyword=x` | Search products | Public |
| GET | `/api/products/categories` | Get all categories | Public |
| POST | `/api/products` | Create product | Admin |
| PUT | `/api/products/{id}` | Update product | Admin |
| DELETE | `/api/products/{id}` | Soft delete product | Admin |

### Payments (Razorpay)
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/payments/create-order` | Create Razorpay order | User |
| POST | `/api/payments/verify` | Verify payment signature | User |

### Cart
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/cart` | Get user cart | User |
| POST | `/api/cart/add` | Add item to cart | User |
| PUT | `/api/cart/update/{itemId}` | Update item quantity | User |
| DELETE | `/api/cart/remove/{itemId}` | Remove item | User |
| DELETE | `/api/cart/clear` | Clear cart | User |

### Orders
| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/orders` | Place new order | User |
| GET | `/api/orders` | Get my orders | User |
| GET | `/api/orders/{id}` | Get order by ID | User |
| PUT | `/api/orders/{id}/cancel` | Cancel order | User |
| GET | `/api/orders/all` | Get all orders | Admin |
| PUT | `/api/orders/{id}/status` | Update order status | Admin |

### Users
| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/users/profile` | Get own profile | User |
| PUT | `/api/users/profile` | Update profile | User |
| PUT | `/api/users/password` | Change password | User |
| GET | `/api/users` | Get all users | Admin |
| GET | `/api/users/{id}` | Get user by ID | Admin |
| DELETE | `/api/users/{id}` | Delete user | Admin |

---

## Data Flow

```
User Action (React)
      │
      ▼
Redux Action / Axios Call
      │
      ▼
Spring Boot Controller  →  JWT Filter validates token
      │
      ▼
Service Layer  →  Business logic & validation
      │                    │
      │              (checkout only)
      │                    ▼
      │             Razorpay API call
      │             returns razorpayOrderId
      ▼
Repository (JPA)  →  SQL query
      │
      ▼
Database (H2 / MySQL)
      │
      ▼
Response DTO  →  JSON  →  React UI update
```

---

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.9+
- MySQL 8 (or use H2 for dev — no setup needed)
- Razorpay account (free) — razorpay.com
- Docker & Docker Compose (optional)

### Razorpay Setup
1. Sign up at [razorpay.com](https://razorpay.com)
2. Go to Dashboard → Settings → API Keys
3. Generate a **Test Mode** key pair
4. Copy **Key ID** (`rzp_test_...`) and **Key Secret**
5. Add them to `backend/src/main/resources/application.properties`:
   ```properties
   razorpay.key-id=rzp_test_YOUR_KEY_ID
   razorpay.key-secret=YOUR_KEY_SECRET
   ```

### Run Backend (dev mode with H2)
```bash
cd backend
mvn spring-boot:run
# API:        http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui/index.html
# H2 Console: http://localhost:8080/h2-console
```

### H2 Console Login
- JDBC URL: `jdbc:h2:file:./data/ecommercedb;AUTO_SERVER=TRUE`
- Username: `sa` | Password: *(blank)*

### Run Frontend
```bash
cd frontend
npm install
npm run dev
# App: http://localhost:5173
```

### Run with Docker
```bash
docker-compose up --build
```

---

## Roles & Authorization

| Role | Permissions |
|---|---|
| `CUSTOMER` | Browse products, manage own cart, place & view own orders, checkout via Razorpay |
| `ADMIN` | All customer permissions + manage products, view all orders, manage users |

---

## Razorpay Test Cards

| Card Number | Scenario |
|---|---|
| `4111 1111 1111 1111` | Payment succeeds |
| `4242 4242 4242 4242` | Payment succeeds |
| `5104 0600 0000 0008` | Rupay card (success) |

Use any future expiry date and any CVV. You can also test with UPI ID `success@razorpay` in test mode.

---

## Default Admin Credentials

| Field | Value |
|---|---|
| Email | `rohansinghofficial13@gmail.com` |
| Password | `Letme@52` |

---

## Development Notes

- JWT token sent in `Authorization: Bearer <token>` header
- Dev profile uses **H2 file-based DB** at `backend/data/ecommercedb.mv.db`
- Prod profile uses **MySQL** — update `application-prod.properties`
- Razorpay signature verified via HMAC-SHA256 (`orderId|paymentId` signed with keySecret)
- Frontend Vite proxy forwards `/api/*` to `http://localhost:8080`
- Razorpay amounts are in **paise** (smallest currency unit) — ₹99.00 = 9900 paise
- Razorpay JS SDK loaded dynamically from CDN — no npm package needed
