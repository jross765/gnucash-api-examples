# Major Changes

## V. 1.8 &rarr; 1.9
* Followed the deprecation of `FixedPointNumber` in the modules
  * "API (Core)", V. 1.9,
  * "Specialized Entitites", V. 0.4 and
  * "API Extended", V. 1.9:

  Changed various implementations so that it's not used any more
  and removed the FP-variant introduced in previous release.

* New package `overall.write` with new tool `ThinOutGCshFile`.

## V. 1.7.1 &rarr; 1.8
* Changed package structure so that module dependencies are clearer.

* Adapted to underlying modules' versions.

* `GenDepotTrx`: Now we have two variants: [BF|FP]
