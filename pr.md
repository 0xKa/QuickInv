# QuickInv - Complete Project Documentation

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Architecture & Design](#architecture--design)
3. [Project Structure](#project-structure)
4. [Root Configuration Files](#root-configuration-files)
5. [Application Module (app/)](#application-module-app)
6. [Java Source Code](#java-source-code)
7. [Resource Files](#resource-files)
8. [Database Design](#database-design)
9. [Application Flow](#application-flow)
10. [API Integration](#api-integration)

---

## 📱 Project Overview

**QuickInv** is a native Android inventory management application built with Java. It enables users to register, login, and manage their personal inventory items with features like real-time search and currency conversion.

### Key Features

- **User Authentication**: Secure registration and login system
- **Inventory CRUD Operations**: Create, Read, Update, Delete inventory items
- **Real-time Search**: Search inventory items by name
- **Currency Conversion**: Built-in currency converter using ExchangeRate-API
- **Local Data Storage**: SQLite database for offline data persistence
- **Session Management**: SharedPreferences-based user session handling

### Technical Specifications

| Specification | Value                     |
| ------------- | ------------------------- |
| Language      | Java                      |
| Min SDK       | API 26 (Android 8.0 Oreo) |
| Target SDK    | API 34 (Android 14)       |
| Build System  | Gradle 8.1.2 (Kotlin DSL) |
| Package Name  | `com.example.quickinv`    |

---

## 🏗️ Architecture & Design

The application follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                    UI Layer (Activities)                │
│   SplashActivity | LoginActivity | InventoryActivity    │
├─────────────────────────────────────────────────────────┤
│                    Adapter Layer                        │
│                     ItemAdapter                         │
├─────────────────────────────────────────────────────────┤
│                    Utility Layer                        │
│           SessionManager | CurrencyConverter            │
├─────────────────────────────────────────────────────────┤
│                    Data Layer (DAO)                     │
│               UserDAO | ItemDAO                         │
├─────────────────────────────────────────────────────────┤
│                   Database Layer                        │
│                   DatabaseHelper                        │
├─────────────────────────────────────────────────────────┤
│                    Model Layer                          │
│                   User | Item                           │
└─────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
QuickInv/
├── build.gradle.kts              # Root build configuration
├── settings.gradle.kts           # Project settings
├── gradle.properties             # Gradle properties
├── gradlew                       # Gradle wrapper script (Unix)
├── gradlew.bat                   # Gradle wrapper script (Windows)
├── local.properties              # Local SDK path configuration
├── README.md                     # Project readme
├── pr.md                         # This documentation file
│
├── app/                          # Main application module
│   ├── build.gradle.kts          # Module build configuration
│   ├── proguard-rules.pro        # ProGuard rules
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/quickinv/
│       │   │   ├── activities/   # UI Activities
│       │   │   ├── adapters/     # RecyclerView Adapters
│       │   │   ├── database/     # Database & DAOs
│       │   │   ├── models/       # Data Models
│       │   │   ├── utils/        # Utility Classes
│       │   │   └── MainActivity.java
│       │   └── res/              # Resources
│       ├── androidTest/          # Instrumented tests
│       └── test/                 # Unit tests
│
├── gradle/wrapper/               # Gradle wrapper files
├── screenshots/                  # App screenshots
├── temp/                         # Temporary/reference files
└── zip/                          # Archive files
```

---

## ⚙️ Root Configuration Files

### build.gradle.kts (Root)

**Location**: `build.gradle.kts`

**Purpose**: Top-level build configuration that applies to all modules.

```kotlin
plugins {
    id("com.android.application") version "8.1.2" apply false
}
```

- Declares the Android Gradle Plugin version 8.1.2
- `apply false` means it's available but not applied at root level

---

### settings.gradle.kts

**Location**: `settings.gradle.kts`

**Purpose**: Configures project settings and repository sources.

**Key Components**:

| Section                                       | Purpose                             |
| --------------------------------------------- | ----------------------------------- |
| `pluginManagement.repositories`               | Where Gradle looks for plugins      |
| `dependencyResolutionManagement.repositories` | Where Gradle looks for dependencies |
| `rootProject.name`                            | Sets project name as "QuickInv"     |
| `include(":app")`                             | Includes the app module             |

**Repository Sources**:

- `google()` - Google's Maven repository (Android libraries)
- `mavenCentral()` - Central Maven repository
- `gradlePluginPortal()` - Gradle plugins

---

### gradle.properties

**Location**: `gradle.properties`

**Purpose**: JVM and AndroidX configuration.

**Key Properties**:

| Property                      | Value                             | Purpose                       |
| ----------------------------- | --------------------------------- | ----------------------------- |
| `org.gradle.jvmargs`          | `-Xmx2048m -Dfile.encoding=UTF-8` | JVM memory (2GB) and encoding |
| `android.useAndroidX`         | `true`                            | Enable AndroidX libraries     |
| `android.nonTransitiveRClass` | `true`                            | Optimize R class generation   |

---

### gradlew / gradlew.bat

**Purpose**: Gradle Wrapper scripts that ensure consistent Gradle version across all environments.

- `gradlew` - Unix/macOS/Linux shell script
- `gradlew.bat` - Windows batch script

---

### gradle/wrapper/gradle-wrapper.properties

**Purpose**: Specifies the exact Gradle distribution to use.

---

### local.properties

**Purpose**: Local machine configuration (SDK path). Not committed to version control.

---

## 📦 Application Module (app/)

### app/build.gradle.kts

**Location**: `app/build.gradle.kts`

**Purpose**: Application-specific build configuration.

**Key Configurations**:

```kotlin
android {
    namespace = "com.example.quickinv"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.quickinv"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}
```

**Dependencies**:

| Dependency                                   | Version  | Purpose                           |
| -------------------------------------------- | -------- | --------------------------------- |
| `androidx.appcompat:appcompat`               | 1.6.1    | Backward-compatible UI components |
| `com.google.android.material:material`       | 1.11.0   | Material Design components        |
| `androidx.constraintlayout:constraintlayout` | 2.1.4    | Flexible layouts                  |
| `androidx.recyclerview:recyclerview`         | 1.3.0    | Efficient list display            |
| `org.json:json`                              | 20231013 | JSON parsing for API              |
| `junit:junit`                                | 4.13.2   | Unit testing                      |
| `androidx.test.espresso:espresso-core`       | 3.5.1    | UI testing                        |

---

### app/proguard-rules.pro

**Purpose**: ProGuard/R8 rules for code shrinking and obfuscation in release builds.

---

### AndroidManifest.xml

**Location**: `app/src/main/AndroidManifest.xml`

**Purpose**: Declares app components, permissions, and metadata.

**Permissions**:

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

Required for the currency conversion API calls.

**Declared Activities**:

| Activity              | Purpose                             | Launcher |
| --------------------- | ----------------------------------- | -------- |
| `SplashActivity`      | App entry point with logo animation | ✅ Yes   |
| `LoginActivity`       | User login screen                   | No       |
| `RegisterActivity`    | New user registration               | No       |
| `InventoryActivity`   | Main inventory list                 | No       |
| `AddEditItemActivity` | Add/Edit inventory items            | No       |

---

## ☕ Java Source Code

### Package: `com.example.quickinv`

---

### MainActivity.java

**Location**: `app/src/main/java/com/example/quickinv/MainActivity.java`

**Purpose**: Default activity (unused in current flow).

**Note**: The app uses `SplashActivity` as the launcher instead.

---

### Package: `activities/`

#### SplashActivity.java

**Purpose**: Entry point with animated splash screen.

**Key Features**:

- Displays app logo and name with fade-in animation (1 second)
- 3-second delay before navigation
- Checks session status via `SessionManager`
- Routes to:
  - `InventoryActivity` if logged in
  - `LoginActivity` if not logged in

**Flow**:

```
App Launch → SplashActivity (3s) → Session Check → Login/Inventory
```

---

#### LoginActivity.java

**Purpose**: User authentication screen.

**Key Features**:

- Username and password input fields
- Input validation (empty fields, minimum 4 character password)
- Database authentication via `UserDAO.loginUser()`
- Session creation via `SessionManager.createLoginSession()`
- Navigation to `RegisterActivity` for new users
- Toast notifications for success/failure

**Validation Rules**:

- Username: Required
- Password: Required, minimum 4 characters

---

#### RegisterActivity.java

**Purpose**: New user account creation.

**Key Features**:

- Username, email, password, confirm password fields
- Comprehensive input validation
- Duplicate username check via `UserDAO.isUsernameExists()`
- User registration via `UserDAO.registerUser()`

**Validation Rules**:

- Username: Required, minimum 3 characters, unique
- Email: Required, must contain "@"
- Password: Required, minimum 4 characters
- Confirm Password: Must match password

---

#### InventoryActivity.java

**Purpose**: Main inventory management screen.

**Key Features**:

- RecyclerView with `ItemAdapter` for item display
- Real-time search via SearchView in action bar
- Add new item button → `AddEditItemActivity`
- Currency converter dialog
- Logout functionality
- Empty state display

**Implements**: `ItemAdapter.OnItemClickListener`

**Interface Methods**:

- `onItemEdit(Item item)` - Opens edit mode in `AddEditItemActivity`
- `onItemDelete(Item item)` - Deletes item via `ItemDAO`

**Menu Options**:

- Search (action bar)
- Logout (overflow menu)

---

#### AddEditItemActivity.java

**Purpose**: Add new items or edit existing items.

**Key Features**:

- Dual-mode: Add new or Edit existing
- Fields: Name, Quantity, Price, Description
- Input validation with error messages
- Insert via `ItemDAO.addItem()` or update via `ItemDAO.updateItem()`

**Intent Extras for Edit Mode**:

- `edit_mode` (boolean): True for edit mode
- `item_id` (int): ID of item to edit

---

### Package: `adapters/`

#### ItemAdapter.java

**Purpose**: RecyclerView adapter for inventory items.

**Key Components**:

**ViewHolder Pattern**:

```java
public static class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView itemNameTextView;
    TextView quantityTextView;
    TextView priceTextView;
    TextView descriptionTextView;
    Button editButton;
    Button deleteButton;
}
```

**OnItemClickListener Interface**:

```java
public interface OnItemClickListener {
    void onItemEdit(Item item);
    void onItemDelete(Item item);
}
```

**Display Format**:

- Item name (bold)
- Price formatted as "$X.XX"
- Quantity prefixed with "Qty:"
- Description text
- Edit and Delete buttons

---

### Package: `database/`

#### DatabaseHelper.java

**Purpose**: SQLite database creation and version management.

**Database Info**:
| Property | Value |
|----------|-------|
| Name | `quickinv.db` |
| Version | 1 |

**Tables**:

**users table**:

```sql
CREATE TABLE users(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    email TEXT NOT NULL
)
```

**items table**:

```sql
CREATE TABLE items(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    quantity INTEGER NOT NULL,
    price REAL NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY(user_id) REFERENCES users(id)
)
```

---

#### UserDAO.java

**Purpose**: Data Access Object for user operations.

**Methods**:

| Method               | Parameters         | Returns | Purpose                         |
| -------------------- | ------------------ | ------- | ------------------------------- |
| `registerUser()`     | User               | long    | Insert new user, returns row ID |
| `loginUser()`        | username, password | User    | Authenticate and return User    |
| `isUsernameExists()` | username           | boolean | Check username availability     |
| `getUserById()`      | userId             | User    | Retrieve user by ID             |

---

#### ItemDAO.java

**Purpose**: Data Access Object for inventory item operations.

**Methods**:

| Method                | Parameters    | Returns    | Purpose                |
| --------------------- | ------------- | ---------- | ---------------------- |
| `addItem()`           | Item          | long       | Insert new item        |
| `updateItem()`        | Item          | int        | Update existing item   |
| `deleteItem()`        | itemId        | int        | Delete item by ID      |
| `getAllItemsByUser()` | userId        | List<Item> | Get all items for user |
| `searchItems()`       | userId, query | List<Item> | Search items by name   |
| `getItemById()`       | itemId        | Item       | Get single item        |

**Query Features**:

- Items ordered by `created_at DESC` (newest first)
- Search uses SQL `LIKE` with wildcards

---

### Package: `models/`

#### User.java

**Purpose**: Data model for user accounts.

**Fields**:
| Field | Type | Purpose |
|-------|------|---------|
| `id` | int | Primary key |
| `username` | String | Unique username |
| `password` | String | User password |
| `email` | String | User email |

**Constructors**:

1. Default (empty)
2. Registration: `User(username, password, email)`
3. Full: `User(id, username, password, email)`

---

#### Item.java

**Purpose**: Data model for inventory items.

**Fields**:
| Field | Type | Purpose |
|-------|------|---------|
| `id` | int | Primary key |
| `userId` | int | Foreign key to users |
| `name` | String | Item name |
| `quantity` | int | Item quantity |
| `price` | double | Item price |
| `description` | String | Optional description |
| `createdAt` | String | Timestamp |

**Constructors**:

1. Default (empty)
2. New item: `Item(userId, name, quantity, price, description)`
3. Full: `Item(id, userId, name, quantity, price, description, createdAt)`

---

### Package: `utils/`

#### SessionManager.java

**Purpose**: Manages user login session using SharedPreferences.

**SharedPreferences Keys**:
| Key | Type | Purpose |
|-----|------|---------|
| `is_login` | boolean | Login status |
| `user_id` | int | Current user ID |
| `username` | String | Current username |

**Methods**:
| Method | Purpose |
|--------|---------|
| `createLoginSession(userId, username)` | Store login state |
| `logout()` | Clear session data |
| `isLoggedIn()` | Check login status |
| `getUserId()` | Get current user ID |
| `getUsername()` | Get current username |

---

#### CurrencyConverter.java

**Purpose**: Real-time currency conversion using ExchangeRate-API.

**API Configuration**:

```java
API_KEY = "cebd5e755274a937d7d5926c"
API_URL = "https://v6.exchangerate-api.com/v6/"
```

**Callback Interface**:

```java
public interface OnConversionListener {
    void onSuccess(String result);
    void onError(String error);
}
```

**Method**:

```java
public void convertCurrency(String baseCurrency, String targetCurrency, double amount)
```

**Features**:

- Runs API call on background thread
- 5-second connection/read timeout
- Parses JSON response
- Returns formatted result: "X.XX BASE = Y.YY TARGET (Rate: Z.ZZZZ)"
- Error handling with descriptive messages

---

## 🎨 Resource Files

### res/layout/

| File                            | Purpose                               |
| ------------------------------- | ------------------------------------- |
| `activity_splash.xml`           | Splash screen with logo and app name  |
| `activity_login.xml`            | Login form layout                     |
| `activity_register.xml`         | Registration form layout              |
| `activity_inventory.xml`        | Main inventory list with RecyclerView |
| `activity_add_edit_item.xml`    | Item form (add/edit)                  |
| `activity_main.xml`             | Default main activity layout          |
| `item_list_row.xml`             | Individual inventory item card        |
| `dialog_currency_converter.xml` | Currency converter dialog             |

---

### res/menu/

#### menu_inventory.xml

**Purpose**: Action bar menu for InventoryActivity.

**Items**:

- `action_search` - SearchView for filtering items
- `action_logout` - Logout option (overflow menu)

---

### res/values/

#### strings.xml

**Purpose**: All user-facing text strings for localization.

**Categories**:

- App info (name, tagline)
- Login/Register screens
- Inventory screen
- Item details
- Buttons
- Currency converter

---

#### colors.xml

**Purpose**: App color palette.

| Color Name         | Hex     | Usage                   |
| ------------------ | ------- | ----------------------- |
| `primary_color`    | #1ABC9C | Main brand color (teal) |
| `primary_dark`     | #16A085 | Status bar color        |
| `accent_color`     | #E74C3C | Accent/highlight (red)  |
| `background_color` | #F5F5F5 | App background          |
| `text_primary`     | #2C3E50 | Main text color         |
| `text_secondary`   | #7F8C8D | Secondary text          |
| `error_color`      | #E74C3C | Error messages          |
| `success_color`    | #27AE60 | Success messages        |

---

#### styles.xml

**Purpose**: App theme and style definitions.

**Themes**:

- `AppTheme` - Main app theme with custom colors
- `AppTheme.NoActionBar` - Theme without action bar

**Styles**:

- `ButtonStyle` - White bold text buttons

---

#### themes.xml

**Purpose**: Material theme overrides.

---

#### dimens.xml

**Purpose**: Dimension values for consistent spacing.

---

### res/drawable/

| File                         | Purpose                  |
| ---------------------------- | ------------------------ |
| `ic_inventory.png`           | App inventory icon       |
| `ic_launcher_background.xml` | Launcher icon background |
| `ic_launcher_foreground.xml` | Launcher icon foreground |

---

### res/mipmap-\*/

**Purpose**: App launcher icons in different densities (hdpi, mdpi, xhdpi, xxhdpi, xxxhdpi).

---

### res/values-night/

**Purpose**: Dark theme color overrides (if implemented).

---

### res/xml/

**Purpose**: XML configurations (backup rules, etc.).

---

## 🗄️ Database Design

### Entity Relationship Diagram

```
┌─────────────────┐         ┌─────────────────────────┐
│     USERS       │         │         ITEMS           │
├─────────────────┤         ├─────────────────────────┤
│ id (PK)         │────┐    │ id (PK)                 │
│ username (UQ)   │    │    │ user_id (FK) ───────────┘
│ password        │    └───→│ name                    │
│ email           │         │ quantity                │
└─────────────────┘         │ price                   │
                            │ description             │
                            │ created_at              │
                            └─────────────────────────┘
```

### Relationships

- **One-to-Many**: One User can have many Items
- **Foreign Key**: `items.user_id` references `users.id`

---

## 🔄 Application Flow

### User Registration Flow

```
RegisterActivity
    │
    ├── Validate Input
    │   ├── Username (3+ chars, unique)
    │   ├── Email (contains @)
    │   └── Password (4+ chars, matches confirm)
    │
    ├── UserDAO.isUsernameExists()
    │
    ├── UserDAO.registerUser()
    │
    └── Navigate to LoginActivity
```

### Login Flow

```
LoginActivity
    │
    ├── Validate Input
    │
    ├── UserDAO.loginUser()
    │
    ├── SessionManager.createLoginSession()
    │
    └── Navigate to InventoryActivity
```

### Inventory Management Flow

```
InventoryActivity
    │
    ├── Load Items (ItemDAO.getAllItemsByUser)
    │
    ├── Add Item → AddEditItemActivity → ItemDAO.addItem()
    │
    ├── Edit Item → AddEditItemActivity → ItemDAO.updateItem()
    │
    ├── Delete Item → ItemDAO.deleteItem()
    │
    ├── Search → ItemDAO.searchItems()
    │
    └── Logout → SessionManager.logout() → LoginActivity
```

---

## 🌐 API Integration

### ExchangeRate-API

**Base URL**: `https://v6.exchangerate-api.com/v6/`

**Endpoint Used**: `/pair/{base}/{target}/{amount}`

**Example Request**:

```
GET https://v6.exchangerate-api.com/v6/cebd5e755274a937d7d5926c/pair/OMR/USD/100
```

**Response Format**:

```json
{
  "result": "success",
  "conversion_rate": 2.6,
  "conversion_result": 260.0
}
```

**Error Handling**:

- Connection timeout (5 seconds)
- HTTP response code validation
- JSON parsing errors
- API error messages

---

## 📝 Testing

### Test Directories

- `app/src/test/` - Unit tests (JUnit)
- `app/src/androidTest/` - Instrumented tests (Espresso)

### Test Configuration

```kotlin
testImplementation("junit:junit:4.13.2")
androidTestImplementation("androidx.test.ext:junit:1.1.5")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
```

---

## 🚀 Build & Run

### Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Clean build
./gradlew clean

# Run tests
./gradlew test
```

### Output Locations

- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`

---

## 📄 Summary

QuickInv is a well-structured Android inventory management application that demonstrates:

✅ **Clean Architecture** - Separation of concerns with Activities, Adapters, DAOs, Models, and Utils

✅ **SQLite Database** - Local data persistence with proper schema design

✅ **Session Management** - SharedPreferences-based authentication state

✅ **API Integration** - Real-time currency conversion with proper error handling

✅ **Material Design** - Modern UI components and consistent theming

✅ **RecyclerView Pattern** - Efficient list display with ViewHolder pattern

✅ **Input Validation** - Comprehensive form validation with user feedback

---

_Documentation generated for QuickInv v1.0_
