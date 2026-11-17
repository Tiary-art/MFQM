#!/usr/bin/env python3
"""Opt-in private pack. Never modifies the input JAR or copies into src/."""
import argparse
import csv
import hashlib
import json
from pathlib import Path
import struct
import zipfile

ROOT = Path(__file__).resolve().parents[1]
EXPECTED = "2a0809e13f2267b0eb981606164e863bc52bcdc326120b0ca27fd845a2f0db6b"
parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument("--jar", required=True, type=Path)
parser.add_argument("--texture", default="assets/morefunquicksandmod/textures/blocks/quicksand.png")
args = parser.parse_args()
jar = args.jar.resolve()
before = hashlib.sha256(jar.read_bytes()).hexdigest()
if before != EXPECTED:
    parser.error("Input must match the researched original JAR SHA-256")
with (ROOT / "docs/texture_mapping_draft.csv").open(encoding="utf-8-sig") as handle:
    matches = [row for row in csv.DictReader(handle) if row["original_path"] == args.texture]
if len(matches) != 1:
    parser.error("Texture must occur exactly once in the mapping draft")
row = matches[0]
output = ROOT / "private-resources/legacy-test-pack.zip"
output.parent.mkdir(exist_ok=True)
with zipfile.ZipFile(jar) as source:
    image = source.read(row["original_path"])
    width, height = struct.unpack(">II", image[16:24])
    target = row["proposed_path"]
    with zipfile.ZipFile(output, "w", zipfile.ZIP_DEFLATED) as pack:
        pack.writestr("pack.mcmeta", json.dumps({"pack": {"pack_format": 15,
            "description": "PRIVATE legacy MFQM texture experiment - do not redistribute"}}))
        pack.writestr(target, image)
        if row["mcmeta_exists"] == "true":
            pack.writestr(target + ".mcmeta", source.read(row["original_mcmeta_path"]))
        texture_id = "mfqm:" + target.split("/textures/", 1)[1][:-4]
        pack.writestr("assets/mfqm/models/block/dev_test_block.json", json.dumps({
            "parent": "minecraft:block/cube_all", "textures": {"all": texture_id}}))
assert hashlib.sha256(jar.read_bytes()).hexdigest() == before
with zipfile.ZipFile(output) as pack:
    assert pack.read(target) == image
print(json.dumps({"private_output": str(output), "original_jar_sha256": before,
    "texture": row["original_path"], "dimensions": [width, height],
    "image_bytes_unchanged": True, "mcmeta_copied_verbatim": row["mcmeta_exists"] == "true",
    "warning": "Draft mapping only. Unusual layouts, including 32x304, require manual testing; no automatic cropping."}, indent=2))
