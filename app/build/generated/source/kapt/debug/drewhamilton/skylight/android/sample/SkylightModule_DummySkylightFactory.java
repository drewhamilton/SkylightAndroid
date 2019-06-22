// Generated by Dagger (https://google.github.io/dagger).
package drewhamilton.skylight.android.sample;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import drewhamilton.skylight.backport.dummy.DummySkylight;
import drewhamilton.skylight.backport.dummy.dagger.DummySkylightComponent;
import javax.inject.Provider;

public final class SkylightModule_DummySkylightFactory implements Factory<DummySkylight> {
  private final Provider<DummySkylightComponent> dummySkylightComponentProvider;

  public SkylightModule_DummySkylightFactory(
      Provider<DummySkylightComponent> dummySkylightComponentProvider) {
    this.dummySkylightComponentProvider = dummySkylightComponentProvider;
  }

  @Override
  public DummySkylight get() {
    return dummySkylight(dummySkylightComponentProvider.get());
  }

  public static SkylightModule_DummySkylightFactory create(
      Provider<DummySkylightComponent> dummySkylightComponentProvider) {
    return new SkylightModule_DummySkylightFactory(dummySkylightComponentProvider);
  }

  public static DummySkylight dummySkylight(DummySkylightComponent dummySkylightComponent) {
    return Preconditions.checkNotNull(
        SkylightModule.dummySkylight(dummySkylightComponent),
        "Cannot return null from a non-@Nullable @Provides method");
  }
}
