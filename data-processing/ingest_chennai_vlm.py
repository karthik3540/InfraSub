import pandas as pd
import psycopg2
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent

CSV_FILE = (
    BASE_DIR
    / "data"
    / "processed"
    / "chennai_vlm_processed.csv"
)

DB_CONFIG = {
    "host": "localhost",
    "port": 5433,
    "database": "infrasub",
    "user": "infrasub",
    "password": "infrasub_dev_password"
}

print("=" * 60)
print("InfraSub - Chennai VLM Data Ingestion")
print("=" * 60)

# Load processed CSV
df = pd.read_csv(CSV_FILE)

print(f"\nRecords loaded from CSV: {len(df):,}")

# Connect to PostgreSQL
connection = psycopg2.connect(**DB_CONFIG)
cursor = connection.cursor()

print("Connected to PostgreSQL.")

# Insert records
insert_query = """
    INSERT INTO insar_measurements (
        latitude,
        longitude,
        vlm_cm_per_year,
        vlm_mm_per_year,
        geom
    )
    VALUES (
        %s,
        %s,
        %s,
        %s,
        ST_SetSRID(ST_MakePoint(%s, %s), 4326)
    )
"""

records = [
    (
        row.latitude,
        row.longitude,
        row.vlm_cm_per_year,
        row.vlm_mm_per_year,
        row.longitude,
        row.latitude
    )
    for row in df.itertuples(index=False)
]

print("Starting insertion...")

cursor.executemany(insert_query, records)

connection.commit()

print(f"Inserted records: {cursor.rowcount:,}")

# Verify database count
cursor.execute("SELECT COUNT(*) FROM insar_measurements;")

count = cursor.fetchone()[0]

print(f"Database record count: {count:,}")

cursor.close()
connection.close()

print("\n" + "=" * 60)
print("INGESTION COMPLETED")
print("=" * 60)