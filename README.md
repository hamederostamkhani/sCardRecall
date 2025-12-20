# sCardRecall
A cross-platform spaced repetition learning system supporting multiple recall algorithms.

## Overview
sCardRecall is a multi-platform application designed to help users learn and retain information using spaced repetition techniques.
Unlike traditional flashcard apps, this project is designed to support **multiple recall algorithms** and allows new methods to be added incrementally over time.

## Supported Recall Methods
- Leitner System
- SM-2 (SuperMemo)
- Additional algorithms planned and extensible by design

## Architecture
The project follows a client-server architecture with a centralized backend and multiple clients:

- Backend: Spring Boot RESTful API
- Web Client: Vue.js
- Android Client: Jetpack Compose
- Desktop Client: JavaFX

## Project Structure
- **backend/**
    - Centralized backend service
    - RESTful APIs and recall scheduling logic
    - Pluggable spaced repetition algorithms (Leitner, SM-2, etc.)

- **webapp/**
    - Web-based client application
    - Built with Vue.js and Tailwind CSS

- **android/**
    - Native Android client
    - Implemented using Jetpack Compose

- **desktop/**
    - Cross-platform desktop client
    - Implemented using JavaFX

## Design Goals
- Support multiple spaced repetition algorithms
- Allow easy experimentation with new recall strategies
- Maintain clean separation between algorithm logic and UI
- Provide a consistent API for all clients

## Tech Stack
- Java, Spring Boot, Spring Security, Spring Data
- Vue 3, Javascript, Tailwind CSS
- Kotlin, Jetpack Compose
- JavaFX
- PostgreSQL
- Docker & Docker Compose

## Development Status
🚧 This project is under active development.
Recall algorithms and client features are being added incrementally.