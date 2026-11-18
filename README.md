# BookWorm

BookWorm is an Android app for readers. It helps people keep a list of books and see their reading progress.

This is a group project for CSS 216.

## Features

The app will help you:

- See books you are reading, books you finished, and books you want to read.
- See how many pages you have read.
- Change the status of a book.
- Add and delete quotes from a book.
- Add and delete books.

## Tools

- Kotlin
- Jetpack Compose and Material 3
- Navigation Compose
- MVVM and StateFlow
- Android Studio

## How to run

1. Install Android Studio, Android SDK 37, and JDK 25.
2. Clone this repository:

   ```bash
   git clone https://github.com/parasattt/1004bala.git
   ```

3. To open the development version, run:

   ```bash
   cd 1004bala
   ```

4. Open the `1004bala` folder in Android Studio.
5. Wait for Gradle sync to finish.
6. Choose an Android phone or emulator with Android 7.0 (API 24) or newer.
7. Click **Run**.

## Team

- **Omirzak Bakdaulet** (Dev 1) - Data and navigation
- **Dauletbai Parassat** (Dev 2) - Library screen
- **Muratov Nurbol** (Dev 3) - Book details screen
- **Kikhmetov Karimkhan** (Dev 4) - Add book screen and app theme

## Course requirements (Week 12)

These are the course requirements. We are still working on the app, so some parts are not ready yet.

The team must choose one app idea and work on the same app in Weeks 12-14.

### 1. Project setup and GitHub

- Make an Android app with Kotlin and Jetpack Compose.
- Create a GitHub repository and add all team members as collaborators.
- Share the repository link with the teacher before the project defense.
- Each student must make at least 10 meaningful commits. The commits must show real work. Empty or unnecessary commits do not count.
- Add a README with the project name, description, team members, and main features.

### 2. App structure and state

- Use MVVM (Model-View-ViewModel).
- Keep the screen parts, screen logic, and data in separate parts of the project.
- Use ViewModel for screen data and logic.
- Use StateFlow or MutableState for the screen state.
- Update the screen when the data changes.

### 3. Screens and design

- Make all needed screens work and connect them with Navigation Compose.
- Use Jetpack Compose for all parts of the screens.
- Make buttons, text fields, and other controls work.
- Use reusable Composable functions to avoid repeating code.
- Use the same style across the app and follow Material Design.

### 4. Navigation

- Use Navigation Compose to move between screens.
- Let users go back to the last screen.
- Pass at least one value between screens, such as a book ID.