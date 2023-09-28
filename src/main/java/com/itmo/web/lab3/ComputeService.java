package com.itmo.web.lab3;

import com.itmo.web.lab3.check.AbstractChecker;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Named
@SessionScoped
public class ComputeService implements Serializable {
    @Inject
    private ResultService resultService;
    private List<AbstractChecker> checkerList;
    private Double currentX;
    private Double currentY;
    private Double currentR = 1.0;

    @PostConstruct
    public void init() {
        checkerList = Arrays.asList(
                new AbstractChecker() {
                    @Override
                    public boolean makeCheck(double x, double y, double r) {
                        return 0 <= x && y <= 0 && (x * x) + (y * y) <= ((r / 2) * (r / 2));
                    }
                },
                new AbstractChecker() {
                    @Override
                    public boolean makeCheck(double x, double y, double r) {
                        return -r / 2 <= x && 0 <= y && x <= 0 && y <= r;
                    }
                },
                new AbstractChecker() {
                    @Override
                    public boolean makeCheck(double x, double y, double r) {
                        return x <= 0 && y <= 0 && (-x - (r / 2)) <= y;
                    }
                }
        );
    }

    public Result compute(Double x, Double y, Double r) {
        Result result = new Result(x, y, r);

        long startComputeTime = System.nanoTime();
        boolean isInside = checkerList.stream()
                .anyMatch(checker -> checker.makeCheck(x, y, r));
        result.setHit(isInside);
        result.setTimestamp(LocalDateTime.now());
        result.setExecutionTime(System.nanoTime() - startComputeTime);

        return result;
    }

    public void computeCurrentAdnPersist() {
        Result currentRes = compute(currentX, currentY, currentR);
        resultService.append(currentRes);
    }

    public Double getCurrentX() {
        return currentX;
    }

    public void setCurrentX(Double currentX) {
        this.currentX = currentX;
    }

    public Double getCurrentY() {
        return currentY;
    }

    public void setCurrentY(Double currentY) {
        this.currentY = currentY;
    }

    public Double getCurrentR() {
        return currentR;
    }

    public void setCurrentR(Double currentR) {
        this.currentR = currentR;
    }
}
