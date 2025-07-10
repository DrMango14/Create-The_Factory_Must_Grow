# Changelog

## Additions
### Items:
- Lithium now has a Crushed Raw Ore item.

### Recipes:
- Added crushing recipes for Crushed Raw Lithium.
- Added Smelting & Blasting recipes for Crushed Raw Lithium.

### Tags:
- Items such as ingots and nuggets are now tagged with their group tags (e.g. `forge:ingots`).

## Changes
### Items:
- Pipes now properly display in the "Create: TFMG Building Blocks" Creative Tab.

### Vat:
- The Vat now also saves the position of attachments instead of just the operation ID.
- Vat Attachments can now define situations where they aren't operational and cannot be used for recipes.
- Attachments that are not operational now display in the tooltip of the Vat but specify that they are not operational.
- The Electrode Holder now always provides their operation id.
- The Industrial Mixer now requires a minimum speed input of "medium".
- The Vat JEI tab now combines ingredients of the same input into one slot to reduce clutter.
- The Vat JEI tab now properly displays if a recipe requires a Firebrick Lined Vat.
- The Vat JEI tab now displays if a recipe requires a Cast Iron Vat.
- The Vat JEI tab now properly displays if a recipe requires a centrifuge.
- The Firebrick Lined Vat item model now uses its single windowless model instead of the windowed model.
- The Vat can now be multi-placed like Fluid Tanks and Item Vaults.

### Blocks:
- The Converter now uses its own config instead of the config for the Accumulator.


### Recipes:
- The recipe for the Coated Circuit Board is now Deploying instead of Item Application.
- TFMGCompactingRecipeGen no longer extends TFMGPressingRecipeGen, which resulted in pressing recipes also registering as compacting recipes.
- TFMG's Mechanical Crafting recipes have been moved to TFMG's namespace instead of Create's namespace.

## Bug Fixes
- Fixed a bug where the Vat would only output the first fluid result of a recipe.
- Fixed a bug where some recipe advancement files would be empty, causing logging errors.
- Fixed a bug where dropping the Advanced Potato Cannon would cause the game to crash.