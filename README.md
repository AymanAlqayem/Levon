# Levon – Online Shopping Android Application

Levon is an **Android-based online shopping application** designed to deliver a seamless and intuitive mobile shopping experience.  
The app allows users to browse products across multiple categories (such as airpods, jeans, and laptops), view detailed product information, manage a shopping cart, mark favorite items, and handle user authentication efficiently.

---

## 🚀 Key Features
- Browse products by category
- View detailed product information
- Add and remove items from the shopping cart
- Manage favorite products
- User authentication flow
- Persistent cart and favorites using local storage
- Smooth navigation between activities

---

## 🏗 Project Structure

### Key Activities
- **SplashActivity** – Application entry point  
- **HomePageActivity** – Main screen for browsing product categories  
- **ItemDetailActivity** – Displays detailed product information  
- **CartActivity** – Manages shopping cart operations  
- **Category Activities** – Dedicated activities for product categories such as:
  - `JeansActivity`
  - `AirpodsActivity`
  - `LaptopActivity`

### Item Management
- **ItemManager**  
  A utility class responsible for:
  - Managing item data and stock
  - Handling cart and favorite items
  - Persisting data using **SharedPreferences**
  - Ensuring data consistency across activities such as `CartActivity` and `FavoritesActivity`

### UI Layouts
- **cart_item_layout.xml** – Layout for cart items  
- **favorite_item_layout.xml** – Layout for favorite items  
- **item_list_layout.xml** – Layout for product listings  

---

## 🛠 Technology Stack
- **Programming Language:** Java  
- **Platform:** Android SDK  
- **UI:** XML-based layouts  
- **IDE:** Android Studio  
- **Local Storage:** SharedPreferences  

---

## ⚙️ Installation & Setup

### Prerequisites
- Android Studio
- JDK 8+
- Android SDK

### Steps
```bash
# Clone the repository
git clone <repository-url>
cd Levon-Online-Shopping-App

# Open the project in Android Studio

# Sync Gradle dependencies

# Run the application on an emulator or physical device
