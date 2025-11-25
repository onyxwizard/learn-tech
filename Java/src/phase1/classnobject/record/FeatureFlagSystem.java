package phase1.classnobject.record;

enum Feature {
  DARK_MODE,
  LIGHT_MODE,
  THEME_ENGINE_ON,
  THEME_ENGINE_OFF
}

record FeatureFlag(String key, String description, Feature f) {
  static Feature engineStatus = Feature.THEME_ENGINE_OFF;
  boolean isEnable(Feature ff) {
    return ff.equals(this.f);
  }

  boolean engine() {
    return engineStatus.equals(Feature.THEME_ENGINE_ON);
  }

  void setTheme(Feature ff) {
    engineStatus = ff;
  }
  
  Feature getEngine() {
    return engineStatus;
  }
}

public class FeatureFlagSystem {
  public static void main(String[] args) {
    FeatureFlag ff = new FeatureFlag("DARK_MODE", "Enable dark theme", Feature.DARK_MODE);
    boolean res = ff.isEnable(Feature.DARK_MODE);
    boolean engine = ff.engine();
    
    System.out.println(ff.getEngine());
    System.out.println(res + " " + engine);
    
    ff.setTheme(Feature.THEME_ENGINE_ON);
    engine = ff.engine();
    System.out.println(ff.getEngine());
    System.out.println(res + " " + engine);
  }
}
