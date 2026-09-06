import pandas as pd
from pathlib import Path

# Paths
BASE_DIR = Path(__file__).resolve().parent.parent
DATA_FILE = BASE_DIR / "data" / "raw" / "chennai" / "Chennai_VLM_data.csv"

print("=" * 60)
print("InfraSub - Chennai VLM Data Inspection")
print("=" * 60)

# Check whether file exists
if not DATA_FILE.exists():
    print(f"ERROR: File not found:")
    print(DATA_FILE)
    raise SystemExit(1)

print(f"\nReading:")
print(DATA_FILE)

# Load data
df = pd.read_csv(DATA_FILE)

# Basic information
print("\n--- BASIC INFORMATION ---")
print(f"Rows    : {len(df):,}")
print(f"Columns : {len(df.columns)}")

print("\nColumns:")
for column in df.columns:
    print(f"  - {column}")

# First few records
print("\n--- FIRST 5 RECORDS ---")
print(df.head())

# Data types
print("\n--- DATA TYPES ---")
print(df.dtypes)

# Missing values
print("\n--- MISSING VALUES ---")
print(df.isnull().sum())

# Duplicate rows
print("\n--- DUPLICATES ---")
print(f"Duplicate rows: {df.duplicated().sum():,}")

# Statistical summary
print("\n--- STATISTICAL SUMMARY ---")
print(df.describe())

# Geographic range
print("\n--- GEOGRAPHIC RANGE ---")

print(f"Longitude:")
print(f"  Minimum: {df['Longitude'].min()}")
print(f"  Maximum: {df['Longitude'].max()}")

print(f"Latitude:")
print(f"  Minimum: {df['Latitude'].min()}")
print(f"  Maximum: {df['Latitude'].max()}")

# VLM statistics
print("\n--- VLM STATISTICS ---")

print(f"Minimum : {df['VLM_cm_per_yr'].min():.6f} cm/year")
print(f"Maximum : {df['VLM_cm_per_yr'].max():.6f} cm/year")
print(f"Mean    : {df['VLM_cm_per_yr'].mean():.6f} cm/year")
print(f"Median  : {df['VLM_cm_per_yr'].median():.6f} cm/year")

# Positive / negative movement
negative = (df["VLM_cm_per_yr"] < 0).sum()
positive = (df["VLM_cm_per_yr"] > 0).sum()
zero = (df["VLM_cm_per_yr"] == 0).sum()

print("\n--- VLM DIRECTION ---")
print(f"Negative VLM : {negative:,}")
print(f"Positive VLM : {positive:,}")
print(f"Zero VLM     : {zero:,}")

print("\n" + "=" * 60)
print("Inspection completed.")
print("=" * 60)