# ![Logo](https://raw.githubusercontent.com/Nilon123456789/Wolke-simulator/refs/heads/master/src/main/resources/icons/wolke-logo-32.png) Wolke Simulator 🌬️

![GitHub Release](https://img.shields.io/github/v/release/Nilon123456789/Wolke-simulator)

Wolke Simulator is a project made by three students over the course of three months for their final year project. The simulator is simulating fluid flow using an Eulerian grid. The project was entirely made in Java ☕.

![example](doc/example.gif)

# Features ✨
- 2D fluid simulation 🌊
- Simulation settings ⚙️
- 2D drawing of obstacles 🖌️
- Image import for obstacles (png, svg) 🖼️
- Scene import and export 💾
- Themes 🎨
- Language selection 🌍
- Zen mode 🧘
- And more...

# Running the project and building the binaries 🛠️

The project is a maven java project. You therefore need to have maven and java installed on your machine if you want to run your own version of the project. You can also download the binaries from the [release page](https://github.com/Nilon123456789/Wolke-simulator/releases/latest) to only install the application.

## Installation Instructions 🔧

1. Clone the repository:
   ```bash
   git clone https://github.com/Nilon123456789/Wolke-simulator.git
   cd Wolke-simulator
   ```

2. Install Maven and Java 17+ on your system (if not already installed):
   - [Download Maven](https://maven.apache.org/install.html)
   - [Download JDK 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)

## Running the project 🚀
The project is using maven and java 17. You can run the project with the following command:

```bash
mvn compile exec:java -D"exec.mainClass=com.e24.wolke.application.AppPrincipale24"
```

## Creating binaries 📦

To create the jar, executable binaries, and the installer, you can use the following command:

```bash
mvn clean compile package
```
__Nota__ : The installer is created with the [jpackage](https://docs.oracle.com/en/java/javase/17/jpackage/jpackage) tool. You need to have the JDK 17 installed on your machine. Also, the binary is created for the current OS. If you want to create a binary for another OS, you need to run the command on that OS.

# Development 💻

We are using different pipelines to ensure the code quality and the project stability.

## Pre-commit hooks 🔄

The project uses pre-commit hooks to ensure the code quality. You can install the hooks with the following command:

Windows and Linux

```bash
pip install pre-commit
pre-commit install

# Test the hooks
pre-commit run --all-files
```

Mac

```bash
brew install pre-commit
pre-commit install

# Test the hooks
pre-commit run --all-files
```
