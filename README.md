Study Log - feature/fact-feed

What I did (branch: feature/fact-feed)

- Initialized a git repo and created branch `feature/fact-feed`.
- Added the Fact Feed first feature (Member 1):
  - `FactFeedActivity.java` (activity that fetches facts and displays them in a RecyclerView)
  - `FactAdapter.java`, `Fact.java` (model + adapter)
  - Layouts: `activity_fact_feed.xml`, `item_fact.xml`
  - Wired `MainActivity` button (`btnFacts`) to open the Fact Feed.
  - Registered `FactFeedActivity` in `AndroidManifest.xml`.
- Updated `app/build.gradle.kts` to add dependencies for Volley (network), Picasso (image loading), and RecyclerView.

Important: Gradle sync/build required

- The project files are added, but the IDE/compiler will show unresolved references for Volley and Picasso until Gradle downloads the dependencies.
- To resolve these, open the project in Android Studio and click "Sync Project with Gradle Files" (or use the menu Build > Make Project). Android Studio will download the new libraries and the errors will disappear.

Quick commands (Windows cmd.exe)

- Build (will download dependencies if internet is available):

  gradlew.bat assembleDebug

- If you want to push the branch to a remote GitHub repo (after creating a remote and adding it):

  git remote add origin <your-repo-url>
  git push -u origin feature/fact-feed

How the Fact Feed works (contract)

- Input: FactFeedActivity makes a GET request to https://api.sampleapis.com/futurama/quotes which returns a JSON array of objects like:
  { "quote": "Some quote text", "character": "...", ... }
- Output: RecyclerView list where each item shows the `quote` text and a placeholder image loaded from https://via.placeholder.com/150.
- Error modes: On network failure a Toast is shown: "Failed to fetch facts".

Files changed / created

- app/build.gradle.kts (deps added)
- app/src/main/java/com/example/myapp/FactFeedActivity.java
- app/src/main/java/com/example/myapp/FactAdapter.java
- app/src/main/java/com/example/myapp/Fact.java
- app/src/main/java/com/example/myapp/MainActivity.java (wired button)
- app/src/main/res/layout/activity_fact_feed.xml
- app/src/main/res/layout/item_fact.xml
- app/src/main/res/layout/activity_main.xml (button added)
- app/src/main/AndroidManifest.xml (activity registered)

Testing checklist

- In Android Studio: Sync Gradle -> Build -> Run app on device/emulator.
- From the app main screen, tap "Open Fact Feed" to open the new activity.
- The list should populate with quotes; if the network is unavailable you should see a Toast.

Next steps / Handoff notes for other members

- Member 2 (Task Log write): Add a new Activity `TaskWriteActivity` with a layout containing an EditText and Save button. Use SQLite/Firebase to write a task. Path suggestions: `app/src/main/java/com/example/myapp/TaskWriteActivity.java`.
- Member 3 (Quick Contact & API 2): Add two buttons on main or a dedicated `ContactActivity` that send implicit intents (ACTION_DIAL, geo query) and a Volley StringRequest for API 2.
- Member 4 (Profile): Create `ProfileActivity` using ActivityResultLauncher to capture a photo and display it; read user name from DB.

If you want, I can now:
- Help sync Gradle remotely (if you give me permission to run gradlew in this environment and network is available), or
- Walk you step-by-step through testing in Android Studio and pushing to GitHub, or
- Implement one of the other features.

Contact

If anything fails during Gradle sync or build, copy/paste the exact Gradle error or sync log and I'll debug further.
