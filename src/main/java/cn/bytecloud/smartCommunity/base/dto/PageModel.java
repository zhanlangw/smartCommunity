package cn.bytecloud.smartCommunity.base.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class PageModel<T> {
    private long totalCount;
    private List<T> value;

    public PageModel() {
    }

    public PageModel(long totalCount, List<T> value) {
        this.totalCount = totalCount;
        this.value = value;
    }

    public PageModel(long totalCount) {
        this.totalCount = totalCount;
        this.value = new ArrayList<>();
    }

    public PageModel(List<T> value) {
        this.value = value;
    }

    public void addToValue(T item) {
        if (value == null) {
            value = new ArrayList<>();
        }
        value.add(item);
    }

    public static PageModel<HashMap> isEmpty() {
        return new PageModel<HashMap>(0, new ArrayList<HashMap>());
    }
}