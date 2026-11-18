# Task 003.5-B1 — motion observation, candidate physics and landing

This branch develops only B-OBS → B-MOTION → B-LANDING for the three core LivingEntity materials. Independent base: merged main79aac86; observation-only checkpointda60f10 contains no physics changes. No boat, geometry, effects, tools, worldgen, AI textures or old private assets are included.

Read [BASELINE_TRACES](BASELINE_TRACES.md), [MOTION_CONTRACT](MOTION_CONTRACT.md), [LANDING_CONTRACT](LANDING_CONTRACT.md), [TEST_RESULTS](TEST_RESULTS.md), [MANUAL_TEST_GUIDE](MANUAL_TEST_GUIDE.md) and [KNOWN_GAPS](KNOWN_GAPS.md). Parameters remain candidate values pending user acceptance. Implemented code, automated tests and actual client runs are distinct evidence categories.

Build on Java17: `./gradlew --no-daemon --console=plain test runGameTestServer build`. The original18JUnit and15GameTest assertions are retained. New tests separately cover entry penetration, actual mob displacement, finite bounds, material curves, landing predicates, lifecycle and delayed-step budget. Diagnostics are explicit operator commands and default off.
