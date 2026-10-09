# InfraSub — Multimodal InSAR Subsidence Digital Twin

![InfraSub](https://img.shields.io/badge/Status-Active-brightgreen)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-PostGIS%20%7C%20pgvector-blue)

InfraSub is a geospatial intelligence dashboard designed to evaluate infrastructure risk stemming from ground subsidence. By leveraging Vertical Land Motion (VLM) measurements (derived from satellite InSAR data), the application calculates subsidence statistics and provides deterministic engineering risk assessments for civil infrastructure.

## 🌟 Features

- **Spatial VLM Analysis**: Leverages PostGIS (`ST_DWithin`) to accurately retrieve InSAR data points within a defined radius of any target infrastructure.
- **Deterministic Risk Scoring**: Evaluates average subsidence, peak local sink, and spatial extent to calculate a risk score (0-100) using engineering heuristics.
- **Infrastructure Vulnerability Multiplier**: Adjusts risk levels dynamically based on the type of infrastructure (e.g., Metro Stations are weighted higher than Roads).
- **Interactive Map Dashboard**: A responsive Vanilla JS & Leaflet frontend showcasing subsidence scatterers via a color-coded heat map.
- **RAG-based Context Retrieval**: Utilizes PostgreSQL's `pgvector` extension and text-search capabilities to fetch relevant geotechnical literature based on the risk profile.

## 🏗️ Architecture

- **Frontend**: Vanilla JavaScript, HTML5, CSS3, Leaflet JS (with CARTO Dark Matter tiles).
- **Backend API**: Java 17, Spring Boot, Spring Data JPA.
- **Database**: PostgreSQL with PostGIS (for spatial geometries) and pgvector (for document embeddings).
- **Data Ingestion**: Python (Pandas, psycopg2) used to process raw CSV data and load it into PostgreSQL.

## 🚀 Getting Started

### Prerequisites
- Docker & Docker Compose
- Java 17 & Maven
- Python 3.9+ (for data ingestion)

### 1. Database Setup
Start the PostgreSQL database (pre-configured with PostGIS and pgvector) using Docker Compose:
```bash
docker-compose up -d
```
The database runs on port `5433` with the username `infrasub` and password `infrasub_dev_password`.

### 2. Data Ingestion (Optional/First-Time Setup)
Process the raw VLM data and ingest it into the database:
```bash
cd data-processing
python3 prepare_chennai_vlm.py
python3 ingest_chennai_vlm.py
```

### 3. Running the Backend
Navigate to the backend directory and run the Spring Boot application. Flyway will automatically execute database migrations (`V1` to `V6`) on startup.
```bash
cd backend/infrasub-backend
./mvnw spring-boot:run
```
The API will be available at `http://localhost:8080`.

### 4. Running the Frontend
The frontend consists of static files. You can serve them using any local web server (or run it directly from the backend's static resources if packaged together).
```bash
cd frontend
python3 -m http.server 3000
```
Open `http://localhost:3000` in your browser.

## 🔍 Key API Endpoints

- `GET /api/infrastructure` - Fetch preset infrastructure assets.
- `GET /api/insar/nearby?latitude={lat}&longitude={lon}&radius={r}` - Fetch spatial InSAR scatter points.
- `GET /api/assessment/evaluate?latitude={lat}&longitude={lon}&radius={r}&type={type}` - Execute the full risk assessment and RAG context generation.

## 🤝 Contributing
Contributions are welcome! Please feel free to submit a Pull Request.

## 📜 License
This project is open-source and available under the MIT License.
