# Spring Boot E-commerce API

- Managing products
- Managing shopping carts
- Checking out
- Viewing order history

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/d-kens/store
cd store
```

### 2. Configure Environment Variables
- Rename the ``.env.example`` file to ``.env``. 
- Update the following environment variables inside .env: 
1. JWT_SECRET
2. STRIPE_SECRET_KEY
3. STRIPE_WEBHOOK_SECRET_KEY


## ▶️ Running the Project

This is a Maven project. To start the application, run:

```bash
./mvnw spring-boot:run
```


## 📚 API Documentation

Swagger UI is available at:

```bash
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Example API Flow

Here's a sample flow to help you understand how to interact with the API after starting the application.

### 1. Get All Products 

```bash
GET /products
```

The database is automatically populated with 10 sample products using a Flyway migration script.

### 2. Create a Shopping Cart 

```bash
POST /carts
```

This will return the cart ID. You don't need to be logged in to create a cart.

### 3. Add Items to Cart 

Once you have a cart ID, you can add products to it by sending:

```bash
POST /carts/{cartId}/items
```

**Request body example**:
```json
{
  "productId": 1
}
```

### 4. Register a New User 
To check out, you have to register and login first: 

```bash
POST /users
```

**Request body**:

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "123456"
}
```

### 5. Login to Get an Access Token 

```bash
POST /auth/login 
```

**Request body**:
```json
{
  "email": "john@example.com",
  "password": "123456"
}
```

**Response body**:
```json
{
  "token": "your-json-web-token"
}
```

### 6. Checkout 

```bash
POST /checkout 
```

**Headers**
```bash
Authorization: Bearer your-json-web-token
```

**Request body**
```json
{
  "cartId": "your-cart-id"
}
```

This endpoint returns a Stripe checkout URL. Open it in your browser to complete the payment using a test card:

```yaml
Card: 4242 4242 4242 4242
Expiry: Any future date
CVC: Any 3 digits
```

### 7. Webhook & Order Status Update

Once payment is completed, Stripe will trigger a webhook call to:

```bash
POST /checkout/webhook 
```

The backend listens for this event and updates the order status in the database accordingly.

---
