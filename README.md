# Bamboo Build Monitor

A small desktop application that periodically checks builds in Bamboo and shows notifications for any builds
that do not have status Successful.

## Features

- Connects regularly to Bamboo
- Check build status of configured projects
- Displays a notification for builds that are not Successful

## Prerequisites

- Java installed
- Access to a Bamboo instance with a valid **Personal access token**

## Configuration

In the configuration file `bamboo-notifier.yml` provide:

- `url`: Base URL of your Bamboo server
- `token`: Personal access token with read permissions for builds (can be generated in your Bamboo profile)
- `projects`: Comma‑separated list of Bamboo project names to monitor
- `checkIntervalMinutes`: (optional) How often the app should check Bamboo for build statuses (default: 15)

## How to Run

1. Build the application with Maven.
2. In target directory you will find archive bamboo-notifier-0.0.1-SNAPSHOT.zip
3. Extract the archive and modify bamboo-notifier.yml as needed.
4. Start the application: bamboo-notifier.exe

## License

See the LICENSE file.