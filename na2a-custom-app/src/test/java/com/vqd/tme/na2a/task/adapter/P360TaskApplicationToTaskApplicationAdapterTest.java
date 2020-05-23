package com.vqd.tme.na2a.task.adapter;

import com.vqd.tme.na2a.model.p360.P360Application;
import com.vqd.tme.na2a.task.model.TaskApplication;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class P360TaskApplicationToTaskApplicationAdapterTest {

    private P360TaskApplicationToTaskApplicationAdapter testable;

    @Before
    public void setUp() throws Exception {
        testable = new P360TaskApplicationToTaskApplicationAdapter();
    }

    @Test
    public void convert() {
        P360Application p360Application = P360Application.builder()
                .id("17236")
                .shortDescription("Brand: TOYOTA, Model: Camry, FMC/MMC: Camry 2001\\\\08 (FMC), ashtray [022]: fr1(with lamp)")
                .build();

        TaskApplication result = testable.convert(p360Application);

        assertNotNull(result);
        assertEquals("17236", result.getId());
        assertEquals("Brand: TOYOTA, Model: Camry, FMC/MMC: Camry 2001\\\\08 (FMC), ashtray [022]: fr1(with lamp)", result.getName());
    }
}
