# Testing Guide - Quick Contact & API Feature

## Overview
This guide covers testing the main functionality implemented in MainActivity.java, including the "Call Tutor" button, "Find Library" button, and API integration for fetching current time data.

## Prerequisites
- Android device or emulator with API level 24+ (Android 7.0)
- Internet connection for API calls
- Phone dialer app installed (pre-installed on most devices)
- Google Maps app installed (for library search functionality)

## Test Cases

### 1. Call Tutor Button Test
**Purpose**: Verify the phone dialer opens with the correct pre-set number.

**Steps**:
1. Launch the Study Log app
2. On the main screen, locate the "Call Tutor" button at the top
3. Tap the "Call Tutor" button

**Expected Result**:
- Phone dialer app should open automatically
- The number field should be pre-filled with "+1234567890"
- User can then tap the dial button in the phone app to make the call

**Pass/Fail Criteria**:
- ✅ Pass: Dialer opens with correct number pre-filled
- ❌ Fail: Dialer doesn't open, wrong number, or app crashes

### 2. Find Library Button Test
**Purpose**: Verify Google Maps opens and searches for nearby libraries.

**Steps**:
1. Ensure location services are enabled on the device
2. On the main screen, locate the "Find Library" button
3. Tap the "Find Library" button

**Expected Result**:
- Google Maps app should launch automatically
- Maps should show a search for "library near me"
- Nearby libraries should be displayed as pins/markers on the map

**Pass/Fail Criteria**:
- ✅ Pass: Google Maps opens and shows library search results
- ❌ Fail: Maps doesn't open, no search performed, or wrong search query

### 3. API Integration Test
**Purpose**: Verify the WorldTime API integration fetches and displays current London time.

**Steps**:
1. Launch the app with an active internet connection
2. Observe the TextView below the buttons (initially shows "Loading current time...")
3. Wait 2-3 seconds for the API call to complete

**Expected Result**:
- Initial text: "Loading current time..."
- After API response: "Current Date/Time: [ISO 8601 timestamp]"
- Example: "Current Date/Time: 2024-11-11T14:30:25.123456+00:00"

**Pass/Fail Criteria**:
- ✅ Pass: Shows loading text, then displays current London time
- ❌ Fail: Shows error message, doesn't update, or crashes

### 4. Error Handling Tests

#### 4.1 No Internet Connection Test
**Steps**:
1. Disable internet connection on device
2. Launch the app
3. Observe the API result TextView

**Expected Result**: 
- Should display "Error fetching data"

#### 4.2 Google Maps Not Installed Test
**Steps**:
1. Temporarily uninstall or disable Google Maps
2. Tap "Find Library" button

**Expected Result**:
- Button tap should have no effect (safe failure)
- App should not crash

## Bottom Navigation Test
**Purpose**: Ensure the fragment navigation system works correctly.

**Steps**:
1. Tap each tab in the bottom navigation: Facts, Tasks, Contact, Profile
2. Verify each fragment loads without errors
3. Return to Facts tab to see the main buttons again

**Expected Result**:
- Each tab should load its respective fragment
- No crashes or blank screens
- Main buttons are visible on the Facts tab

## Performance Verification
- App should launch within 3-5 seconds
- Button responses should be immediate (< 100ms)
- API calls should complete within 5 seconds on good network
- Memory usage should remain stable during testing

## Device Compatibility Notes
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 36)
- **Required Permissions**: Internet access (automatically granted)
- **Optional**: Phone and location permissions for full functionality

## Troubleshooting

### Common Issues:
1. **API not loading**: Check internet connection and firewall settings
2. **Dialer not opening**: Verify device has a phone dialer app
3. **Maps not working**: Ensure Google Maps is installed and updated
4. **App crashes**: Check device compatibility and available memory

### Debug Information:
- API Endpoint: `https://worldtimeapi.org/api/timezone/Europe/London`
- Phone Number: `+1234567890` (configurable in strings.xml)
- Maps Query: `geo:0,0?q=library+near+me`

## Code Quality Improvements Made
- Refactored MainActivity into smaller, readable methods
- Replaced hardcoded strings with resource references
- Added proper dimension resources for consistent spacing
- Implemented proper error handling for network requests
- Used method references and lambda expressions for cleaner code

## Build Verification
The project successfully builds with `./gradlew assembleDebug` and is ready for installation on test devices.
