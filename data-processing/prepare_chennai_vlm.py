import pandas as pd
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent

INPUT_FILE = (
    BASE_DIR
    / "data"
    / "raw"
    / "chennai"
    / "Chennai_VLM_data.csv"
)

OUTPUT_DIR = BASE_DIR / "data" / "processed"
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

OUTPUT_FILE = OUTPUT_DIR / "chennai_vlm_processed.csv"

print("=" * 60)
print("InfraSub - Chennai VLM Data Processing")
print("=" * 60)

# Load raw dataset
df = pd.read_csv(INPUT_FILE)

print(f"\nLoaded records: {len(df):,}")

# Rename columns to application-friendly names
df = df.rename(columns={
    "Longitude": "longitude",
    "Latitude": "latitude",
    "VLM_cm_per_yr": "vlm_cm_per_year"
})

# Convert cm/year → mm/year
df["vlm_mm_per_year"] = df["vlm_cm_per_year"] * 10

# Create PostGIS-compatible WKT geometry
df["geometry"] = (
    "POINT("
    + df["longitude"].astype(str)
    + " "
    + df["latitude"].astype(str)
    + ")"
)

# Arrange columns
df = df[
    [
        "latitude",
        "longitude",
        "vlm_cm_per_year",
        "vlm_mm_per_year",
        "geometry"
    ]
]

# Save processed dataset
df.to_csv(OUTPUT_FILE, index=False)

print(f"Processed records: {len(df):,}")

print("\nColumns:")
for column in df.columns:
    print(f"  - {column}")

print("\nFirst 5 processed records:")
print(df.head())

print("\nOutput:")
print(OUTPUT_FILE)

print("\n" + "=" * 60)
print("Processing completed.")
print("=" * 60)