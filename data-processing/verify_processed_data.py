import pandas as pd
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent

RAW_FILE = (
    BASE_DIR
    / "data"
    / "raw"
    / "chennai"
    / "Chennai_VLM_data.csv"
)

PROCESSED_FILE = (
    BASE_DIR
    / "data"
    / "processed"
    / "chennai_vlm_processed.csv"
)

print("=" * 60)
print("InfraSub - Processed Data Verification")
print("=" * 60)

raw = pd.read_csv(RAW_FILE)
processed = pd.read_csv(PROCESSED_FILE)

# 1. Record count
print("\n--- RECORD COUNT ---")
print(f"Raw       : {len(raw):,}")
print(f"Processed : {len(processed):,}")

assert len(raw) == len(processed), "Record count mismatch!"

# 2. Required columns
print("\n--- COLUMNS ---")

required = [
    "latitude",
    "longitude",
    "vlm_cm_per_year",
    "vlm_mm_per_year",
    "geometry"
]

missing = [c for c in required if c not in processed.columns]

if missing:
    raise ValueError(f"Missing columns: {missing}")

print("All required columns present.")

# 3. Check conversion
print("\n--- UNIT CONVERSION ---")

expected_mm = processed["vlm_cm_per_year"] * 10

conversion_ok = (
    (processed["vlm_mm_per_year"] - expected_mm).abs() < 1e-10
).all()

print(f"cm/year → mm/year conversion: {conversion_ok}")

assert conversion_ok, "VLM unit conversion is incorrect!"

# 4. Check coordinates
print("\n--- COORDINATES ---")

print(
    f"Latitude : "
    f"{processed['latitude'].min():.6f} → "
    f"{processed['latitude'].max():.6f}"
)

print(
    f"Longitude: "
    f"{processed['longitude'].min():.6f} → "
    f"{processed['longitude'].max():.6f}"
)

# 5. Check missing values
print("\n--- MISSING VALUES ---")

missing_values = processed.isnull().sum()

print(missing_values)

assert missing_values.sum() == 0, "Missing values detected!"

# 6. Check geometry
print("\n--- GEOMETRY ---")

invalid_geometry = ~processed["geometry"].str.startswith("POINT(")

print(f"Invalid geometry strings: {invalid_geometry.sum():,}")

assert invalid_geometry.sum() == 0, "Invalid geometry detected!"

print("\n" + "=" * 60)
print("VERIFICATION PASSED")
print("=" * 60)