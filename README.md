# LumaGrid Street Light Monitoring System — V10

This version includes the LumaGrid welcome, sign-in and account creation screens, dashboard summary, street light management, network search and status filters, and the automatic day/night schedule.

## New: Activity History

Choose **Activity History** on the dashboard to review recent events, including lights added, edited, removed, or switched by the automatic schedule. The history table is created automatically when the app starts. It records activity from the first V10 launch onward; earlier activity is not included.

## Automatic schedule

While the application is open, it checks your computer's local time every minute and updates saved database statuses: OFF from 6:00 AM to 6:00 PM, ON from 6:00 PM to 6:00 AM. This updates database records only; it does not control physical lamp hardware.

## Run in VS Code

1. Extract this ZIP file.
2. In VS Code, choose **File → Open Folder** and select the extracted `StreetLightUpdatedV10` folder (the one containing `pom.xml`).
3. If VS Code asks whether you trust the folder, choose **Trust**.
4. Open `src/main/java/com/streetlight/App.java`.
5. Click **Run** above `main`. If Run is not visible, right-click inside `App.java` and select **Run Java**.
6. Make sure MySQL is running and the connection settings in `src/main/java/com/streetlight/util/DBConnection.java` match your computer.

