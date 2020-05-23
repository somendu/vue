package com.vqd.tme.na2a.task.service.impl;

import com.vqd.tme.na2a.model.TreeNode;
import com.vqd.tme.na2a.service.RefTableService;
import com.vqd.tme.na2a.task.service.TaskRefTableService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TaskRefTableServiceImpl implements TaskRefTableService {

    private RefTableService refTableService;

    public TaskRefTableServiceImpl(RefTableService refTableService) {
        this.refTableService = refTableService;
    }

    @Override
    public Collection<TreeNode> getSsns(String brand, String model) {

        Collection<TreeNode> projects = refTableService.listProjects(brand, model);

        for (TreeNode treeNode : projects) {
            String project = treeNode.getKey();

            Collection<TreeNode> child = refTableService.listSsns(brand, model, project);

            treeNode.setChildren(child);
        }

        return projects;
    }
}
