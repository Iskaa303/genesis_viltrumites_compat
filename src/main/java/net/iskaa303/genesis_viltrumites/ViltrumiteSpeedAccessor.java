package net.iskaa303.genesis_viltrumites;

/**
 * Accessor for the per-player speed multiplier field added by
 * {@link net.iskaa303.genesis_viltrumites.mixin.ViltrumiteFlightSpeedMixin}.
 * Cast a Player to this interface to call the getter/setter.
 */
public interface ViltrumiteSpeedAccessor {
    float getSpeedMultiplier();
    void setSpeedMultiplier(float multiplier);
}
