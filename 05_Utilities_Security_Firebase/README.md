# 🛡️ 05 — Utilities, Security & Firebase Configuration (`com.expensetracker.util`)

## 🎯 Purpose of This Layer
This folder contains shared cross-cutting utility classes for **Firebase Admin SDK initialization**, **Session state management**, **BCrypt password security**, and **server-side data validation & XSS sanitization**.

---

## 📂 Files in This Folder

### 1. `FirebaseConfig.java`
* **Purpose**: Thread-safe Singleton for Firebase Admin SDK and Cloud Firestore initialization.
* **Credential Discovery Order**:
  1. Environment variable `FIREBASE_CONFIG_PATH`
  2. System property `firebase.config.path`
  3. Classpath resource `/firebase-service-account.json`
  4. Local project root `firebase-service-account.json`
* **Features**:
  * Initializes `FirebaseApp` only once with `GoogleCredentials.fromStream()`.
  * Provides `getFirestore()` returning the active Firestore database instance.
  * Fails gracefully with detailed setup warnings if credentials are not yet supplied.

---

### 2. `SessionUtil.java`
* **Purpose**: Helper for managing HTTP Session attributes and user authentication state.
* **Key Methods**:
  * `setLoggedInUser(request, user)`: Stores `User` object and `userId` in `HttpSession`.
  * `getLoggedInUser(request)`: Retrieves the currently logged-in `User` entity.
  * `getLoggedInUserId(request)`: Extracts the active user's unique ID.
  * `isLoggedIn(request)`: Checks whether an active authenticated session exists.
  * `logout(request)`: Invalidates the `HttpSession`.
  * `setFlashMessage(request, type, message)`: Stores temporary alert notifications (`FLASH_SUCCESS`, `FLASH_ERROR`) that disappear after rendering.

---

### 3. `PasswordUtil.java`
* **Purpose**: High-security password hashing and validation.
* **Key Methods**:
  * `hashPassword(plainPassword)`: Generates a salted **BCrypt hash** (cost factor 12).
  * `verifyPassword(plainPassword, hashedPassword)`: Uses `BCrypt.checkpw()` to verify passwords in constant time (protecting against timing attacks).
  * Includes resilient fallback to SHA-256 with salt if BCrypt native binaries are unavailable in minimal environments.

---

### 4. `ValidationUtil.java`
* **Purpose**: Server-side validation and XSS prevention.
* **Key Methods**:
  * `isValidEmail(email)`: Validates email format using regex pattern `^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,63}$`.
  * `isValidPassword(password)`: Checks password length $\ge 6$.
  * `isValidAmount(amountStr)`: Checks for positive numeric values $> 0$.
  * `isValidDate(dateStr)`: Enforces valid `YYYY-MM-DD` ISO dates.
  * `isValidMonth(month)`: Ensures integer between 1 and 12.
  * `sanitize(input)`: Escapes `<`, `>`, `&`, `"`, `'`, `/` into safe HTML entities to prevent **Cross-Site Scripting (XSS)** attacks.

---

## 🎤 College Viva / Presentation Speaking Points
> **Q: Why is server-side validation mandatory even when HTML5 client-side validation is present?**  
> *Answer*: Client-side JavaScript validation can be easily bypassed by disabling JavaScript or sending direct HTTP POST requests via tools like Postman or cURL. Validating on the server ensures data integrity and security at all times.
