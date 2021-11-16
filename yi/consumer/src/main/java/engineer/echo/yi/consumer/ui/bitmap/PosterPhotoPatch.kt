package engineer.echo.yi.consumer.ui.bitmap

data class PosterPhotoPatch(
    val worldWidth: Int,
    val worldHeight: Int,
    val neighborDistribution: BooleanArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PosterPhotoPatch

        if (worldWidth != other.worldWidth) return false
        if (worldHeight != other.worldHeight) return false
        if (!neighborDistribution.contentEquals(other.neighborDistribution)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = worldWidth
        result = 31 * result + worldHeight
        result = 31 * result + neighborDistribution.contentHashCode()
        return result
    }
}
