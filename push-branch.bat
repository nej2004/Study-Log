@echo off
echo ========================================
echo  Quick Contact & API 2 - Branch Push
echo ========================================
echo.

echo Current branch status:
git branch --show-current
echo.

echo Checking git status...
git status --short
echo.

echo Ready to push branch: feature/dinidu-Quick Contact ^& API 2
echo.
echo IMPORTANT: Make sure you have set up your remote repository first!
echo Example: git remote add origin https://github.com/YOUR_USERNAME/Study-Log.git
echo.

set /p confirm=Push branch to remote repository? (y/n):
if /i "%confirm%"=="y" (
    echo.
    echo Pushing branch to remote...
    git push -u origin "feature/dinidu-Quick Contact & API 2"
    echo.
    echo ✅ Branch pushed successfully!
    echo.
    echo Next steps:
    echo 1. Go to your GitHub repository
    echo 2. Click "Compare & pull request"
    echo 3. Use the PR template that will auto-populate
    echo 4. Add screenshots of the UI changes
    echo 5. Submit PR for review
    echo.
    pause
) else (
    echo.
    echo Push cancelled. Set up your remote first:
    echo git remote add origin https://github.com/YOUR_USERNAME/Study-Log.git
    echo.
    pause
)
