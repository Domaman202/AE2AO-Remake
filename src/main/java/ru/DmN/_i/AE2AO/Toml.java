package ru.DmN._i.AE2AO;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Provides access to the keys and tables in a TOML data source.</p>
 *
 * <p>All getters can fall back to default values if they have been provided as a constructor argument.
 * Getters for simple values (String, Date, etc.) will return null if no matching key exists.
 * 
 * <p>All read methods throw an {@link IllegalStateException} if the TOML is incorrect.</p>
 *
 * <p>Example usage:</p>
 * <pre><code>
 * Toml toml = new Toml().read(getTomlFile());
 * String name = toml.getString("name");
 * Long port = toml.getLong("server.ip"); // compound key. Is equivalent to:
 * Long port2 = toml.getTable("server").getLong("ip");
 * MyConfig config = toml.to(MyConfig.class);
 * </code></pre>
 *
 */
public class Toml {
  
  private static final Gson DEFAULT_GSON = new Gson();

  private Map<String, Object> values;
  private final Toml defaults;

  /**
   * Creates Toml instance with no defaults.
   */
  public Toml() {
    this(null);
  }

  /**
   * @param defaults fallback values used when the requested key or table is not present in the TOML source that has been read.
   */
  public Toml(Toml defaults) {
    this(defaults, new HashMap<>());
  }

