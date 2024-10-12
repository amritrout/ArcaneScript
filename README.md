# ArcaneScript

ArcaneScript is a custom programming language developed to explore language design and interpretation. This project includes the development of both the ArcaneScript language and its online Code Runner. The grammar was written using GOLD Parser and the interpreter in Java, enabling users to write, test, and execute ArcaneScript code.

## Features

- **Custom Programming Language**: ArcaneScript, a unique language with its own syntax and grammar rules.
- **Online Interpreter**: A web-based interface that allows users to write and execute ArcaneScript code.
- **Code Editor**: Integrated with CodeMirror for a rich editing experience with syntax highlighting.
- **AWS Hosting**: The live version of ArcaneHttpServer is deployed on Amazon AWS.
- **Netlify Hosting**: The live version of frontend is hosted on Netlify.

## Language Features

ArcaneScript is continuously evolving, with new features being added to enhance its capabilities. Here's an overview of the current and upcoming language features:

<details>
  <summary>Currently Implemented Features</summary>

- **Mathematical Operations**: Support for basic arithmetic and complex mathematical expressions.
- **Comparison Operations**: Ability to compare values and expressions.
- **Loops**: For & While loop.
- **Arrays**: Support for one-dimensional arrays
- **Functions**

</details>

<details>
  <summary>Upcoming Features (Version 2)</summary>

- **2D Arrays**
- **Linked List**
- **Stack**
- Additional data structures

</details>


## Technologies Used

- **Programming Language**: Java
- **Parsing Library**: GOLD Parser
- **Web Technologies**: HTML, CSS, JavaScript
- **Code Editor**: CodeMirror
- **Cloud Hosting**: Amazon AWS (backend), Netlify (frontend)

## Project Structure

The project is organized into the following directories:

- `/backend`: Contains the backend services.
  - `/arcaneInterpreter`: The core interpreter for the ArcaneScript language.
  - `/ArcaneHttpServer`: HTTP server to handle requests and interactions.
- `/frontend`: Contains the frontend HTML file and associated assets.

## Setup Instructions

### Prerequisites
- Java 20 or later
- Maven 3.8.4 or later

### Backend Setup

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/arcane-script.git
   cd arcane-script/backend
   ```

2. Build the backend projects:
   ```
   mvn clean install
   ```
   This command will build both the arcaneInterpreter and ArcaneHttpServer modules.

### Frontend Setup

The frontend is a static HTML file that can be served directly. No additional setup is required.

## Usage

### Running Locally

To run the project locally:

1. Start the backend server:
   ```
   cd arcane-script/backend/ArcaneHttpServer/target
   java -jar ArcaneHttpServer.jar
   ```

2. Open the frontend HTML file in your web browser:
   ```
   cd arcane-script/frontend
   ```
   Open the HTML file in your preferred browser.

### Live Version

You can access the live version of ArcaneScript without any local setup:

- Frontend: [https://arcanescript.netlify.app/](https://arcanescript.netlify.app/)

Use the Netlify-hosted frontend to write and run ArcaneScript code, which will be processed by the AWS-hosted backend.

## Contributing

We welcome contributions to ArcaneScript! If you have suggestions for improvements or encounter any issues, please feel free to open an issue or submit a pull request. For significant changes, please open an issue first to discuss your proposed changes.

## Acknowledgments

- GOLD Parser for providing the parsing framework.
- **[ridencww](https://github.com/ridencww)** for their continued development and support of the `goldengine` library, which provides the foundation for the ArcaneScript interpreter.
- CodeMirror for the excellent code editing capabilities.
- The open-source community for inspiration and tools that made this project possible.
