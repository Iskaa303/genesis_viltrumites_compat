package net.iskaa303.genesis_viltrumites;

import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Viltrumites.MOD_ID)
public final class Viltrumites {
    public static final String MOD_ID = "genesis_viltrumites";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Viltrumites() {
        LOGGER.info("Genesis: Viltrumites loaded.");
    }
}
