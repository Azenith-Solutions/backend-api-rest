package com.azenithsolutions.backendapirest.v1.service.Box;

import com.azenithsolutions.backendapirest.v1.model.Box;
import com.azenithsolutions.backendapirest.v1.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoxService {
    @Autowired
    private BoxRepository boxRepository;

    public List<Box> findAllBoxes() {
        return boxRepository.findAll();
    }
}
