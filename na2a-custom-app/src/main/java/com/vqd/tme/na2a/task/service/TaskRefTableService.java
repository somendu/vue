package com.vqd.tme.na2a.task.service;

import com.vqd.tme.na2a.model.TreeNode;

import java.util.Collection;

public interface TaskRefTableService {

    Collection<TreeNode> getSsns(String brand, String model);
}
