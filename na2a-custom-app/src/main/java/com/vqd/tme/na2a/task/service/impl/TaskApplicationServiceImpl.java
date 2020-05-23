package com.vqd.tme.na2a.task.service.impl;

import java.util.List;

import com.vqd.tme.na2a.model.Product;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.vqd.tme.na2a.task.adapter.P360TaskApplicationToTaskApplicationAdapter;
import com.vqd.tme.na2a.task.adapter.P360TaskProductToTaskProductAdapter;
import com.vqd.tme.na2a.data.ApplicationRepository;
import com.vqd.tme.na2a.data.CommodityRepository;
import com.vqd.tme.na2a.task.data.TaskPartRepository;
import com.vqd.tme.na2a.data.ProductRepository;
import com.vqd.tme.na2a.task.model.TaskApplication;
import com.vqd.tme.na2a.task.model.TaskApplicationResponse;
import com.vqd.tme.na2a.task.model.TaskCommodity;
import com.vqd.tme.na2a.model.p360.P360Application;
import com.vqd.tme.na2a.task.service.TaskApplicationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskApplicationServiceImpl implements TaskApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ProductRepository productRepository;
    private final CommodityRepository commodityRepository;
    private final TaskPartRepository taskPartRepository;

    private final P360TaskApplicationToTaskApplicationAdapter applicationAdapter;
    private final P360TaskProductToTaskProductAdapter productAdapter;

    @Override
    public List<TaskApplicationResponse> get(String brand, String model, String project) {

        List<P360Application> p360Applications = applicationRepository.findByBrandAndModelAndProject(brand, model, project);
        List<TaskApplicationResponse> responses = Lists.newArrayList();

        p360Applications.forEach(p360Application -> responses.add(TaskApplicationResponse.builder()
                .taskApplication(applicationAdapter.convert(p360Application))
                .build()));

        responses.forEach(response -> {
        	TaskApplication application = response.getTaskApplication();
            Product product = productRepository.findByApplicationId(application.getId());
            application.setProduct(productAdapter.convert(product))
        				.setCommodity(TaskCommodity.builder().value(commodityRepository.findByApplicationId(application.getId())).build())
        				.setParts(taskPartRepository.findByApplicationId(application.getId()));

        });

        return responses;
    }
}
