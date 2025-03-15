# Flamingo

[![Android](https://github.com/Kwasow/Flamingo/actions/workflows/android.yml/badge.svg)](https://github.com/Kwasow/Flamingo/actions/workflows/android.yml)
[![CodeQL Advanced](https://github.com/Kwasow/Flamingo/actions/workflows/codeql.yml/badge.svg)](https://github.com/Kwasow/Flamingo/actions/workflows/codeql.yml)

An app for couples for storing and celebrating memories, sharing wishes, sending
nudges and much more.

It is made to be self-hosted, so that you can keep your data safe and under your
control. The backend can run on any PHP enabled server and the mobile app
supports Android 6 or newer. iOS is currently unsupported, but might come later
when Compose Multiplatform matures.

_**Why flamingo?** Because flamingos form life-long relationships_

## Features

📆 **Memories** - keep track of your memories and show your relationship timeline.

🎶 **Music** - record songs for each other and listen to them in the app.

✨ **Wishlist** - keep track of gift ideas and wishes.

💔 **Missing You** - send a nudge to your loved once to tell them that you're
missing them.

🧭 **Location** - help each other find you and keep your significant other safe
when they are alone. Privacy controls included.

## Setup

The backend is written in PHP and tested with version 8.2. Dependencies are
managed by composer. The project also uses Firebase for authentication and
sending notifications.

### Project setup

Go to the [Firebase Console](https://console.firebase.google.com/) and create
a new project - I disabled Gemini and Analytics in my case, as these are not
needed.

Now enable "Sign in with Google" by going into Build > Authentication > Set up
sign-ing method and select "Google". Enable it, set a recognizable name (like
"Flamingo"), select an email address from the list and press "Save".

Then follow [this](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
tutorial to enable the Google Maps SDK for the project and get the API key.
Take note of it, we will need it in a moment.

### Firebase setup

First install the Firebase CLI following the
[official instructions](https://firebase.google.com/docs/cli/). Then in the
`firebase/` directory run:

```bash
firebase login
cd functions/
npm install
cd ..
firebase deploy
```

### Android setup

For sign-in with Google to work, we have to add our signing keys to Firebase.
In the Firebase Console navigate to Project Overview > Cog icon > Project
settings > General and then follow the steps to add your app at the bottom of
the page. You'll have to add at least the release version of your app (package
name is `pl.kwasow.flamingo`), but it is advised to also add the debug versions,
so you can test before releasing (package name is `pl.kwasow.flamingo.beta`).

For generating a new signing key and checking it's fingerprint follow the
[official guide](https://developer.android.com/studio/publish/app-signing) from
Google.

After adding both versions of the app, click the "google-services.json" button
to download the JSON file containing details about our Firebase project and place
it in the `android/app` directory. Do not change the filename.

Now go into the `app/android/` directory and copy the `secrets.default.properties`
to `secrets.properties`. Now edit the new file and replace all fields with your
values:

- _MAPS\_API\_KEY_ - the API key for the Maps SDK that you created earlier
- _BASE\_URL_ - the URL where your server is hosted
- _DEVELOPMENT\_BASE\_URL_ - the URL where your test server is hosted (could be
  the same as _BASE\_URL_)
- _GOOGLE\_WEB\_CLIENT\_ID_ -
- _RELATIONSHIP\_START_ - the date you started dating (or what you consider the
  start of your relationship) in the "YYYY-MM-DD" format

### Server setup

Choose a hosting provider that supports PHP (preferably version 8.2) and uses a
mysql database - I use OVHCloud for my setup, since it is pretty cheap at around
50$ a year for a domain and basic hosting.

Through the hosting provider, access your database and create all the necessary
tables based on the `scripts/mysql/100-mysql_init.sql` file. Then insert the
necessary details into the `Users` table. You might have to modify the table
temporarily:

```sql
ALTER TABLE User
DROP CONSTRAINT FK_MissingYouRecipient;

-- Insert what you need here

ALTER TABLE User
ADD CONSTRAINT FK_MissingYouRecipient FOREIGN KEY (missing_you_recipient) REFERENCES User(id);
```

Now go into the `backend/` directory and run `composer install` to install the
servers dependencies.

Create a file under `backend/config/config.php` and put in the following, while
substituting the default values with the appropriate ones for you:

```php
<?php

function getFlamingoConfig() {
  return [
    "anniversaryMonth" => 8,
    "anniversaryDay" => 31,
    
    "databaseAddress" => "database",
    "databaseUsername" => "flamingo-user",
    "databasePassword" => "flamingo-user",
    "databaseName" => "flamingo"
  ];
}

?>
```

Then go to [this](https://console.cloud.google.com/iam-admin/serviceaccounts?inv=1&invt=AbsHIw&walkthrough_id=iam--create-service-account)
site and create a new key for the `firebase-adminsdk` service account. Download
it and put it into the `backend/config/` directory and name it `googleServiceAccount.json`.

Now you can upload all the server code to your hosting provider through FTP.
Make sure to include these files and directories from the `backend/` directory:

- `api/`
- `config/`
- `src/`
- `vendor/`
- `.htaccess`

After uploading everything, make sure that these URLs return a 403 error and
are not browsable (a file list doesn't show).

- `$SERVER_URL/config/config.php`
- `$SERVER_URL/config/googleServiceAccount.json`
- `$SERVER_URL/vendor/`

## Building

To build the app for testing, just open the `android/` directory in
[Android Studio](https://developer.android.com/studio) and press the "play"
icon in the upper-right hand corner.

To build the app for release and sign it, select the menu in the upper-left
hand corner and the select Build > Generate signed App Bundle/APK. If you
don't know which one to choose, please follow to the next section, to help you
decide.

## Distribution

There are a couple options when it comes to the distribution of the app:

1. Build a signed APK and share it through your preferred method (easy)

2. Create your own flavor of the app in `build.gradle.kts` with a different
   `applicationId` and distribute it as an Internal Testing build through the
   Google Play store. This is more difficult to set up, but more convenient.
   Build an App Bundle if you're going for this option.

## Architecture

The mobile app is built in Kotlin with Jetpack Compose and uses Koin for
dependency injection.

Firebase Auth and Sign in with Google are used for authentication as I don't
feel confident building my own solution, but I'm sure that I can keep everything
safe with a little help from those. I know it's not ideal, but I want to keep
that part simple. Firebase tokens are used to verify the users identity on the
server and are sent with every request in the `Authorization` header.

Memory notifications are sent through a notification that is triggered by
a Firebase Functions daily job. In the past I used Android's `Alarm manager`,
but it was unreliable and often failed on some brands of phones that managed
background processes to aggressively. I've found that sending a Firebase
notifications reliably wakes up the app and allows it to do it's job. The
Firebase Functions task itself doesn't have any memories related logic, it
doesn't check if there are any memories for the day and it doesn't communicate
with the server. It's only job is to wake up the app so it can do it's thing.
