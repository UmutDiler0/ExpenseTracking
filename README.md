# ExpenseTracking

ExpenseTracking is a modern Android expense tracking application that allows users to manage their income and daily expenses efficiently. The application is built using up-to-date Android technologies, follows clean architecture principles, and provides a scalable and maintainable structure.


https://github.com/user-attachments/assets/7e136dac-934d-4bdc-a8ee-172f764419de


## Features

- User registration and authentication (Firebase Authentication)
- Daily expense and monthly income tracking
- Secure data storage with Firebase Firestore
- Modern and declarative UI using Jetpack Compose
- MVI architecture
- Asynchronous operations with Kotlin Coroutines
- Animated UI components using Lottie
- Local data persistence with Room and DataStore
- Seamless screen navigation with Navigation Compose

## Tech Stack & Libraries

### Android & Jetpack
- Jetpack Compose
- Material 3
- Navigation Compose
- DataStore Preferences
- Room Database

### Dependency Injection
- Hilt
- Hilt Navigation Compose

### Firebase
- Firebase Authentication
- Firebase Firestore

### Concurrency & State Management
- Kotlin Coroutines
- Coroutines Play Services

### UI & Animations
- Coil (Image Loading)
- Lottie Compose
- DotLottie Android
- Accompanist System UI Controller
- Material Icons Extended

### Other Libraries
- Gson

## Architecture

The project is developed based on the **MVVM (Model-View-ViewModel)** architecture.

- **UI Layer**: Jetpack Compose
- **State Management**: ViewModel + StateFlow
- **Data Layer**: Room, Firebase Firestore, DataStore
- **Dependency Injection**: Hilt

This architecture ensures:
- Clear separation of concerns
- Improved testability
- Better maintainability and scalability

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/UmutDiler0/ExpenseTracking.git
