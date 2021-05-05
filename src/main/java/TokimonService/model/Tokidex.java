package TokimonService.model;

import TokimonService.helpers.FileIOHelper;
import TokimonService.model.enums.TokimonType;
import TokimonService.model.exceptions.CustomException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 *  Tokidex is a class that manages all the Tokimons that we currently have info on.
 *      - (similar to the Pokédex from pokemon)
 *  Responsibilities:
 *      - Store list of current Tokimons
 *      - Find the Tokimon object given its id
 *      - Add new tokimon
 *      - Change a tokimon
 *      - Delete a tokimon
 */
@Data
@NoArgsConstructor
public class Tokidex {
    private TreeMap<Long, Tokimon> tokimonMap = new TreeMap<>();
    private AtomicLong atomicLong = new AtomicLong();

    public Tokidex(List<Tokimon> tokimonList) {
        long maxId = 0;
        for (Tokimon tokimon : tokimonList) {
            tokimonMap.put(tokimon.getTokimonId(), tokimon);
            maxId = Math.max(maxId, tokimon.getTokimonId());
        }
        atomicLong.set(maxId);
    }

    public List<Tokimon> getTokimonList() {
        return new ArrayList<>(tokimonMap.values());
    }

    public Tokimon getTokimonById(Long id) {
        if (!tokimonMap.containsKey(id)) {
            throw new CustomException.ResourceNotFoundException("Tokimon not found. id: " + id);
        }
        return tokimonMap.get(id);
    }

    public Tokimon addTokimon(Tokimon tokimon) {
        TokimonType.validateEnum(tokimon);

        if (tokimon.getName() == null || tokimon.getName().isEmpty()) {
            throw new CustomException.BadRequestException("Please give the tokimon a name at least!");
        }
        if (tokimon.getAbility() == null || tokimon.getAbility().isEmpty()) {
            throw new CustomException.BadRequestException("Please give the tokimon an ability type! " + Arrays.toString(TokimonType.values()));
        }
        if (tokimon.getHealth() < 0) {
            tokimon.setHealth(0);
        } else if (tokimon.getHealth() > 100) {
            tokimon.setHealth(100);
        }
        if (tokimon.getHeight() < 0 || tokimon.getWeight() < 0) {
            throw new CustomException.BadRequestException("Height and weight cannot be negative!");
        }
        long newId = atomicLong.incrementAndGet();
        tokimon.setTokimonId(newId);

        tokimonMap.put(newId, tokimon);
        FileIOHelper.writeToFile(getTokimonList());
        return tokimon;
    }

    public Tokimon changeTokimonById(Long id, Tokimon newToki) {
        Tokimon toki = getTokimonById(id);
        toki.alterToki(newToki);

        tokimonMap.put(id, toki);
        FileIOHelper.writeToFile(getTokimonList());
        return toki;
    }

    public void deleteToki(Long id) {
        if (!tokimonMap.containsKey(id)) {
            throw new CustomException.ResourceNotFoundException("Tokimon not found. id: " + id);
        }
        tokimonMap.remove(id);
        FileIOHelper.writeToFile(getTokimonList());
    }

}
