
/**
 * Use cases to demonstrate Stego project operations
 * To run this application construct a demo object and 
 * then call the demo methods.
 * 
 * @author  Rachel Cardell-Oliver
 * @version Sep 2019
 */
public class StegoDemo
{
    StegoViewer stegoview; //for displaying results
    StegoCoder mystego; //for calling encrypt and decrypt
    StegoImage cover, secret, message; //example stego images

    /**
     * Constructor for objects of class Demo
     */
    public StegoDemo() {
        stegoview = new StegoViewer(); 
        mystego = new StegoCoder();
    }
    

    /** 
     * Demo for setting up the stego display
     */
    public void demoDisplay() {
        cover = new StegoImage("ProjectImages/covers/image0.png");
        secret = new StegoImage("ProjectImages/secrets/image3.png");
        stegoview.displayImage(cover.getImage(),0);
        stegoview.displayImage(secret.getImage(),1);
        stegoview.displayImage(cover.getImage(),2);
        stegoview.displayImage(secret.getImage(),3);
    }


    /** 
     * Demo for encrypting and decrypting
     */
    public void demoEncodeDecode() {
        cover = new StegoImage("ProjectImages/covers/image1.png");
        secret = new StegoImage("ProjectImages/secrets/image5.png");
        stegoview.displayImage(cover.getImage(),0);
        stegoview.displayImage(secret.getImage(),1);
        //encrypt
        message = mystego.encrypt(cover,secret);
        stegoview.displayImage(message.getImage(),2);
        //decrypt
        secret = mystego.decrypt(message);
        stegoview.displayImage(secret.getImage(),3);

    }

    /** 
     * Demo for learning a secret from a message
     */
    public void demoDecryptA() {
        cover = new StegoImage("ProjectImages/covers/image0.png");
        message = new StegoImage("ProjectImages/messages/image04.png");
        stegoview.displayImage(message.getImage(),2);
        //decode to learn the secret
        secret = mystego.decrypt(message);
        stegoview.displayImage(secret.getImage(),3);
    }

    /** 
     * Another demo for learning a secret from a message
     */
    public void demoDecryptB() {
        cover = new StegoImage("ProjectImages/covers/image2.png");
        message = new StegoImage("ProjectImages/messages/image23.png");
        stegoview.displayImage(message.getImage(),2);
        //decode to learn the secret
        secret = mystego.decrypt(message);
        stegoview.displayImage(secret.getImage(),3);
    }

}
