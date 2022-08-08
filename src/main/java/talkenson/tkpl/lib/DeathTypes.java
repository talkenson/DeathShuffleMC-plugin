package talkenson.tkpl.lib;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import talkenson.tkpl.lib.types.DeathTypeMeta;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeathTypes {
  HashMap<String, DeathTypeMeta> deathMap = new HashMap<>();
  Gson gson = new Gson();
  Type type = new TypeToken<HashMap<String, DeathTypeMeta>>() {}.getType();

  public DeathTypes() {
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      InputStream inputStream = classLoader.getResourceAsStream("json/deathTypes.json");
      if (inputStream == null) {
        throw new IllegalArgumentException("file not found! ");
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

      this.deathMap = gson.fromJson(reader, type);

    } catch (IllegalArgumentException e) {
      Bukkit.getLogger().warning("deathTypes.json is not available");
    }
  }

  public List<String> getDeathTypes() {
    return deathMap.keySet().stream().toList();
  }

  public List<Float> getDeathProbabilities() {
    return deathMap.values().stream().map(v -> v.prob).toList();
  }

  public boolean testDeathType(String deathType, String deathMessage) {
    if (!getDeathTypes().contains(deathType)) {
      return false;
    }

    final Pattern pattern =
        Pattern.compile(
            deathMap.get(deathType).regex,
            Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    final Matcher matcher = pattern.matcher(deathMessage);

    return matcher.find();
  }
}
