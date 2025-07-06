# Bolt AI Clone App
Web app like bolt.ai, where user can create a react based web app using prompts.
User, will be able to preview the app, enter more prompts to make changes to app.

# Architecture:
- Its a event and microservice based architecture, using Spring, Maven, Postgres, Kafka, Aws ECS.
- There are 3 main microservices running:
    - Project Service: Orchastrates the user requests, container update requests and ai requests.
    - Container Service: Manages the containers, creates new containers, updates existing container codes. These containers are running the react app, on AWS ECS. They are to have logs using AWS Cloudwatch.
    - AI Service: This service is responsible for generating the code based on user prompts, Currently we are using Perplexity AI Sonar Model.

# Event Flow:
- Client sends a request to create a new project.
- Project Service creates a new project record in DB with data (Project ID, Status = New), and send 'ContainerCreate' event with data (ProjectId) to Kafka.
- Container Service listens to 'ContainerCreate' event, creates a new container on AWS ECS using a pre generated task definition defining a basic react vite app image, and sends 'ContainerCreated' event to Kafka which container data (ProjectId, ContainerId, ContainerUrl).
- Project Service listens to 'ContainerCreated' event, updates the project record with container details with data (ContainerId, Status=Active, Url=ContainerUrl) for Given Project ID, and infroms user that project is ready via socket.
- User then send prompt to Project Service to update the app.
- Project Service, sets the Project Record to 'PROCESSING_AI', and sends 'AIRequest' event to AI Service with data (ProjectId, Prompt).
- AI Service listens to 'AIRequest' event, generates the code and summary using Perplexity AI Sonar Model, and sends 'AIResponse' event to Kafka with data (ProjectId, Code, Summary).
- Project Service listens to 'AIResponse' event, updates the project record to 'PROCESSING_CODE', and sent 'CONTAINER_UPDATE' event to Container Service with data (ProjectId, Code), and send summary to user via socket.
- Container Service listens to 'CONTAINER_UPDATE' event, updates the container code with new code, restart the react vite app and sends 'ContainerUpdated' event to Kafka with data (ProjectId, ContainerId).
- Project Service listens to 'ContainerUpdated' event, updates the project record to 'ACTIVE', and informs user with preview link via socket.
- Now user can preview the app.

- FURTHER STEPS OF PROMPT UPDATE, IS TO BE DISCUSSED LATER.
