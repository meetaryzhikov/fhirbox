# FHIRbox

A simple FHIR server implementation in Clojure using PostgreSQL for data storage.

## Features

- FHIR CapabilityStatement endpoint
- PostgreSQL with JSONB for resource storage
- Integrant for system configuration
- Ring + Jetty for HTTP server
- Automatic SQL migrations

## Requirements

- Java 11+
- Clojure 1.12+
- Docker and Docker Compose (for PostgreSQL)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/meetaryzhikov/fhirbox.git
   cd fhirbox
   ```

2. Start PostgreSQL:
   ```bash
   docker-compose up -d
   ```

3. Run migrations and start the server:
   ```bash
   ./server.sh start
   ```

## Usage

- Server runs on http://127.0.0.1:3001
- GET / : Health check
- GET /metadata : FHIR CapabilityStatement

## API

### GET /

Returns a simple health check message.

### GET /metadata

Returns the FHIR CapabilityStatement in JSON format.

### POST /:resourceType

Creates a new FHIR resource of the specified type.

**Example:**
```bash
curl -X POST http://127.0.0.1:3001/Patient \
  -H "Content-Type: application/json" \
  -d '{"resourceType": "Patient", "name": [{"family": "Doe", "given": ["John"]}]}'
```

### GET /:resourceType

Searches for resources of the specified type.

**Example:**
```bash
curl http://127.0.0.1:3001/Patient
```

### GET /:resourceType/:id

Retrieves a specific resource by ID.

**Example:**
```bash
curl http://127.0.0.1:3001/Patient/123
```

## Development

- Start REPL: `clojure -M:repl/cider`
- Run tests: `clojure -M:test`
- Run linter: `clojure -M:cljfmt check` (if configured)

## Database

Connect to PostgreSQL:
```bash
docker-compose exec postgres psql -U fhirbox -d fhirbox
```

## Stopping

```bash
./server.sh stop
docker-compose down
```

## License

MIT
