# API Integration Debug Guide

## Issue: App showing "Error fetching data"

## Fixes Applied:

### 1. Enhanced Error Handling & Logging
- Added comprehensive logging with TAG "MainActivity"
- Added specific error messages for different HTTP status codes
- Added timeout and network error detection
- Added JSON parsing error handling

### 2. Network Security Configuration
- Created `network_security_config.xml` to handle HTTPS/HTTP issues
- Added `android:usesCleartextTraffic="true"` in AndroidManifest
- Allow cleartext traffic for worldtimeapi.org domain

### 3. Backup API System
- Primary: `https://worldtimeapi.org/api/timezone/Europe/London`
- Backup: `http://worldtimeapi.org/api/timezone/Europe/London` (HTTP fallback)
- Alternative: `https://timeapi.io/api/Time/current/zone?timeZone=Europe/London`

### 4. Comprehensive API Testing
- Added `testAllAPIs()` method to test all endpoints
- Click "Error fetching data" text to run full API diagnostics
- Will show which API works and detailed error messages

### 5. Improved Request Configuration
- Added proper User-Agent header
- Added Accept: application/json header
- Increased timeout to 15 seconds
- Disabled automatic retries to avoid delays

## Testing Steps:

### Method 1: Check Logs
1. Connect device/emulator to Android Studio
2. Open Logcat and filter by "MainActivity"
3. Launch app and watch for API logs
4. Look for specific error messages

### Method 2: Use Built-in Diagnostics
1. Launch the app
2. If you see "Error fetching data", tap on the text
3. App will run comprehensive API tests
4. Will show which API works or specific error messages

### Method 3: Manual API Testing
Test the APIs directly:
- Primary: `curl "https://worldtimeapi.org/api/timezone/Europe/London"`
- Backup: `curl "http://worldtimeapi.org/api/timezone/Europe/London"`
- Alternative: `curl "https://timeapi.io/api/Time/current/zone?timeZone=Europe/London"`

## Expected Behavior:
- Loading: "Loading current time..."
- Success: "Current Date/Time: 2024-11-12T14:30:25.123456+00:00"
- Error: Specific error message + "- Tap to retry"

## Common Issues & Solutions:

### 1. SSL/TLS Issues
- **Solution**: Network security config allows both HTTP and HTTPS
- **Backup**: Falls back to HTTP if HTTPS fails

### 2. API Endpoint Down
- **Solution**: Multiple backup APIs configured
- **Fallback**: TimeAPI.io as alternative service

### 3. Network Connectivity
- **Detection**: Shows "No internet connection available"
- **Solution**: Check device network settings

### 4. Firewall/Corporate Network
- **Detection**: Shows timeout or connection errors
- **Solution**: May need to test on different network

### 5. JSON Parsing Issues
- **Detection**: Shows "Error parsing date/time"
- **Logging**: Full response logged for debugging

## Debug Commands:
```bash
# Build and install
./gradlew assembleDebug

# Install on connected device
adb install app/build/outputs/apk/debug/app-debug.apk

# View real-time logs
adb logcat | grep MainActivity
```

## Files Modified:
- `MainActivity.java` - Enhanced API integration with comprehensive error handling
- `AndroidManifest.xml` - Added network security configuration
- `res/xml/network_security_config.xml` - Network security rules
- `res/values/strings.xml` - Added backup API URLs

The app should now provide detailed error messages and automatically try multiple APIs to ensure the time data loads successfully.
