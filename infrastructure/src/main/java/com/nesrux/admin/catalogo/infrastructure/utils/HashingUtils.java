package com.nesrux.admin.catalogo.infrastructure.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public final class HashingUtils {
    private static final HashFunction CHECKSUM = Hashing.crc32();

    private HashingUtils() {
    }

    public static String checkSum(final byte[] content) {
        return CHECKSUM.hashBytes(content).toString();
    }


}
