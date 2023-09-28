package com.itmo.web.lab3;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class ResultService implements Serializable {
    @Inject
    private ResultRepository resultRepository;

    public void append(Result result) {
        resultRepository.insert(result);
    }

    public List<Result> getResultList() {
        return resultRepository.fetchAllResults();
    }

    public Result getLastResult() {
        return resultRepository.fetchLastResult();
    }

}
