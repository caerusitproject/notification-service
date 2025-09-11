# Notification Service - Complete (Twilio SMS & WhatsApp)

This repo contains a Spring Boot Notification Service with real Twilio integrations for SMS and WhatsApp.

## Quick start with Docker Compose

Set env variables for Twilio in your shell (or use .env):
```
export TWILIO_ACCOUNT_SID=ACxxxx
export TWILIO_AUTH_TOKEN=yyyy
export TWILIO_FROM_NUMBER=+1xxxxxxxxxx
export TWILIO_WHATSAPP_FROM=whatsapp:+1415xxxxxxx
```

Then run:
```bash
docker-compose -f docker/docker-compose.yml up --build
```

The Notification Service will be available at http://localhost:8080

## REST API
POST /api/notifications/send
```json
{ "userId":"00000000-0000-0000-0000-000000000001", "channel":"SMS", "content":"Hello from Notification Service" }
```

Kafka topic: `task-events`
