import pandas as pd
from pathlib import Path

# Project paths
BASE_DIR = Path(__file__).resolve().parent.parent
DATA_FILE = BASE_DIR / "data" / "raw" / "chennai" / "Chennai_VLM_data.csv"

print("=" * 70)
print("InfraSub - Chennai VLM Data Validation")
print("=" * 70)

# --------------------------------------------------
# 1. Load data
# --------------------------------------------------

df = pd.read_csv(DATA_FILE)

print(f"\nTotal records: {len(df):,}")

# --------------------------------------------------
# 2. Required columns
# --------------------------------------------------

required_columns = [
    "Longitude",
    "Latitude",
    "VLM_cm_per_yr"
]

print("\n--- REQUIRED COLUMNS ---")

missing_columns = [
    column for column in required_columns
    if column not in df.columns
]

if missing_columns:
    print("ERROR: Missing columns:")
    for column in missing_columns:
        print(f"  - {column}")
else:
    print("All required columns are present.")

# --------------------------------------------------
# 3. Missing values
# --------------------------------------------------

print("\n--- MISSING VALUES ---")

missing = df[required_columns].isnull().sum()

for column, count in missing.items():
    print(f"{column}: {count:,}")

# --------------------------------------------------
# 4. Duplicate records
# --------------------------------------------------

print("\n--- DUPLICATES ---")

duplicates = df.duplicated().sum()

print(f"Duplicate rows: {duplicates:,}")

# --------------------------------------------------
# 5. Coordinate validation
# --------------------------------------------------

print("\n--- COORDINATE VALIDATION ---")

invalid_longitude = (
    (df["Longitude"] < -180) |
    (df["Longitude"] > 180)
).sum()

invalid_latitude = (
    (df["Latitude"] < -90) |
    (df["Latitude"] > 90)
).sum()

print(f"Invalid longitude values: {invalid_longitude:,}")
print(f"Invalid latitude values : {invalid_latitude:,}")

# --------------------------------------------------
# 6. Chennai geographic sanity check
# --------------------------------------------------

print("\n--- GEOGRAPHIC SANITY CHECK ---")

print(
    f"Longitude range: "
    f"{df['Longitude'].min():.6f} → "
    f"{df['Longitude'].max():.6f}"
)

print(
    f"Latitude range : "
    f"{df['Latitude'].min():.6f} → "
    f"{df['Latitude'].max():.6f}"
)

# --------------------------------------------------
# 7. VLM validation
# --------------------------------------------------

print("\n--- VLM VALIDATION ---")

invalid_vlm = df["VLM_cm_per_yr"].isnull().sum()

print(f"Missing VLM values: {invalid_vlm:,}")

print(
    f"Minimum VLM: "
    f"{df['VLM_cm_per_yr'].min():.6f} cm/year"
)

print(
    f"Maximum VLM: "
    f"{df['VLM_cm_per_yr'].max():.6f} cm/year"
)

print(
    f"Mean VLM: "
    f"{df['VLM_cm_per_yr'].mean():.6f} cm/year"
)

print(
    f"Median VLM: "
    f"{df['VLM_cm_per_yr'].median():.6f} cm/year"
)

# --------------------------------------------------
# 8. Extreme values
# --------------------------------------------------

print("\n--- EXTREME VALUES ---")

print("\nMost negative VLM:")
print(
    df.nsmallest(5, "VLM_cm_per_yr")[
        ["Longitude", "Latitude", "VLM_cm_per_yr"]
    ]
)

print("\nMost positive VLM:")
print(
    df.nlargest(5, "VLM_cm_per_yr")[
        ["Longitude", "Latitude", "VLM_cm_per_yr"]
    ]
)

# --------------------------------------------------
# 9. Overall validation result
# --------------------------------------------------

print("\n--- VALIDATION RESULT ---")

problems = (
    len(missing_columns)
    + missing.sum()
    + duplicates
    + invalid_longitude
    + invalid_latitude
)

if problems == 0:
    print("PASS: No basic data-quality problems detected.")
else:
    print(f"WARNING: {problems:,} potential data-quality issues detected.")

print("\n" + "=" * 70)
print("Validation completed.")
print("=" * 70)