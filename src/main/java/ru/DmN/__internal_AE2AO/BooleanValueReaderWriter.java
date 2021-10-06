package ru.DmN.__internal_AE2AO;

import java.util.concurrent.atomic.AtomicInteger;


class BooleanValueReaderWriter implements ValueReader {
  static final BooleanValueReaderWriter BOOLEAN_VALUE_READER_WRITER = new BooleanValueReaderWriter(); 

  @Override
  public boolean canRead(String s) {
    return s.startsWith("true") || s.startsWith("false");
  }

  @Override
  public Object read(String s, AtomicInteger index, Context context) {
    s = s.substring(index.get());
    Boolean b = s.startsWith("true") ? Boolean.TRUE : Boolean.FALSE;
    
    index.addAndGet(b == Boolean.TRUE ? 3 : 4);
    
    return b;
  }
}
