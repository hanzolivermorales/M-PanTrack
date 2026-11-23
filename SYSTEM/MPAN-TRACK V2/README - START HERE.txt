MPAN-TRACK V2 — ANDROID STUDIO PROJECT
======================================

PROJECT FOLDER
--------------
MPAN-TRACK V2

OPENING THE PROJECT
-------------------
1. Extract the ZIP file completely.
2. Open Android Studio.
3. Choose File > Open.
4. Select the extracted folder named "MPAN-TRACK V2" (the folder containing settings.gradle.kts).
5. Wait for Gradle sync to finish. Use JDK 17 for Android Gradle Plugin 8.2.0.
6. Select an Android emulator (API 24+) or connect an Android phone with USB debugging enabled.
7. Press Run ▶ and select the app configuration.

DEMO SIGN-IN ACCOUNTS
---------------------
Administrator: admin@mpantrack.com  / admin123
Vendor:        vendor@mpantrack.com / vendor123
Customer:      customer@mpantrack.com / customer123

The demo accounts are for testing the screens and role-specific workflows. The current starter build runs in local demo mode and keeps its sample data in the app session. It does not pretend that a Firebase project has been connected.

WHAT IS INCLUDED
----------------
- Kotlin 1.9.20, Android Gradle Plugin 8.2.0, Gradle 8.4
- Jetpack Compose + Material 3, minSdk 24, targetSdk 34
- Role-specific Administrator, Vendor, and Customer dashboards
- Customer search for available products and vendors
- Selling-location screen with selectable location pins and vendor/product/schedule details
- Add/edit/delete UI flows for vendor, product, location, schedule, and user records (admin)
- Vendor product availability toggle and price validation (> PHP 0)
- Location coordinate validation (latitude -90..90, longitude -180..180)
- Schedule validation (end time later than start time)
- Activity history list and customer registration
- Green theme based on the documentation (#1E3F20, #122814, #3A6B3D, #F5F8F5)

IMPORTANT LIMITATIONS / NEXT SETUP
----------------------------------
1. Firebase Authentication and Cloud Firestore dependencies are included, but this package does not contain your private google-services.json and does not connect to a live Firebase project yet. To enable a real shared cloud backend, open Firebase Console, select/create project ID m-pan-track, add an Android app with package name com.mpan.track, download google-services.json, and place it in app/.
2. Enable Email/Password in Firebase Authentication and create the Firestore database. Create users documents with fields uid, fullName, username, email, role (ADMIN/VENDOR/CUSTOMER), isActive, and vendorId where applicable. Passwords must remain in Firebase Authentication, never in Firestore.
3. Apply the Firestore security rules in firestore.rules after adapting them to the final account bootstrap process. Do not publish permissive test rules.
4. The map in this starter is a schematic location-pin view using the coordinates stored in the sample locations; it is not a live Google Maps tile map or moving GPS tracker. The project documentation explicitly excludes live vendor GPS tracking.
5. Data is seeded in memory for the demo build; it is not yet synchronized between devices. Firebase setup and repository wiring are required for the shared cloud behavior described in the OOP document.

DESIGN SOURCE
-------------
The screen flow and UI styling follow the uploaded OOP Project Documentation: role-based navigation (page 23), the login/admin dashboard/map/vendor product form wireframe (page 24), and the specified hunter-green palette and Material 3 design (page 22).
