package org.haox.kerb.crypto2;

import org.haox.kerb.common.Checksum;
import org.haox.kerb.common.EncryptedData;
import org.haox.kerb.spec.KrbException;

import java.security.GeneralSecurityException;

public final class Des3CbcHmacSha1KdEType extends EType {

    public int eType() {
        return EncryptedData.ETYPE_DES3_CBC_HMAC_SHA1_KD;
    }

    public int minimumPadSize() {
        return 0;
    }

    public int confounderSize() {
        return blockSize();
    }

    public int checksumType() {
        return Checksum.CKSUMTYPE_HMAC_SHA1_DES3_KD;
    }

    public int checksumSize() {
        return Des3.getChecksumLength();
    }

    public int blockSize() {
        return 8;
    }

    public int keySize() {
        return 24; // bytes
    }

    public byte[] encrypt(byte[] data, byte[] key, int usage)
        throws KrbException {
        byte[] ivec = new byte[blockSize()];
        return encrypt(data, key, ivec, usage);
    }

    public byte[] encrypt(byte[] data, byte[] key, byte[] ivec, int usage)
        throws KrbException {
        try {
            return Des3.encrypt(key, usage, ivec, data, 0, data.length);
        } catch (GeneralSecurityException e) {
            KrbException ke = new KrbException(e.getMessage());
            ke.initCause(e);
            throw ke;
        }
    }

    public byte[] decrypt(byte[] cipher, byte[] key, int usage)
        throws KrbException{
        byte[] ivec = new byte[blockSize()];
        return decrypt(cipher, key, ivec, usage);
    }

    public byte[] decrypt(byte[] cipher, byte[] key, byte[] ivec, int usage)
        throws KrbException {
        try {
            return Des3.decrypt(key, usage, ivec, cipher, 0, cipher.length);
        } catch (GeneralSecurityException e) {
            KrbException ke = new KrbException(e.getMessage());
            ke.initCause(e);
            throw ke;
        }
    }

    // Override default, because our decrypted data does not return confounder
    // Should eventually get rid of EType.decryptedData and
    // EncryptedData.decryptedData altogether
    public byte[] decryptedData(byte[] data) {
        return data;
    }
}
