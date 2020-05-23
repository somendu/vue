package com.vqd.tme.na2a.task.support;

import com.vqd.tme.na2a.config.InformaticaPimProperties;
import com.vqd.tme.na2a.p360.GetResponse;
import com.vqd.tme.na2a.task.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskResolver {

    final private InformaticaPimProperties pimProperties;
    final private RestTemplate rest;

    public Task doTask(GetResponse taskResponse) {

        List<Object> values = taskResponse.getRows().get(0).getValues();

        LinkedHashMap<String, String> creationUser = (LinkedHashMap<String, String>) values.get(4);

        String creationUserLoginName = getUserLoginName(creationUser.get("id"));

        return new Task(values.get(0).toString(), values.get(1).toString(), values.get(2).toString(),
                values.get(3).toString(), creationUserLoginName, values.get(5).toString(), values.get(6).toString(),
                false); // todo Pack is not yet in fieldslist. So not obtainable. De-hardcode when available.


    }

    private String getUserLoginName(String id) {
        GetResponse res = rest.getForObject(pimProperties.getServer() +
                        "/rest/V2.0/list/User/byItems" +
                        "?items={id}" +
                        "&fields=User.LoginName",
                GetResponse.class,
                id);

        return res.getRows().get(0).getValues().get(0).toString();
    }
}
