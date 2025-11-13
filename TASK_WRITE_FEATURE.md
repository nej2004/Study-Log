Study Log - feature/task-write

What I did (branch: feature/task-write)

- Created feature/task-write branch for the Task Log write functionality
- Implemented the mandatory Database Write functionality:
  - `Task.java` - Model class to represent a task
  - `TaskDatabaseHelper.java` - SQLite database helper for CRUD operations
  - Updated `TaskLogFragment.java` - Fragment with EditText input and Save button
  - Updated `fragment_task_log.xml` - UI layout with EditText and Save button
  
Feature Implementation Details

TaskLogFragment:
- Displays a simple UI with a heading "Add New Task"
- Contains an EditText field (id: etTaskName) with placeholder text "Enter task name (e.g., Review WS10)"
- Has a "Save Task" button (id: btnSaveTask)
- When button is clicked:
  1. Validates that the task name is not empty
  2. Saves the task to SQLite database using TaskDatabaseHelper
  3. Shows "Task Saved!" Toast message on success
  4. Clears the EditText for next input
  5. Shows error message if save fails

TaskDatabaseHelper:
- Creates and manages SQLite database named "studylog.db"
- Creates "tasks" table with columns:
  - _id (INTEGER PRIMARY KEY AUTOINCREMENT)
  - task_name (TEXT NOT NULL)
  - created_at (LONG NOT NULL - timestamp)
- Provides insertTask(String taskName) method to save tasks

Task Model:
- Simple POJO class with getters/setters for:
  - id
  - taskName
  - createdAt (timestamp)

Files Changed/Created:
- app/src/main/java/com/example/studylog/Task.java (NEW)
- app/src/main/java/com/example/studylog/TaskDatabaseHelper.java (NEW)
- app/src/main/java/com/example/studylog/TaskLogFragment.java (UPDATED)
- app/src/main/res/layout/fragment_task_log.xml (UPDATED)

Local Commits:
✓ Commit created: "feat: add task write functionality with SQLite database"
✓ Branch feature/task-write created and committed
✓ Merged into main branch

GitHub Push Status:
- Attempted to push to https://github.com/DilushaGalhena/Study-Log.git
- GitHub server returned "Internal Server Error" - this is a temporary server issue
- All commits are properly created locally
- Can retry push when GitHub service is available

Testing Checklist:
1. In Android Studio: Sync Gradle and Build the project
2. Run app on device/emulator
3. Navigate to "Tasks" tab (nav_tasks) on bottom navigation
4. Enter a task name (e.g., "Review WS10")
5. Click "Save Task" button
6. Verify "Task Saved!" Toast appears
7. EditText should clear and be ready for next task

Database File Location:
- Path: /data/data/com.example.studylog/databases/studylog.db
- Can be viewed using Android Studio's Device File Explorer after running on device/emulator

Integration Notes:
- TaskLogFragment is already wired in MainActivity (referenced as nav_tasks)
- No additional manifest changes needed
- Fragment uses standard lifecycle methods
- Database is initialized on first save

Troubleshooting:
- If push still fails: Try `git push --force-with-lease origin main` after verifying remote URL
- Remote configured as: https://github.com/DilushaGalhena/Study-Log.git
- Can manually create a Pull Request on GitHub to merge feature/task-write into main

Next Steps for Other Members:
- Member 1: Fact Feed feature (already completed in previous branch)
- Member 3: Quick Contact & API 2
- Member 4: Profile with photo capture

