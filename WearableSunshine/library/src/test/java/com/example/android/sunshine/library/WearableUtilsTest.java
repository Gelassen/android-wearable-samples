package com.example.android.sunshine.library;


import android.net.Uri;

import com.example.android.sunshine.library.utils.DataWearInteractor;

import org.junit.Test;

import static org.junit.Assert.*;

public class WearableUtilsTest {

    @Test
    public void testUri() throws Exception {
        final String nodeId = "1000";
        final String resourcePath = Params.WEATHER;
        Uri wearableResourceUri = DataWearInteractor.getUri(nodeId, resourcePath);
        assertEquals("Uri should be equal", "wear://" + nodeId + resourcePath, wearableResourceUri.buildUpon().toString());
    }
}
