package com.vqd.tme.na2a.model.tme;

import lombok.Data;

import java.util.List;

@Data
public class TmePartsWrapper {
    private Integer total;
    private Integer count;
    private Embedded _embedded;

    public List<TmePart> getParts() {
        return _embedded.parts;
    }

    @Data
    static class Embedded {
        private List<TmePart> parts;
    }
}
