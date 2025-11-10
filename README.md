# Study Log

This repository contains the Android Java app "Study Log" (package: `com.example.studylog`).

This repo is prepared for team collaboration. The Fact Feed feature is implemented on branch `feature/fact-feed`. Use the guidance below to contribute safely.

## Quick start (build & run)
1. Open the project in Android Studio.
2. If prompted, use the bundled JDK or set `JAVA_HOME` to a JDK 11/17.
3. Click "Sync Project with Gradle Files" to download dependencies (Volley, Picasso, etc.).
4. Build -> Make Project. Run on an emulator or device.

Command-line (Windows cmd.exe):

```bat
cd /d "C:\Users\user\AndroidStudioProjects\MyApp"
gradlew.bat assembleDebug
gradlew.bat installDebug   # installs on connected device/emulator
```

## What is implemented
- MainActivity with BottomNavigationView and 4 fragments (Facts, Task Log, Contact, Profile)
- FactFeedFragment (Volley + RecyclerView + Picasso placeholder images)
- Adapter, model, layouts for the Fact feed

## Branching & collaboration workflow (recommended)
1. Create a remote GitHub repository and add it as `origin`.
2. Each team member creates their own feature branch from `main` or `develop` (choose a primary branch):
   - `feature/fact-feed` (already used for facts)
   - `feature/task-log` (Member 2)
   - `feature/contact` (Member 3)
   - `feature/profile` (Member 4)

3. Work locally, commit often, and push branches to remote:

```bat
git checkout -b feature/task-log
# work, add, commit
git push -u origin feature/task-log
```

4. Create a Pull Request (PR) on GitHub when ready. Use the PR template that exists in `.github/PULL_REQUEST_TEMPLATE.md`.
5. Request at least one review. Merge after approval and CI checks.

## How to create a GitHub repo and push (exact commands)
1. Create a new empty repository on GitHub (via UI). Copy the repository HTTPS URL (e.g. `https://github.com/<org-or-user>/MyApp.git`).
2. Run these commands locally:

```bat
cd /d "C:\Users\user\AndroidStudioProjects\MyApp"
# if you haven't already initialized git
# git init
# make sure your changes are committed
git remote add origin https://github.com/<your-user-or-org>/MyApp.git
git branch -M main
git push -u origin main
# push your feature branch
git push -u origin feature/fact-feed
```

## Adding teammates & permissions
- On GitHub: Repository -> Settings -> Manage access -> Invite collaborators (add GitHub usernames/emails).
- Optionally: Create a team in an organization and add the repo to that team.

## Code review & PR checklist (short)
- Small, focused PR (preferably < 300 LOC)
- Compile with `Build -> Make Project` (no errors)
- Include a short description and testing steps in the PR
- Add reviewers and address feedback before merging

## Testing notes
- FactFeed requires network access. If using an emulator, ensure the emulator can access the internet.
- If you see a Firebase warning at startup: that's expected unless `google-services.json` is added.

## Files of interest for contributors
- `app/src/main/java/com/example/studylog/MainActivity.java` - app navigation
- `app/src/main/java/com/example/studylog/FactFeedFragment.java` - facts implementation
- `app/src/main/java/com/example/studylog/FactAdapter.java` - RecyclerView adapter
- `app/src/main/res/layout/fragment_fact_feed.xml` - fact fragment layout
- `app/src/main/res/layout/item_fact.xml` - fact item layout

---

If you'd like, I can also:
- Create a `CONTRIBUTING.md` and a `.github/PULL_REQUEST_TEMPLATE.md` (I will add them now),
- Create a simple GitHub Action workflow to run `./gradlew assembleDebug` on each PR (CI),
- Remove the old neutralized `com.example.myapp` files and commit the cleanup.

Tell me which of those you'd like me to add (I will add `CONTRIBUTING.md` and PR template now).
