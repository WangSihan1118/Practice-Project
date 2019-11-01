
/**
 * Stego Coder.
 * utility class
 * 
 * @Wang Sihan
 * 9/10/2019
 */
public  final class StegoCoder
{
    // utility class NO FILED VARIBLES

    public StegoCoder()
    {
        // utility class constructor body should be EMPTY
    }

    /**
     * Class methods
     */
    public static StegoImage encrypt(StegoImage cover, StegoImage secret)
    {
        cover.clearLowBit();
        secret.setZeroOne();
        cover.mergeImage(secret);
        return cover;
    }
    
    public static StegoImage decrypt(StegoImage message)
    {
        message.setToLowBit();
        message.setBlackWhite();
        return message;
    }
}
