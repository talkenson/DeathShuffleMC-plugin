package talkenson.tkpl.lib;

import java.util.List;

public final class Utils {
  public static <T> T getRandomFromArray(List<T> array) {
    var index = (int) Math.floor(Math.random() * array.size());
    return array.get(index);
  }

  public static <T> T getRandomFromArrayProbabilities(List<T> array, List<Float> probabilities) {
    var sum = probabilities.stream().reduce(0F, Float::sum);
    var mapped = probabilities.stream().map(prob -> (float) prob / sum).toList();
    var rand = Math.random();
    Float currentProb = 0F;

    for (var index = 0; index < mapped.size(); index++) {
      currentProb += mapped.get(index);
      if (rand < currentProb) return array.get(index);
    }

    return array.get(array.size() - 1);
  }
}
