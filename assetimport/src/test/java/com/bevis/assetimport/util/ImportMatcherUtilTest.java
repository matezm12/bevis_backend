package com.bevis.assetimport.util;


import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@Ignore
public class ImportMatcherUtilTest {

    private final String URL = "https://www.google.com/";
    private final String SKU = "theo-ag-v2";
    private final String SKU2 = "theo-ag";
    private final String BTC = "17FDm12b7LeSeghv33H3PTvJ5ruiZ1h35J";
    private final String ETH = "0xbd1414cef4302db34dbbb8cf7a2a13fa138d08d0";
    private final String UPC = "680666485016";
    private final String BAD_UPC = "6806664850160";

    @Test
    public void testMatchUrl() {
        assertTrue("Test matchUrl?", ImportMatcherUtil.matchUrl(URL));
        assertFalse("Test matchUrl?", ImportMatcherUtil.matchUrl(SKU));
        assertFalse("Test matchUrl?", ImportMatcherUtil.matchUrl(SKU2));
        assertFalse("Test matchUrl?", ImportMatcherUtil.matchUrl(BTC));
        assertFalse("Test matchUrl?", ImportMatcherUtil.matchUrl(ETH));
        assertFalse("Test matchUrl?", ImportMatcherUtil.matchUrl(UPC));
        assertFalse("Test matchUrl?", ImportMatcherUtil.matchUrl(BAD_UPC));
    }

    @Test
    public void testMatchSku() {
        assertTrue("Test matchSku?", ImportMatcherUtil.matchSku(SKU));
        assertTrue("Test matchSku?", ImportMatcherUtil.matchSku(SKU2));
        assertFalse("Test matchSku?", ImportMatcherUtil.matchSku(URL));
        assertFalse("Test matchSku?", ImportMatcherUtil.matchSku(BTC));
        assertFalse("Test matchSku?", ImportMatcherUtil.matchSku(ETH));
        assertFalse("Test matchSku?", ImportMatcherUtil.matchSku(UPC));
        assertFalse("Test matchSku?", ImportMatcherUtil.matchSku(BAD_UPC));
    }

    @Test
    public void testMatchBTCPublicKey() {
        assertTrue("Test matchPublicKey?", ImportMatcherUtil.matchPublicKey(BTC));
        assertTrue("Test matchPublicKey?", ImportMatcherUtil.matchPublicKey(ETH));
        assertFalse("Test matchPublicKey?", ImportMatcherUtil.matchPublicKey(SKU));
        assertFalse("Test matchPublicKey?", ImportMatcherUtil.matchPublicKey(SKU2));
        assertFalse("Test matchPublicKey?", ImportMatcherUtil.matchPublicKey(URL));
        assertFalse("Test matchPublicKey?", ImportMatcherUtil.matchPublicKey(UPC));
        assertFalse("Test matchPublicKey?", ImportMatcherUtil.matchPublicKey(BAD_UPC));
    }

    @Test
    public void testMatchUpc() {
        assertTrue("Test matchUpc?", ImportMatcherUtil.matchUpc(UPC));
        assertFalse("Test matchUpc?", ImportMatcherUtil.matchUpc(ETH));
        assertFalse("Test matchUpc?", ImportMatcherUtil.matchUpc(BTC));
        assertFalse("Test matchUpc?", ImportMatcherUtil.matchUpc(URL));
        assertFalse("Test matchUpc?", ImportMatcherUtil.matchUpc(BAD_UPC));
        assertFalse("Test matchUpc?", ImportMatcherUtil.matchUpc(SKU));
        assertFalse("Test matchUpc?", ImportMatcherUtil.matchUpc(SKU2));
    }
}
