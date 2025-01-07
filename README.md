# hackmerlin.io

## Project Structure

- **backend**: Contains the Spring Boot application.
- **frontend**: Contains the React application.

## Getting Started

Currently, this project supports only azure openai deployments as LLM backend.
To point this app to your deployment, you need to set the following `application.yml` properties:

```yaml
merlin:
  azure:
    key: your-openai-api-key
    url: your-openai-api-url
```

## Running the Application

Start the application:

```sh
./gradlew run
```