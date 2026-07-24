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
- PC with Windows

## How to run

1. Download and
   unpack
   the [package for Windows](https://github.com/kubo44/bamboo-notifier/releases/latest/download/bamboo-notifier.zip)
2. Update the configuration in bamboo-notifier.yml.
3. (Optional) Set up Autostart – see the FAQs.

## Configuration

In the configuration file `bamboo-notifier.yml` provide:

- `url`: Base URL of your Bamboo server
- `token`: Personal access token with read permissions for builds (can be generated in your Bamboo profile)
- `projects`: Comma‑separated list of Bamboo project names to monitor
- `checkIntervalMinutes`: (optional) How often the app should check Bamboo for build statuses (default: 15)

## FAQ

### How to start the app automatically?

1. Extract zip to the folder of your choice.
2. Press Windows Key + R to open the "Run" dialog.
3. Type shell:startup and click OK.
4. Drag and drop the application's shortcut (or a copy of the executable) into this folder.

### How to Build

1. Build the application with Maven.
2. In target directory you will find archive bamboo-notifier.zip
3. Extract the archive and modify bamboo-notifier.yml as needed.
4. Start the application: bamboo-notifier.exe

## Known Issues

* Check if server is available before processing
* Nice error message when incorrect parameter (e.g. invalid project name)

## License

See the LICENSE file.