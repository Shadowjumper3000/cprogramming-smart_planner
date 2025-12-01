# SmartPlanner Email Reminder System

## Overview
The SmartPlanner Email Reminder System automatically sends email notifications for upcoming tasks at a specified time before they're due.

## Features
- ✅ Automatic email reminders for tasks
- ✅ Configurable reminder timing (5 mins to 24 hours before due time)
- ✅ Secure email configuration storage
- ✅ Support for Gmail and other SMTP servers
- ✅ Background service that checks for reminders every minute
- ✅ Test email functionality to verify settings

## Setup Instructions

### 1. Configure Email Settings

1. **Launch SmartPlanner** and log in
2. **Go to Planner → Email Settings** button
3. **Fill in the simple form:**

   **Basic Setup (Required):**
   - **Your Email**: Your full email address (e.g., `yourname@gmail.com`)
   - **Send Reminders To**: Where to receive reminders (can be the same email)
   - **Email Password**: Your regular email password

   **That's it!** The default settings work for most email providers (Gmail, Outlook, Yahoo, etc.)

   **Advanced Settings** (optional - click "▼ Advanced Settings" to expand):
   - SMTP Host: Default is `smtp.gmail.com` (auto-configured for Gmail)
   - SMTP Port: Default is `587` (standard for most providers)
   - Use TLS: Enabled by default (recommended)
   - Username: Auto-filled from your email

4. **Click "Save Settings"**
5. **Click "Send Test Email"** to verify your configuration

### 2. Email Provider Notes

**Gmail Users:**
- Works with regular password for most accounts
- If you have 2-Step Verification enabled, you may need an App Password (see troubleshooting)

**Other Providers:**
- **Outlook.com**: Works with regular password (SMTP: `smtp-mail.outlook.com`)
- **Yahoo Mail**: Works with regular password (SMTP: `smtp.mail.yahoo.com`)
- Most email providers work with default settings

### 3. Enable Reminders for Tasks

When adding or editing a task:

1. Fill in the task details (Title, Description, etc.)
2. **Set a Due Date and Due Time** (required for reminders)
3. **Check "Enable Email Reminder"**
4. **Select reminder timing** from dropdown:
   - 5 minutes before
   - 10 minutes before
   - 15 minutes before
   - 30 minutes before (default)
   - 1 hour before
   - 2 hours before
   - 3 hours before
   - 6 hours before
   - 12 hours before
   - 24 hours before
5. Click **Save**

## How It Works

1. **Background Service**: SmartPlanner runs a background service that checks for pending reminders every minute
2. **Reminder Calculation**: For each task with reminders enabled, it calculates when to send the reminder based on the due date/time and the "remind before" setting
3. **Email Sending**: When it's time to send a reminder, an email is automatically sent with task details
4. **No Duplicates**: The system tracks sent reminders to avoid sending duplicates

## Email Content

Reminder emails include:
- Task Title
- Description
- Due Date & Time
- Priority Level
- Category
- Custom message encouraging timely completion

## Troubleshooting

### "Failed to send email" Error

**Check the following:**

1. **Internet Connection**: Ensure you're connected to the internet
2. **Email Credentials**: Verify username and password are correct
3. **App Password**: For Gmail, make sure you're using an App Password, not your regular password
4. **SMTP Settings**: Confirm SMTP host and port are correct
5. **Firewall/Antivirus**: Some security software may block SMTP connections
6. **2-Step Verification**: Gmail requires 2-Step Verification to generate App Passwords

### Reminders Not Being Sent

**Verify:**

1. **Email Configuration**: Go to Settings → Email Settings and click "Send Test Email"
2. **Task Settings**: Ensure the task has:
   - A due date
   - A due time
   - "Enable Email Reminder" checked
3. **SmartPlanner is Running**: The application must be running for reminders to be sent
4. **Reminder Timing**: The reminder is sent at the calculated time (e.g., 30 minutes before due time)
5. **Check Spam Folder**: Reminder emails may be filtered to spam

### Gmail "Authentication Failed" Error

**If using 2-Step Verification**, you'll need an App Password:
1. Go to https://myaccount.google.com/security
2. Enable 2-Step Verification (if not already enabled)
3. Go to "App passwords" 
4. Generate a new password for "Mail" / "Windows Computer"
5. Use this 16-character password in SmartPlanner instead of your regular password

**Without 2-Step Verification**: Your regular password should work

## Technical Details

### Files Created
- `data/email_config.properties` - Stores email configuration
- `data/planner.csv` - Stores tasks with reminder settings

### Dependencies
- JavaMail API 1.6.2 (included in pom.xml)

### Classes Added
- `EmailConfig` - Model for email settings
- `EmailService` - Handles email sending
- `ReminderService` - Background service for reminder checking
- `EmailSettingsPage` - UI for configuring email
- `TaskReminderDialog` - Enhanced task dialog with reminder options

## Security Notes

⚠️ **Password Storage**: Email passwords are stored in plain text in `data/email_config.properties`. Keep this file secure and do not share it.

**Best Practices:**
- Use App Passwords instead of main account passwords
- Keep your email configuration file private
- Use a dedicated email account for notifications if concerned about security
- Regularly rotate your App Passwords

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Verify your email provider's SMTP settings
3. Test with the "Send Test Email" button
4. Check the console output for detailed error messages

## Future Enhancements

Potential features for future versions:
- Multiple recipient support
- Custom email templates
- SMS reminder integration
- Calendar integration
- Recurring task reminders
- Reminder escalation (multiple reminders per task)
