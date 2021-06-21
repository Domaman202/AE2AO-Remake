package com.moandjiezana.toml;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    Results results = TomlParser.run(tomlString);
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
}
