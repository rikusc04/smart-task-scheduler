# Smart Task Scheduler

A Java-based task scheduling system that uses the **Earliest Deadline First (EDF)** algorithm to prioritize and schedule tasks based on deadlines and priority. Includes AI-powered scheduling advice via the Claude API.
Video Link/Demo: https://youtu.be/5qGQ2tsZ8aM

## Features

- Add tasks with a name, duration, deadline, and priority (LOW / MEDIUM / HIGH)
- EDF scheduling with priority as a tiebreaker
- Max 12 hours of work scheduled per day, starting at 9:00 AM
- Flags tasks that cannot be fit before their deadline
- AI suggestions via Claude to improve your schedule (requires an Anthropic API key)

## Requirements

- Java 17+
- Maven 3.6+
- An `ANTHROPIC_API_KEY` environment variable (optional, for AI suggestions)

## Build & Run

```bash
mvn package
java -jar target/smart-scheduler-1.0-SNAPSHOT.jar
```

## Commands

| Command    | Description                              |
|------------|------------------------------------------|
| `add`      | Add a new task                           |
| `list`     | List all tasks                           |
| `delete`   | Delete a task by ID                      |
| `schedule` | Generate and display the schedule        |
| `suggest`  | Get AI suggestions (requires API key)    |
| `help`     | Show the help menu                       |
| `quit`     | Exit the program                         |

## Running Tests

```bash
mvn test
```

## Project Structure

```
src/
├── main/java/scheduler/
│   ├── MainApp.java               # Entry point, CLI
│   ├── SchedulerEngine.java       # EDF scheduling algorithm
│   ├── Schedule.java              # Schedule data structure
│   ├── Task.java                  # Task model
│   ├── TimeSlot.java              # Scheduled time block
│   ├── Priority.java              # Priority enum
│   ├── ConstraintValidator.java   # Input validation
│   ├── AiAdvisor.java             # Claude API integration
│   ├── InvalidTaskException.java
│   └── UnschedulableTaskException.java
└── test/java/scheduler/
    ├── TaskTest.java
    ├── TimeSlotTest.java
    ├── SchedulerEngineTest.java
    └── ConstraintValidatorTest.java
```
