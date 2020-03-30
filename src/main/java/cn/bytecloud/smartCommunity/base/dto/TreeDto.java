package cn.bytecloud.smartCommunity.base.dto;

import lombok.Data;

@Data
public class TreeDto {
    private String uid;
    private String title;
    private TreeType type;
    private boolean isLeaf;
    private String key;

    public void setIsLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

}
