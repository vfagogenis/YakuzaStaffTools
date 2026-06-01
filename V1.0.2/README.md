# Yakuza Staff Tools

Client-side Fabric staff moderation utility for Advancius Network and Shinigami Valley Network.

## Features

- Watchdog alert parser
- Top-left Watchdog HUD with last 5 detected targets
- Numpad 1-5 manual command shortcuts for Watchdog targets
- Spy chat detection
- Right-side Spy Chat panel
- Optional hiding of Spy Chat from vanilla chat
- Staff Online HUD using configured staff usernames from the tab list
- Clickable chat tab overlay: Yakuza, Vanilla, Watchdog, Spy
- JSON config at `config/yakuzastafftools.json`

## Requirements

- Minecraft 1.21.11
- Fabric Loader 0.18.4+
- Java 21

Fabric API is intentionally not required in this MVP.

## Build

```bash
./gradlew clean build
```

On Windows:

```bash
.\gradlew clean build
```

Output:

```text
build/libs/yakuzastafftools-1.0.0.jar
```

## Configuration

After first launch, edit:

```text
.minecraft/config/yakuzastafftools.json
```

Set real staff usernames and commands there.

## Disclaimer

For authorized staff use only on Advancius Network and Shinigami Valley Network.
