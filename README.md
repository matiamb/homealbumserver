## HomeAlbumServer

HomeAlbumServer is a self-hosted Spring Boot API designed to run on your home server to store photos and videos uploaded from the HomeAlbum Android app.

It is intended for use inside a trusted local network or through a private network such as Tailscale.

## Features
- Upload photos and videos from the HomeAlbum Android app
- Store media files directly on the server filesystem
- Store media metadata using an embedded H2 database
- Automatically create the database on first startup
- Detect duplicate files using SHA-256 hashes
- Delete stored media using its hash
- Validate supported media file types
- Configurable media storage directory
- REST API built with Spring Boot
## Requirements
- Java 21
- A writable directory for storing media files

No external database installation is required. HomeAlbumServer uses an embedded H2 database that is created automatically when the server starts.

## Configuration

API configuration is handled through:

application.properties

This includes settings such as:

- H2 database username and password
- Media storage directory

**Do not commit real credentials, private IP addresses or other sensitive configuration values to a public repository.**

## Running the Server

Build the project:

`./mvnw clean package`

Then run the generated JAR:

`java -jar target/homealbumserver-0.1.0-SNAPSHOT.jar`

Alternatively, download the JAR from the project's GitHub Releases page and run it directly:

`java -jar homealbumserver-0.1.0.jar`

By default, Spring Boot runs on:

`http://localhost:8080`

## Network Usage

HomeAlbumServer is designed primarily for self-hosted environments.

You can access it:

- From devices connected to the same local network
- Through a private network such as Tailscale

**The API currently does not implement its own authentication layer, so exposing it directly to the public Internet is not recommended.**

## Database

HomeAlbumServer uses an embedded H2 database to store information about uploaded media.

The database is created automatically when the server starts for the first time, so no external database server is required.

Media files themselves are stored on the filesystem, while their metadata and paths are stored in the database.

## HomeAlbum Android App

HomeAlbumServer is designed to work together with the HomeAlbum Android application.

The Android app connects to this API to upload, check and delete backed-up media files.

HomeAlbum Android app: https://github.com/matiamb/HomeAlbum/releases

## Contributing
HomeAlbumServer is currently maintained as a personal project.

Bug reports and feature suggestions are welcome through GitHub Issues. Pull Requests are not being accepted at this stage.
  
## Current Status
HomeAlbumServer is currently in early development.

The 0.1.x releases provide the initial functionality required for communication with the HomeAlbum Android app and basic media backup management.
