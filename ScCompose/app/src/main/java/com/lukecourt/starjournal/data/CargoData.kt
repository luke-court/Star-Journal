package com.lukecourt.starjournal.data

data class CargoData(
    val cargoName: String,
    val cargoCode: String,
    val cargoType: String,
    val isAvailable: Boolean,
    val isAvailableLive: Boolean,
    val isVisible: Boolean,
    val isExtractable: Boolean,
    val isMineral: Boolean,
    val isRaw: Boolean,
    val isPure: Boolean,
    val isRefined: Boolean,
    val isRefinable: Boolean,
    val isHarvestable: Boolean,
    val isBuyable: Boolean,
    val isSellable: Boolean,
    val isTemp: Boolean,
    val isVolatileQt: Boolean,
    val isVolatileTime: Boolean,
    val isInert: Boolean,
    val isExplosive: Boolean,
    val isFuel: Boolean,
    val isBuggy: Boolean
)