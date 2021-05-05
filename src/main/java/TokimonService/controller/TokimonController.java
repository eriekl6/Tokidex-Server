package TokimonService.controller;

import TokimonService.model.Tokimon;
import TokimonService.model.Tokidex;
import TokimonService.model.enums.TokimonType;
import TokimonService.model.exceptions.CustomException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Controller class for handling Tokimon
 * Stores tokimon info in a json file 'tokimon.json'
 *
 * Has endpoints to:
     * Get all Tokimons
     * Get Tokimon by Id
     * Add tokimon
     * Alter Tokimon
     * Delete Tokimon
 */
@RestController
@RequestMapping("/api/tokimon")
public class TokimonController {

    Tokidex tokidex = new Tokidex();

    @GetMapping("/all")
    public List<Tokimon> getAllTokimons() {
        return tokidex.getTokimonList();
    }

    @GetMapping("/{id}")
    public Tokimon getTokimonById(@PathVariable Long id) {
        return tokidex.getTokimonById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Tokimon addNewTokimon(@RequestBody Tokimon tokimon) {
        if (TokimonType.fromString(tokimon.getAbility()) == null) {
            throw new CustomException.BadRequestException("Ability field is not valid! Valid fields are: " + Arrays.toString(TokimonType.values()));
        } else {
            tokimon.setAbility(tokimon.getAbility().toUpperCase());
        }
        return tokidex.addTokimon(tokimon);
    }

    @PostMapping("/change/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Tokimon changeTokimonById(@PathVariable Long id, @RequestBody(required = false) Tokimon tokimon) {
        if (tokimon == null) {
            throw new CustomException.BadRequestException("Required request body is missing.");
        }
        return tokidex.changeTokimonById(id, tokimon);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTokimonById(@PathVariable Long id) {
        tokidex.deleteToki(id);
    }

    @PostConstruct
    public void initData() {
        Gson gson = new Gson();
        File file = new File("data/tokimon.json");

        try {
            System.out.println(String.format("Reading data in %s to memory", file.getName()));
            List<Tokimon> tokis = gson.fromJson(new FileReader(file.getAbsolutePath()), new TypeToken<List<Tokimon>>(){}.getType());
            System.out.println(tokis);
            tokidex = new Tokidex(tokis);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. " + "\nFile: " + file.getName() + "\nPath: " + file.getAbsolutePath());
        }

    }
}
