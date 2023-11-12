package com.vetrikos.inventory.system.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

  private static final String SPLIT_DELIMITER = ";";

  @Override
  public String convertToDatabaseColumn(List<String> strings) {
    return strings != null ? String.join(SPLIT_DELIMITER, strings) : "";
  }

  @Override
  public List<String> convertToEntityAttribute(String s) {
    return s != null ? Arrays.asList(s.split(SPLIT_DELIMITER)) : Collections.emptyList();
  }
}
