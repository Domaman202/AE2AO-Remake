package ru.DmN.__internal_AE2AO;

import java.util.concurrent.atomic.AtomicInteger;

import static ru.DmN.__internal_AE2AO.BooleanValueReaderWriter.BOOLEAN_VALUE_READER_WRITER;
import static ru.DmN.__internal_AE2AO.NumberValueReaderWriter.NUMBER_VALUE_READER_WRITER;

class ValueReaders {

  static final ValueReaders VALUE_READERS = new ValueReaders();

  Object convert(String value, AtomicInteger index, Context context) {
    String substring = value.substring(index.get());
    if (NUMBER_VALUE_READER_WRITER.canRead(substring))
      return NUMBER_VALUE_READER_WRITER.read(value, index, context);
    if (BOOLEAN_VALUE_READER_WRITER.canRead(substring))
      return BOOLEAN_VALUE_READER_WRITER.read(value, index, context);

    Results.Errors errors = new Results.Errors();
    errors.invalidValue(context.identifier.getName(), substring, context.line.get());
    return errors;
  }

  private ValueReaders() {
  }
}