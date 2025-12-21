# QuickInv

**Smart Inventory Management Mobile Application**

QuickInv is an Android application designed to help individuals and small businesses efficiently manage their inventory

## 📱 Features

### Core Functionality

- **User Authentication**

  - Registration and login functionality
  - User-specific inventory management

- **Inventory Management**

  - Add new items with detailed information (name, quantity, price, description)
  - Edit existing inventory items
  - Delete items with confirmation
  - Real-time search functionality
  - Organized list view with RecyclerView

- **Currency Conversion**

  - Built-in currency converter tool
  - Real-time exchange rates via API integration
  - Support for multiple currencies
  - Easy-to-use dialog interface

- **Data Persistence**
  - Local SQLite database storage
  - Automatic timestamp tracking for items
  - Items linked to specific users

### User Interface

- Material Design components
- Clean and intuitive navigation
- Splash screen for app launch
- Search functionality in the action bar
- Responsive layouts

## 🛠️ Tech Stack

### Technologies & Frameworks

- **Language:** Java
- **Minimum SDK:** API 26 (Android 8.0 Oreo)
- **Target SDK:** API 34 (Android 14)
- **Build System:** Gradle (Kotlin DSL)

### Key Dependencies

```gradle
- AndroidX AppCompat: 1.6.1
- Material Components: 1.11.0
- ConstraintLayout: 2.1.4
- RecyclerView: 1.3.0
- JSON Library: 20231013
- JUnit: 4.13.2
- Espresso: 3.5.1
```

### Architecture Components

- **Database:** SQLite with custom DatabaseHelper
- **DAO Pattern:** Separate Data Access Objects for Users and Items
- **Session Management:** Custom SessionManager for user authentication state
- **API Integration:** Currency exchange API (ExchangeRate-API)

## 🚀 Getting Started

### Prerequisites

- Android Studio (Arctic Fox or newer)
- JDK 8 or higher
- Android SDK with API 26+
- Gradle 8.1.2+

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/QuickInv.git
   cd QuickInv
   ```

2. **Open in Android Studio**

   - Launch Android Studio
   - Select "Open an existing project"

3. **Build & Run on emulator or device**

   - Connect an Android device or start an emulator
   - Click the "Run" button in Android Studio

## 🔧 Configuration

### API Keys

The currency converter uses ExchangeRate-API. To use your own API key:

1. Sign up at [ExchangeRate-API](https://www.exchangerate-api.com/)
2. Update the API key in `CurrencyConverter.java`:
   ```java
   private static final String API_KEY = "your_api_key_here";
   ```

### Customization

- **App Name:** Modify `res/values/strings.xml`
- **Theme:** Edit `res/values/styles.xml` or `themes.xml`
- **Icons:** Replace files in `res/drawable/`

## 📸 Screenshots

Screenshots are available in the [`screenshots/`](/screenshots/) directory showing:

- Splash screen
- Login and registration
- Inventory list view
- Add/Edit item screens
- Currency converter
- Search functionality
