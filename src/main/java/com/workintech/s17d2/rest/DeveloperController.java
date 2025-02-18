package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public final Taxable taxable;


    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    public Map<Integer, Developer> developers = new HashMap<>();

    @PostConstruct
    public void init() {
        developers.put(1, new JuniorDeveloper(1, "Emir", 15000.0));
        developers.put(2, new MidDeveloper(2, "Erim", 20000.0));
        developers.put(3, new SeniorDeveloper(3, "Kanat", 25000.0));
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")

    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDeveloper(@RequestBody Developer developer) {
        double salary = developer.getSalary();

        switch (developer.getExperience()) {
            case JUNIOR:
                salary -= (salary * taxable.getSimpleTaxRate() / 100);
                developers.put(developer.getId(), new JuniorDeveloper(developer.getId(), developer.getName(), salary));
                break;
            case MID:
                salary -= (salary * taxable.getMiddleTaxRate() / 100);
                developers.put(developer.getId(), new MidDeveloper(developer.getId(), developer.getName(), salary));
                break;
            case SENIOR:
                salary -= (salary * taxable.getUpperTaxRate() / 100);
                developers.put(developer.getId(), new SeniorDeveloper(developer.getId(), developer.getName(), salary));
                break;
        }

        return developers.get(developer.getId());
    }


    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer developer) {
        developers.put(id, developer);
        return developer;
    }

    @DeleteMapping("/{id}")
    public String deleteDeveloper(@PathVariable int id) {
        developers.remove(id);
        return "Developer deleted!";
    }
}


