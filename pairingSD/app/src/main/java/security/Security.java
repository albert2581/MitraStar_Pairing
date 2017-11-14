package security;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by kuan-ting on 2015/11/10.
 */
public class Security {
    private static final IvParameterSpec iv = new IvParameterSpec(new byte[]{48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48});
    private Key key;
    private Cipher cipher;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public Security(final String key) {
        byte[] data = Base64.decode(key, 0);
        this.key = new SecretKeySpec(data, "AES");

        try {
            cipher = Cipher.getInstance("aes/cbc/NoPadding");
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private static byte[] getHash(final String algorithm, final byte[] data) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(data);
            return digest.digest();
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public byte[] encrypt(final byte[] data) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            int textLength = data.length;
            System.out.println(textLength);
            int numberOfModuleLength = 16 - (textLength % 16);
            byte[] newData = new byte[textLength + numberOfModuleLength + 16];
            String textlengthString = Integer.toString(textLength);
            int digitOftextlength = textlengthString.length();
            // add textLength
            for (int i=0; i<digitOftextlength; i++) {
                newData[i] = textlengthString.getBytes()[i];
            }
            System.arraycopy(data, 0, newData, 16, data.length);
            byte[] encryptData = cipher.doFinal(newData);
            System.out.println(encryptData.length);
            return Base64.encode(encryptData, 0);
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String encryptFromString(String inputString) throws UnsupportedEncodingException {
        return new String(encrypt(inputString.getBytes()),"Big5");
    }

    public void encryptFromFile(String inputFileName, String saveFileName) throws IOException {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/" + inputFileName);

        int size = (int) dir.length();
        byte[] data = new byte[size];
        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(dir));
        buf.read(data, 0, data.length);
        buf.close();
        saveByteToFile(saveFileName, encrypt(data));
    }

    public byte[] decrypt(String inputString) {
        try {
            byte[] data = Base64.decode(inputString, 0);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decryptData = cipher.doFinal(data);
            byte[] decryptLen = new byte[16];
            System.arraycopy(decryptData,  0, decryptLen, 0, 16);
            String decryptionString = new String(decryptLen);
            String abc = decryptionString.split("\0")[0];
            byte[] newByte = new byte[Integer.parseInt(abc)];
            System.arraycopy(decryptData,  16, newByte, 0, Integer.parseInt(abc));
            return newByte;
        } catch (final Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String decryptionFromString(String inputString) throws UnsupportedEncodingException {
        return new String(decrypt(inputString),"Big5");
    }

    public void decryptionFromFile(String fileName, String saveFileName) throws IOException {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(fileName);

        int size = (int) dir.length();
        byte[] data = new byte[size];
        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(dir));
        buf.read(data, 0, data.length);
        buf.close();
        byte[] decryptionData = decrypt(new String(data,"Big5"));
        saveByteToFile(saveFileName, decryptionData);
    }

    public void saveByteToFile(String filename, byte[] saveData) throws IOException {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(filename);

        FileOutputStream fos = new FileOutputStream(dir);
        fos.write(saveData);
        fos.close();
    }
}
