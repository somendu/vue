package com.vqd.tme.na2a.adapter;

public interface Adapter<SOURCE, TARGET> {

    TARGET convert(SOURCE source);
}
