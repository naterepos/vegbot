# VegBot 

VegBot is an interactive Discord bot designed to work for servers concerned about animal ethics. While the bot is public, there is currently not 
public hosted option so anyone looking to use the bot must build this jar and run it themselves as a standalone bot. This should give the freedom to edit it how 
any given developer sees fit.

## How to Build

1. Import project into any Java IDE
2. Import the gradle workspace from the build.gradle file
3. Run the `shadowJar` task
4. Execute the jar file
   1. This will shut down on first boot. Modify the configuration found at `Settings.conf` and reboot

## Features

### Command Library

VegBot comes built in with a command parser and builder. This allows you to create simple executor classes that handle
the functionality of a command without having to worry about checking user input and type casting. 

### Storage Library

Not only does VegBot automatically set up a MySQL database and relevant data, it also caches all user data as custom server
users called `VegUsers`. This allows you to create extra information on users without having to touch REST APIs.

### Permission/Role Library

Because Discord servers can get hectic and mismanaged, VegBot is equipped to handle roles and the permissions that
come with said roles in the backend. No longer are actual Discord roles needed to run commands.

### Interaction Library

Commands may be easy for developers but many users want a simple button-clicking system like they are used to with most 
things on the internet. To accommodate for that, VegBot has a library to handle reactions as if they were buttons. 