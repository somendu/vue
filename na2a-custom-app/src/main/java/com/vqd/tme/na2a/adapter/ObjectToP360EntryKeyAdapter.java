package com.vqd.tme.na2a.adapter;

import com.vqd.tme.na2a.model.p360.P360EntryKey;
import com.vqd.tme.na2a.support.CastUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ObjectToP360EntryKeyAdapter implements Adapter<Object, P360EntryKey> {

    @Override
    public P360EntryKey convert(Object obj) {
        if(obj instanceof Map) {
            Map<String, String> entry = CastUtils.asMap(obj);
            P360EntryKey key = new P360EntryKey();
            key.setId(entry.get("id"));
            key.setLabel(entry.get("label"));
            return key;
        }
        if (obj instanceof P360EntryKey) {
            return (P360EntryKey) obj;
        }
        return null;
    }
}
