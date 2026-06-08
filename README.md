# Real-Time Kids Tracking System

A real-time GPS tracking microservices system that allows parents to monitor their children, receive safe-zone notifications, and view GPS movement history.

> ⚠️ This project is still under active development and some services/features are not fully completed yet.

---

<div align="center">

<img width="2720" height="1480" alt="backend_architecture" src="https://github.com/user-attachments/assets/765b987c-073a-4266-81e4-c20f774297b4" />

</div>

---

# Project Overview

This project is built using a microservices architecture with Spring Boot and event-driven communication.

The system receives GPS coordinates from tracking devices in real time, processes location events, manages geofencing logic, and sends notifications when children enter or exit safe zones.

The architecture focuses on:

- scalability
- asynchronous communication
- real-time processing
- distributed systems design
- high-volume event streaming

---

# Architecture Overview

## API Gateway

The entry point of the system.

### Responsibilities

- Routing requests
- Centralized access management
- Authentication
- Forwarding requests to internal services

---

## Discovery Service

Handles service registration and discovery between microservices.

---

## EMQX Broker

Receives MQTT messages from GPS tracking devices and forwards them to the Ingestion Service.

---

## Ingestion Service

Responsible for:

- Receiving GPS data from devices
- Validating incoming events
- Publishing GPS events to Kafka

---

## Kafka

Used for high-volume GPS event streaming.

### Why Kafka?

- Handles massive event throughput
- Durable message storage
- Asynchronous processing
- Scalable event pipelines

Kafka allows GPS data to be consumed later by processing services without losing events.

---

## Process Service

Consumes GPS events from Kafka and processes movement history data before storing it in the database.

---

## Geofencing Service

Handles:

- Safe-zone logic
- Location verification
- Detecting entry/exit events

Uses Redis caching to reduce repeated service calls and improve performance.

---

## RabbitMQ

Used for asynchronous communication between services.

### Example

- Geofencing Service publishes notification events
- Notification Service consumes and sends alerts

### Why RabbitMQ?

- Decouples services
- Non-blocking communication
- Reliable event delivery

---

## Notification Service

Sends alerts to parents when:

- A child enters a safe zone
- A child exits a safe zone

---

## Device Service

Manages:

- Tracking devices
- Device-to-child relationships

---

## Family Service

Handles family and parent-related information.

---

## Billing Service

Responsible for subscription and billing management.

---

# Technologies Used

- Java
- Spring Boot
- Spring WebFlux
- Spring Security
- Kafka
- RabbitMQ
- Redis
- Docker
- PostgreSQL
- gRPC
- MQTT (EMQX)

---

# Goals of the Project

This project was created to practice and explore:

- Microservices architecture
- Event-driven systems
- Reactive programming
- Distributed communication
- Scalable backend design
- Real-time GPS processing

---
