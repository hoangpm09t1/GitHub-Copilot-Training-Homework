# GitHub Copilot Assignment

## 1. Describe two different ways you can trigger a suggestion from GitHub Copilot. What is the difference between an inline suggestion and the Copilot Chat/Panel?

There are multiple ways to trigger suggestions from GitHub Copilot in Visual Studio Code.

### First Method: Inline Suggestions While Typing

When writing code, Copilot automatically analyzes the current context and displays a gray “ghost text” suggestion directly inside the editor.

You can also manually trigger it using keyboard shortcuts such as:

- `Alt + \` (Windows/Linux)
- `Option + \` (Mac)

Pressing `Tab` accepts the suggestion.

### Second Method: Using Copilot Chat or the Copilot Panel

Users can open the Copilot Chat sidebar and ask questions in natural language, such as:

- “Create a REST API endpoint”
- “Explain this function”
- “Refactor this code”

The panel provides longer explanations, code generation, debugging help, and interactive conversations.

### Difference Between Inline Suggestions and Copilot Chat/Panel

- **Inline suggestions** are quick autocomplete-style code predictions shown directly while coding. They are fast and context-based.
- **Copilot Chat/Panel** is more interactive and conversational. It allows users to ask complex questions, request explanations, generate larger code blocks, or troubleshoot problems.

---

## 2. What makes a "good" prompt? Based on your experience in this assignment, list three principles for writing effective prompts to get the best code from Copilot.

A good prompt is clear, specific, and provides enough context for Copilot to understand the task.

### Three Important Principles for Writing Effective Prompts

#### 1. Be Specific About the Task

Instead of saying “make a function,” describe exactly what the function should do.

**Example:**

- Weak: “Create login code”
- Better: “Create a Python login function that validates username and password using hashed passwords.”

#### 2. Provide Context and Requirements

Mention the programming language, framework, expected inputs/outputs, or coding style.

**Example:**

> Using Spring Boot, create a REST API endpoint that returns JSON data.

#### 3. Break Large Tasks into Smaller Steps

Copilot performs better when tasks are focused.

Asking for one component at a time reduces errors and improves code quality.

---

## 3. Did you encounter any instances where Copilot generated incorrect, inefficient, or insecure code? Describe one such instance and how you identified and corrected it.

Yes. During the assignment, there was an instance where Copilot generated code that was not secure.

For example, Copilot suggested building an SQL query using string concatenation like this:

```java
String query = "SELECT * FROM users WHERE username = '" + username + "'";
```

This approach is vulnerable to SQL Injection attacks because user input is directly inserted into the query.

I identified the issue because concatenating raw user input into SQL statements is considered an insecure coding practice.

To correct it, I replaced the code with a parameterized query using `PreparedStatement`:

```java
PreparedStatement stmt = connection.prepareStatement(
    "SELECT * FROM users WHERE username = ?"
);
stmt.setString(1, username);
```

This solution is safer because it separates user input from the SQL command and prevents malicious SQL execution.