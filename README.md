# Flamingo

[![Android](https://github.com/Kwasow/Flamingo/actions/workflows/android.yml/badge.svg)](https://github.com/Kwasow/Flamingo/actions/workflows/android.yml)
[![Backend](https://github.com/Kwasow/Flamingo/actions/workflows/backend.yml/badge.svg)](https://github.com/Kwasow/Flamingo/actions/workflows/backend.yml)

**The backend is currently being rewritten to Spring Boot. The main branch contains
the latest SNAPSHOT code. This version is unstable and may receive breaking changes
without notice.**

An app for couples for storing and celebrating memories, sharing wishes, sending
nudges and much more.

You can self-host the app, so that you can keep your data safe and under your
control. The backend can run on any docker enabled server and the mobile app
supports Android 6 or newer. Future plans include an iOS and Desktop/Web apps.

_**Why flamingo?** Because flamingos form life-long relationships_

## Features

📆 **Memories** - keep track of your memories and show your relationship timeline.

🎶 **Music** - record songs for each other and listen to them in the app.

✨ **Wishlist** - keep track of gift ideas and wishes.

💔 **Missing You** - send a nudge to your loved ones to tell them that you're
missing them.

🧭 **Location** - help each other find you and keep your significant other safe
when they are alone. Privacy controls included.

## Getting the app

**Version 3.0** is in the works. I want to build privacy mechanisms into the app
(like end-to-end encryption of most data) and finish the multi-user support
features. When all of that is done, the app will release on the Play Store and
you'll be able to install the app and setup an account from there.

**Current SNAPSHOT** Built in Spring Boot with a mariadb database. Moving closer
towards the 3.0 milestone.

**Version 2.0** is the first public release built in PHP. Some multi-user
features have been merged, but there were no privacy protections built in.
The app was not available in any store for download. If you want to use this
version of the app, you can host it yourself. If you wish to do that, please
follow the instructions in [HOSTING.md](https://github.com/Kwasow/Flamingo/blob/601a5401c52152faf893c5626304874a60e40725/HOSTING.md).
Be aware, that this version  of the app/backend will not be receiving any updates.

**Version 1.0** was never released to the public, but was a fully serverless
app. Due to that, it had some serious security issues (like bundling a private
key in the app for sending Firebase messages directly from the app).

## Architecture

The mobile app is built in Kotlin with Jetpack Compose and uses Koin for
dependency injection.

Firebase Auth and Sign in with Google are used for authentication as I don't
feel confident building my own solution, but I'm sure that I can keep everything
safe with a little help from those. I know it's not ideal, but I want to keep
that part simple. Firebase tokens are used to verify the users identity on the
server and are sent with every request in the `Authorization` header.

Memory notifications are sent through a notification that is triggered by
a cron job in the Spring Boot server. In the past I used Android's `Alarm manager`,
but it was unreliable and often failed on some brands of phones that managed
background processes too aggressively. I've found that sending a Firebase
notifications reliably wakes up the app and allows it to do its job. The
server cron job itself doesn't have any memories related logic, it
doesn't check if there are any memories for the day. It's only job is to wake
up the app so it can do its thing.
