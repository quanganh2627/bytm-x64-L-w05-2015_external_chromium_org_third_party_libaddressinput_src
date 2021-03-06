/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.i18n.addressinput;

import com.android.i18n.addressinput.testing.AddressDataMapLoader;

import junit.framework.TestCase;

/**
 * Tests to ensure that {@code AddressVerificationData} can parse all the default data.
 */
public class AddressVerificationDataTest extends TestCase {

    private static final AddressVerificationData ADDRESS_DATA =
            new AddressVerificationData(AddressDataMapLoader.DATA);

    public void testParseAllData() {
        for (String key : ADDRESS_DATA.keys()) {
            AddressVerificationNodeData nodeData = ADDRESS_DATA.get(key);
            assertNotNull(key + " maps to null value.", nodeData);
            assertNotNull("Id is required", nodeData.get(AddressDataKey.ID));
        }
    }

    public void testLoadingCountries() {
        AddressVerificationNodeData nodeData = ADDRESS_DATA.get("data");
        String[] countries = nodeData.get(AddressDataKey.COUNTRIES).split("~");
        assertTrue(countries.length > 0);
    }

    public void testUsData() {
        AddressVerificationNodeData nodeData = ADDRESS_DATA.get("data/US");
        assertEquals("data/US", nodeData.get(AddressDataKey.ID));
        assertNotNull(nodeData.get(AddressDataKey.SUB_KEYS));
        assertNotNull(nodeData.get(AddressDataKey.SUB_NAMES));
        assertEquals("en", nodeData.get(AddressDataKey.LANG));
    }

    public void testCaData() {
        AddressVerificationNodeData nodeData = ADDRESS_DATA.get("data/CA");
        String names = nodeData.get(AddressDataKey.SUB_NAMES);
        String keys = nodeData.get(AddressDataKey.SUB_KEYS);

        assertEquals("data/CA", nodeData.get(AddressDataKey.ID));
        assertEquals("en", nodeData.get(AddressDataKey.LANG));

        assertEquals("AB~BC~MB~NB~NL~NT~NS~NU~ON~PE~QC~SK~YT", keys);
        assertEquals("Alberta~British Columbia~Manitoba~New Brunswick" +
                     "~Newfoundland and Labrador~Northwest Territories~Nova Scotia~Nunavut" +
                     "~Ontario~Prince Edward Island~Quebec~Saskatchewan~Yukon",
                     names);
    }

    public void testCaFrenchData() {
        AddressVerificationNodeData nodeData = ADDRESS_DATA.get("data/CA--fr");
        String names = nodeData.get(AddressDataKey.SUB_NAMES);
        String keys = nodeData.get(AddressDataKey.SUB_KEYS);

        assertEquals("data/CA--fr", nodeData.get(AddressDataKey.ID));
        assertEquals("fr", nodeData.get(AddressDataKey.LANG));
        assertEquals("AB~BC~PE~MB~NB~NS~NU~ON~QC~SK~NL~NT~YT", keys);
        assertTrue(names.contains("Colombie"));
    }

    public void testBackSlashUnEscaped() {
        for (String lookupKey : ADDRESS_DATA.keys()) {
            AddressVerificationNodeData nodeData = ADDRESS_DATA.get(lookupKey);
            for (AddressDataKey dataKey : AddressDataKey.values()) {
                String val = nodeData.get(dataKey);
                if (val != null) {
                    assertFalse("Backslashes need to be unescaped: " + val, val.contains("\\\\"));
                }
            }
        }

        // Spot check.
        assertEquals("Kazhakstan's postal code pattern mismatched", "\\d{6}",
                     ADDRESS_DATA.get("data/KZ").get(AddressDataKey.ZIP));
    }

    public void testExampleData() {
        assertNotNull("Expects example data.", AddressDataMapLoader.DATA.get("examples"));
        assertNotNull("Expects example US address.",
                      AddressDataMapLoader.DATA.get("examples/US/local/en"));
        assertEquals("'examples/TW/local/zh_Hant' and 'examples/TW/local/_default' should " +
                     "return same value.",
                     AddressDataMapLoader.DATA.get("examples/TW/local/zh_Hant"),
                     AddressDataMapLoader.DATA.get("examples/TW/local/_default"));
    }
}
