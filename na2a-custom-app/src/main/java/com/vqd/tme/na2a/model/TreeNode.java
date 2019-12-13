package com.vqd.tme.na2a.model;

import java.util.Collection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain=true)
public class TreeNode {
  private String id;
  private String key;
  private String value;
  private Collection<String> katashiki;
  private Collection<TreeNode> children;

  public TreeNode(TreeNode other) {
    id = other.getId();
    key = other.getKey();
    value = other.getValue();
    katashiki = other.getKatashiki();
    children = other.getChildren();
  }
}
