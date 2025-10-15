package vn.tcl.timviec24h.util.constant;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * AttributeConverter to safely convert between database integer ordinal and LevelEnum.
 * This prevents Hibernate from throwing ArrayIndexOutOfBoundsException when the stored
 * ordinal does not match any enum constant (e.g. dirty data).
 */
@Converter(autoApply = true)
public class LevelEnumConverter implements AttributeConverter<LevelEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LevelEnum attribute) {
        return attribute == null ? null : attribute.ordinal();
    }

    @Override
    public LevelEnum convertToEntityAttribute(Integer dbData) {
        if (dbData == null) return null;
        LevelEnum[] values = LevelEnum.values();
        if (dbData >= 0 && dbData < values.length) {
            return values[dbData];
        }
        // Out-of-range value in DB: return null so application can handle it gracefully.
        // Alternative: return a sentinel like LevelEnum.UNKNOWN if you add it to the enum.
        return null;
    }
}
