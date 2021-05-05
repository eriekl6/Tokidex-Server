package TokimonService.model.enums;

import TokimonService.model.Tokimon;
import TokimonService.model.exceptions.CustomException;

import java.util.Arrays;

/**
 * Enum for possible types/abilities of a tokimon
 */
public enum TokimonType {
    FLYING,
    FIRE,
    WATER,
    ELECTRIC,
    ICE;

    public static TokimonType fromString(String text) {
        for (TokimonType type : TokimonType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

    public static void validateEnum(Tokimon tokimon) {
        if (TokimonType.fromString(tokimon.getAbility()) == null) {
            throw new CustomException.BadRequestException("Ability field is not valid! Valid fields are: " + Arrays.toString(TokimonType.values()));
        } else {
            tokimon.setAbility(tokimon.getAbility().toUpperCase());
        }
    }
}
