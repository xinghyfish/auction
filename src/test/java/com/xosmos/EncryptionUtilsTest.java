package com.xosmos;

import com.xosmos.utils.EncryptUtils;
import org.junit.jupiter.api.Test;

public class EncryptionUtilsTest {

    @Test
    public void testMD5() {
        System.out.println(EncryptUtils.encrypt("admin0001"));
    }
}
