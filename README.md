# Levon - Online Shopping App

Levon is an Android-based online shopping app built to provide a seamless shopping experience. It allows users to browse products (e.g., airpods, jeans, laptops), manage their cart, favorite items, and handle user authentication.

## Project Structure
- **Key Activities:** 
  - `SplashActivity`: App entry point.
  - `HomePageActivity`: Main page for browsing categories.
  - `ItemDetailActivity`: Displays product details.
  - `CartActivity`: Manages the shopping cart.
  - Includes additional category-specific activities (e.g., `JeansActivity`, `AirpodsActivity`) for browsing various products.
- **ItemManager:** A utility class that handles item data, stock management, and persistence using `SharedPreferences`, ensuring seamless integration across activities like `CartActivity` and `FavoritesActivity`.
- **Layouts:** Custom layouts for cart items (`cart_item_layout.xml`), favorites (`favorite_item_layout.xml`), and item lists (`item_list_layout.xml`).

## Tech Stack
- Built with Android Studio, Java, and XML.

## Setup
1. Clone the repository.
2. Open in Android Studio.
3. Build and run on an emulator or device.

Explore the app to shop for your favorite products!
