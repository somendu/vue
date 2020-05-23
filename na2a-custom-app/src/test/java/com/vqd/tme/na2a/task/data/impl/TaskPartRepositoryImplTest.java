package com.vqd.tme.na2a.task.data.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vqd.tme.na2a.partlinking.model.Part;
import com.vqd.tme.na2a.partlinking.model.VariantReference;
import com.vqd.tme.na2a.partlinking.persistence.Parts;
import com.vqd.tme.na2a.partlinking.persistence.VariantReferences;
import com.vqd.tme.na2a.partlinking.persistence.impl.P360Parts;
import com.vqd.tme.na2a.task.data.TaskPartRepository;
import com.vqd.tme.na2a.task.model.TaskPart;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskPartRepositoryImplTest {

    public static final String APPLICATION_ID = "17236";
    private Parts p360PartsRepo;
    private VariantReferences variantReferences;

    private TaskPartRepository testable;

    @Before
    public void setUp() throws Exception {
        p360PartsRepo = mock(P360Parts.class);
        variantReferences = mock(VariantReferences.class);

        testable = new TaskPartRepositoryImpl(p360PartsRepo, variantReferences);
    }

    @Test
    public void findByApplicationId() {

        List<VariantReference> links = Lists.newArrayList(
                new VariantReference().setArticleCode("12345").setQuantity(new BigDecimal(13)).setType("12")
        );

        Set<String> occurringPartIds = Sets.newHashSet();
        occurringPartIds.add("12345");

        Part part = new Part();
        part.setPartNumber("12345");
        part.setPartName("Spare fitting kit for TPMS");

        Map<String, Part> p360Parts = Maps.newHashMap();
        p360Parts.put("12345", part);

        when(variantReferences.list(APPLICATION_ID)).thenReturn(links);
        when(p360PartsRepo.get(occurringPartIds)).thenReturn(p360Parts);

        List<TaskPart> result = testable.findByApplicationId(APPLICATION_ID);

        assertEquals(1, result.size());
        assertEquals("12345", result.get(0).getPartNumber());
        assertEquals("Spare fitting kit for TPMS", result.get(0).getPartName());
        assertEquals(new BigDecimal(13), result.get(0).getQuantity());
        assertEquals("12", result.get(0).getType());
    }
}
