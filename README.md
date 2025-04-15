# SmartLogin
SmartLogin is an Android application that simulates a secure login process where access is only granted if five conditions are met, all based on real time data from the device.

## Project Description
The login screen contains five "locks" that represent different conditions.  
Each lock requires the user to enter input that matches the device's current state.  
Only after all locks are verified, the login button becomes active.

## Conditions Required to Unlock Access
1. **Battery Percentage** – the user must input the exact current battery percentage.
2. **Charging Status** – the user must input whether the device is charging (yes/no).
3. **WiFi Connection** – the user must input whether the device is connected to WiFi (yes/no).
4. **Current Hour** – the user must input the current hour in 24-hour format.
5. **Location** – the device must be located within few kilometers of Tel Aviv, and the user must type "tel aviv".

## Technologies Used
- Java (Android)
- Android Studio
- FusedLocationProviderClient for location access
- BatteryManager
- ConnectivityManager
- Runtime permission handling for location access
