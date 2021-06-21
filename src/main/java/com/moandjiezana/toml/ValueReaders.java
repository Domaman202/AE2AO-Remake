package com.moandjiezana.toml;

import java.util.concurrent.atomic.AtomicInteger;

import static com.moandjiezana.toml.BooleanValueReaderWriter.BOOLEAN_VALUE_READER_WRITER;
import static com.moandjiezana.toml.NumberValueReaderWriter.NUMBER_VALUE_READER_WRITER;

class ValueReaders {
  
  static final ValueReaders VALUE_READERS = new ValueReaders();
  
  Object convert(String value, AtomicInteger index, Context context) {
    String substring = value.substring(index.get());
    for (ValueReader valueParser : READERS) {
      if (valueParser.canRead(substring)) {
        return valueParser.read(value, index, context);
      }
    }
    
    Results.Errors errors = new Results.Errors();
    errors.invalidValue(context.identifier.getName(), substring, context.line.get());
    return errors;
  }
  
  private ValueReaders() {}
  
  private static final ValueReader[] READERS = { NUMBER_VALUE_READER_WRITER, BOOLEAN_VALUE_READER_WRITER };
}
