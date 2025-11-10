# Contributing to Simple Study Log

Thanks for helping — this project is structured so each team member works on one feature. Please follow these simple steps to contribute safely.

1. Set up locally
   - Clone the repo and open in Android Studio.
   - Use the bundled JDK or set JAVA_HOME to JDK 11/17.
   - Sync Gradle and build the project.

2. Branching
   - Create a new branch for your feature: `feature/<your-name>-<short-feature>`.
   - Keep changes focused and small.

3. Coding standards
   - Java code: prefer clear, readable code with small methods.
   - XML: use resource strings and dimensions. Avoid hard-coded sizes.

4. Database & secrets
   - Do not commit `google-services.json` or any credentials. If you need Firebase, share the `google-services.json` privately and ask the repo owner to add it.

5. Testing
   - Ensure the app builds and the feature is testable on an emulator/device.
   - Add a short testing guide in your PR description.

6. Pull Requests
   - Push your branch and create a PR against `main`.
   - Use the PR template and include testing steps and screenshots if UI changes.

7. Reviews & merging
   - Request at least one review.
   - Fix review comments and push updates to the same branch.
   - After approval, merge using GitHub UI.

8. Communication
   - Keep the team informed of major changes (especially package names or manifest changes).

Thanks — if you want, I can add a GitHub Action CI workflow to automatically build each PR (recommended).
