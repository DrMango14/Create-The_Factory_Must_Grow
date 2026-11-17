# Create: TFMG Community Edition 1.3.4
## Rant:


**Please note that not all bugs are fixed and some new additions are subject to change and should be considered experimental.**

## Contributors:
### Developers:
- @ShallowAssumption
- @wolfieboy09

### Artists:



## Changelog:
### Bug Fixes:
- Segmented Displays
  - now have a back texture
  - Item texture matches block texture
- Large Engines are no longer `IElectric`

### Changes:
- Segmented Displays
  - textures connect to Heavy Machinery Casings.
  - Can be targeted by display links.
  - Can read text from Clipboards.
- Small Engines
  - now display electric information when an electricity-generating upgrade is attached.
- Engine Fuels
  - Added a JEI category for Engine Fuel stats.
  - Engine Fuels are now synchronised to clients.

### API Changes:
- `EngineFuelType.getTypeForFluid` returns `Optional<EngineFuelType>` instead of `Optional<Reference<EngineFuelType>>`
- `EngineFuelType.TAG_CODEC` was added, a codec for the tags if needed 
- `CylinderFuels` can now be modified through `CylinderFuels#withAdditionalFuel`
  - This returns a **new** `CylinderFuel` with the same values, and both the original existing valid fuels and the new ones.
  - The data component attached to an item must be manually replaced

### New Translations:
