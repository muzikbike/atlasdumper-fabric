package lol.skye.atlasdumper.util;

import lol.skye.atlasdumper.AtlasDumper;
import lol.skye.atlasdumper.data.SpriteAtlasData;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ReflectionUtil {

    private static void openField(Field field)
            throws NoSuchFieldException, IllegalAccessException {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }

    public static <T> T getField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // if there is a better way to do this with the mixin library,
    // please let me know :pleading_face:
    public static SpriteAtlasData getData(Object data, boolean getMaxLevel) {
        Class<?> dataClass = data.getClass();
        int width, height;
        int maxLevel = -1;

        try {
            Field widthField = dataClass.getDeclaredField("field_17901");
            Field heightField = dataClass.getDeclaredField("field_17902");
            openField(widthField);
            openField(heightField);

            width = widthField.getInt(data);
            height = heightField.getInt(data);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            AtlasDumper.LOGGER.error("reflection failed", e);
            return null;
        }

        if (getMaxLevel) {
            try {
                Field maxLevelField = dataClass.getDeclaredField("field_21795");
                openField(maxLevelField);
                maxLevel = maxLevelField.getInt(data);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // ignore, leave maxLevel set to -1
            }
        }

        return new SpriteAtlasData(width, height, maxLevel);
    }

    public static SpriteAtlasData getRecordData(Object record) {
        Class<?> recordClass = record.getClass();
        int width, height, maxLevel;

        try {
            Field widthField = recordClass.getDeclaredField("comp_1040");
            Field heightField = recordClass.getDeclaredField("comp_1041");
            Field maxLevelField = recordClass.getDeclaredField("comp_1042");

            widthField.setAccessible(true);
            heightField.setAccessible(true);
            maxLevelField.setAccessible(true);

            width = widthField.getInt(record);
            height = heightField.getInt(record);
            maxLevel = maxLevelField.getInt(record);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            AtlasDumper.LOGGER.error("reflection failed", e);
            return null;
        }

        return new SpriteAtlasData(width, height, maxLevel);
    }
}
