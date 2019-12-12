package com.vqd.tme.na2a.service;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.adapter.RowToKeyValueAdapter;
import com.vqd.tme.na2a.data.StructureGroupRepository;
import com.vqd.tme.na2a.model.ResponseAccessoryColumn;
import com.vqd.tme.na2a.model.ResponseAccessoryTab;
import com.vqd.tme.na2a.model.ResponseAccessoryTable;
import com.vqd.tme.na2a.model.p360.P360Structure;
import com.vqd.tme.na2a.model.p360.P360StructureGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccessoryTableService {

    private final StructureService structureService;
    private final StructureGroupRepository structureGroupRepository;
    private final RowToKeyValueAdapter keyValueAdapter;

    public ResponseAccessoryTable getAccessoryTable() {
        ResponseAccessoryTable table = ResponseAccessoryTable.builder().build();
        P360Structure productStructure = structureService.getProductStructure();
        List<P360StructureGroup> structureGroups = structureGroupRepository.findByStructure(productStructure);
        table.getTabs().addAll(buildTabs(structureGroups, productStructure.getIdentifier()));
        return table;
    }

    private List<ResponseAccessoryTab> buildTabs(List<P360StructureGroup> structureGroups, String structureIdentifier) {
        List<P360StructureGroup> tabs = getChilderen(structureGroups, structureIdentifier);

        return tabs.stream()
                .map(sg -> ResponseAccessoryTab.builder()
                        .name(sg.getName())
                        .columns(buildTabColumns(structureGroups, sg.getIdentifier()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<ResponseAccessoryColumn> buildTabColumns(List<P360StructureGroup> structureGroups, String structureIdentifier) {
        List<P360StructureGroup> columns = getChilderen(structureGroups, structureIdentifier);

        return columns.stream()
                .map(sg -> ResponseAccessoryColumn.builder()
                        .headerName(sg.getName())
                        .availableItems(buildColumnItems(structureGroups, sg.getIdentifier()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<P360StructureGroup> getChilderen(List<P360StructureGroup> structureGroups, String structureIdentifier) {
        return structureGroups.stream()
                .filter(sg -> sg.getParentIdentifier() != null && sg.getParentIdentifier().equalsIgnoreCase(structureIdentifier))
                .collect(Collectors.toList());
    }

    private List<String> buildColumnItems(List<P360StructureGroup> structureGroups, String structureIdentifier) {
        List<P360StructureGroup> items = getChilderen(structureGroups, structureIdentifier);

        return items.stream()
                .map(P360StructureGroup::getName)
                .collect(Collectors.toList());
    }

}