  /**
   * Populates the current Toml instance with values from file.
   *
   * @param file The File to be read. Expected to be encoded as UTF-8.
   * @return this instance
   * @throws IllegalStateException If file contains invalid TOML
   */
  public Toml read(File file) {
    try {
      return read(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Populates the current Toml instance with values from reader.
   *
   * @param reader Closed after it has been read.
   * @return this instance
   * @throws IllegalStateException If file contains invalid TOML
   */
  public Toml read(Reader reader) {
    try (BufferedReader bufferedReader = new BufferedReader(reader)) {
      StringBuilder w = new StringBuilder();
      String line = bufferedReader.readLine();
      while (line != null) {
        w.append(line).append('\n');
        line = bufferedReader.readLine();
      }
      read(w.toString());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  /**
   * Populates the current Toml instance with values from tomlString.
   *
   * @param tomlString String to be read.
   * @return this instance
   * @throws IllegalStateException If tomlString is not valid TOML
   */
  public Toml read(String tomlString) throws IllegalStateException {
    Results results = run(tomlString);
    if (results.errors.hasErrors()) {
      throw new IllegalStateException(results.errors.toString());
    }

    this.values = results.consume();

    return this;
  }

  /**
   * <p>
   *  Populates an instance of targetClass with the values of this Toml instance.
   *  The target's field names must match keys or tables.
   *  Keys not present in targetClass will be ignored.
   * </p>
   *
   * <p>Tables are recursively converted to custom classes or to {@link Map Map&lt;String, Object&gt;}.</p>
   *
   * <p>In addition to straight-forward conversion of TOML primitives, the following are also available:</p>
   *
   * <ul>
   *  <li>Integer -&gt; int, long (or wrapper), {@link java.math.BigInteger}</li>
   *  <li>Float -&gt; float, double (or wrapper), {@link java.math.BigDecimal}</li>
   *  <li>One-letter String -&gt; char, {@link Character}</li>
   *  <li>String -&gt; {@link String}, enum, {@link java.net.URI}, {@link java.net.URL}</li>
   *  <li>Multiline and Literal Strings -&gt; {@link String}</li>
   *  <li>Array -&gt; {@link List}, {@link Set}, array. The generic type can be anything that can be converted.</li>
   *  <li>Table -&gt; Custom class, {@link Map Map&lt;String, Object&gt;}</li>
   * </ul>
   *
   * @param targetClass Class to deserialize TOML to.
   * @param <T> type of targetClass.
   * @return A new instance of targetClass.
   */
  public <T> T to(Class<T> targetClass) {
    JsonElement json = DEFAULT_GSON.toJsonTree(toMap());
    
    if (targetClass == JsonElement.class) {
      return targetClass.cast(json);
    }
    
    return DEFAULT_GSON.fromJson(json, targetClass);
  }

  public Map<String, Object> toMap() {
    HashMap<String, Object> valuesCopy = new HashMap<>(values);
    
    if (defaults != null) {
      for (Map.Entry<String, Object> entry : defaults.values.entrySet()) {
        if (!valuesCopy.containsKey(entry.getKey())) {
          valuesCopy.put(entry.getKey(), entry.getValue());
        }
      }
    }

    return valuesCopy;
  }

  private Toml(Toml defaults, Map<String, Object> values) {
    this.values = values;
    this.defaults = defaults;
  }

  static Results run(String tomlString) {
    final Results results = new Results();

    if (tomlString.isEmpty()) {
      return results;
    }

    AtomicInteger index = new AtomicInteger();
    boolean inComment = false;
    AtomicInteger line = new AtomicInteger(1);
    Identifier identifier = null;
    Object value = null;

    for (int i = index.get(); i < tomlString.length(); i = index.incrementAndGet()) {
      char c = tomlString.charAt(i);

      if (results.errors.hasErrors()) {
        break;
      }

      if (c == '#' && !inComment) {
        inComment = true;
      } else if (!Character.isWhitespace(c) && !inComment && identifier == null) {
        Identifier id = idConvert(tomlString, index, new Context(null, line, results.errors));

        if (id != Identifier.INVALID) {
          if (id.isKey()) {
            identifier = id;
          } else if (id.isTable()) {
            results.startTables(id, line);
          } else if (id.isTableArray()) {
            results.startTableArray(id, line);
          }
        }
      } else if (c == '\n') {
        inComment = false;
        identifier = null;
        value = null;
        line.incrementAndGet();
      } else if (!inComment && identifier != null && identifier.isKey() && value == null && !Character.isWhitespace(c)) {
        value = convert(tomlString, index, new Context(identifier, line, results.errors));

        if (value instanceof Results.Errors) {
          results.errors.add((Results.Errors) value);
        } else {
          results.addValue(identifier.getName(), value, line);
        }
      } else if (value != null && !inComment && !Character.isWhitespace(c)) {
        results.errors.invalidTextAfterIdentifier(identifier, line.get());
      }
    }

    return results;
  }

  static Object convert(String value, AtomicInteger index, Context context) {
    String substring = value.substring(index.get());
    if (intCanRead(substring))
      return intRead(value, index, context);
    if (booleanCanRead(substring))
      return booleanRead(value, index, context);

    Results.Errors errors = new Results.Errors();
    errors.invalidValue(context.identifier.getName(), substring, context.line.get());
    return errors;
  }

  static boolean booleanCanRead(String s) {
    return s.startsWith("true") || s.startsWith("false");
  }

  static Object booleanRead(String s, AtomicInteger index, Context context) {
    s = s.substring(index.get());
    Boolean b = s.startsWith("true") ? Boolean.TRUE : Boolean.FALSE;

    index.addAndGet(b == Boolean.TRUE ? 3 : 4);

    return b;
  }

  static boolean intCanRead(String s) {
    char firstChar = s.charAt(0);

    return firstChar == '+' || firstChar == '-' || Character.isDigit(firstChar);
  }

  static Object intRead(String s, AtomicInteger index, Context context) {
    boolean signable = true;
    boolean dottable = false;
    boolean exponentable = false;
    boolean terminatable = false;
    boolean underscorable = false;
    String type = "";
    StringBuilder sb = new StringBuilder();

    for (int i = index.get(); i < s.length(); i = index.incrementAndGet()) {
      char c = s.charAt(i);
      boolean notLastChar = s.length() > i + 1;

      if (Character.isDigit(c)) {
        sb.append(c);
        signable = false;
        terminatable = true;
        if (type.isEmpty()) {
          type = "integer";
          dottable = true;
        }
        underscorable = notLastChar;
        exponentable = !type.equals("exponent");
      } else if ((c == '+' || c == '-') && signable && notLastChar) {
        signable = false;
        if (c == '-')
          sb.append('-');
      } else if (c == '.' && dottable && notLastChar) {
        sb.append('.');
        type = "float";
        terminatable = false;
        dottable = false;
        exponentable = false;
        underscorable = false;
      } else if ((c == 'E' || c == 'e') && exponentable && notLastChar) {
        sb.append('E');
        type = "exponent";
        terminatable = false;
        signable = true;
        dottable = false;
        exponentable = false;
        underscorable = false;
      } else if (c == '_' && underscorable && notLastChar && Character.isDigit(s.charAt(i + 1))) {
        underscorable = false;
      } else {
        if (!terminatable)
          type = "";
        index.decrementAndGet();
        break;
      }
    }

    if ("integer".equals(type))
      return Long.valueOf(sb.toString());
    Results.Errors errors = new Results.Errors();
    errors.invalidValue(context.identifier.getName(), sb.toString(), context.line.get());
    return errors;
  }

  static Identifier idConvert(String s, AtomicInteger index, Context context) {
    boolean quoted = false;
    StringBuilder name = new StringBuilder();
    boolean terminated = false;
    boolean isKey = s.charAt(index.get()) != '[';
    boolean isTableArray = !isKey && s.length() > index.get() + 1 && s.charAt(index.get() + 1) == '[';
    boolean inComment = false;

    for (int i = index.get(); i < s.length(); i = index.incrementAndGet()) {
      char c = s.charAt(i);
      if (Keys.isQuote(c) && (i == 0 || s.charAt(i - 1) != '\\')) {
        quoted = !quoted;
        name.append(c);
      } else if (c == '\n') {
        index.decrementAndGet();
        break;
      } else if (quoted) {
        name.append(c);
      } else if (c == '=' && isKey) {
        terminated = true;
        break;
      } else if (c == ']' && !isKey) {
        if (!isTableArray || s.length() > index.get() + 1 && s.charAt(index.get() + 1) == ']') {
          terminated = true;
          name.append(']');
          if (isTableArray) {
            name.append(']');
          }
        }
      } else if (terminated && c == '#') {
        inComment = true;
      } else if (terminated && !Character.isWhitespace(c) && !inComment) {
        terminated = false;
        break;
      } else if (!terminated) {
        name.append(c);
      }
    }

    if (!terminated) {
      if (isKey) {
        context.errors.unterminatedKey(name.toString(), context.line.get());
      } else {
        context.errors.invalidKey(name.toString(), context.line.get());
      }

      return Identifier.INVALID;
    }

    return Identifier.from(name.toString(), context);
  }
}
