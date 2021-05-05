package TokimonService.model;

import TokimonService.model.enums.TokimonType;
import TokimonService.model.exceptions.CustomException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * Model class of a Tokimon.
 * used to store in memory as well as in json when serialized
 * has method to alter its own fields (except for tokimonId) based on another tokimon
 */
@Data
@NoArgsConstructor
public class Tokimon {

    private Long tokimonId;
    private String name;
    private Double weight;
    private Double height;
    private String ability;
    private Double strength;
    private String color;
    private int health;

    public Tokimon(String name, Double weight, Double height, String ability, Double strength, String color, int health) {
        if (TokimonType.fromString(ability) == null) {
            throw new CustomException.BadRequestException("Ability field is not valid! Valid fields are: " + Arrays.toString(TokimonType.values()));
        }
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.ability = ability.toUpperCase();
        this.strength = strength;
        this.color = color;
        if (health < 0) {
            this.health = 0;
        } else if (health > 100) {
            this.health = 100;
        } else {
            this.health = health;
        }
    }

    public void alterToki(Tokimon newToki) {
        if (newToki.getAbility() != null) {
            TokimonType.validateEnum(newToki);
            this.ability = newToki.getAbility();
        }
        if (newToki.getName() != null) {
            this.name = newToki.getName();
        }
        if (newToki.getWeight() != null) {
            this.weight = newToki.getWeight();
        }
        if (newToki.getHeight() != null) {
            this.height = newToki.getHeight();
        }
        if (newToki.getStrength() != null) {
            this.strength = newToki.getStrength();
        }
        if (newToki.getColor() != null) {
            this.color = newToki.getColor();
        }
        if (newToki.getHealth() < 0) {
            this.health = 0;
        } else if (newToki.getHealth() > 100) {
            this.health = 100;
        } else {
            this.health = newToki.getHealth();
        }
    }
}
