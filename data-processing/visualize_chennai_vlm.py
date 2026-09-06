import pandas as pd
import matplotlib.pyplot as plt
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent

DATA_FILE = (
    BASE_DIR
    / "data"
    / "raw"
    / "chennai"
    / "Chennai_VLM_data.csv"
)

OUTPUT_DIR = BASE_DIR / "data-processing" / "output"
OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

OUTPUT_FILE = OUTPUT_DIR / "chennai_vlm_map.png"

# Load raw data
df = pd.read_csv(DATA_FILE)

# Plot VLM measurements
plt.figure(figsize=(10, 8))

scatter = plt.scatter(
    df["Longitude"],
    df["Latitude"],
    c=df["VLM_cm_per_yr"],
    s=2,
    alpha=0.7
)

plt.colorbar(
    scatter,
    label="VLM (cm/year)"
)

plt.xlabel("Longitude")
plt.ylabel("Latitude")

plt.title("Chennai Vertical Land Motion")

plt.tight_layout()

plt.savefig(
    OUTPUT_FILE,
    dpi=300,
    bbox_inches="tight"
)

plt.show()

print(f"\nMap saved to:")
print(OUTPUT_FILE)