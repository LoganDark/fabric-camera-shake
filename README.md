# Camera Shake

- This library provides a convenient API for mods to add camera shake in a way
  that is compatible with other mods that want to do the same thing
- In third-person mode, the zoom calculation is done after the camera shake, so
  that the camera still does not intersect with blocks
- Also, it is compatible with Immersive Portals, and respects the view bobbing
  factor, so camera shake does not happen while you are nearly intersecting a
  portal
- Compatibility with other mods is planned and pull requests are welcome

## Description

Using this library you can easily add camera shake to your mods through a
convenient API. Headaches like finding where to mixin, compatibility with other
mods, etc. are handled for you.

All that's needed to add some shake is this:

```java
CameraShakeManager.getInstance().addEvent(new BoomEvent(.25, 0, .5));
```

First, assuming that we are in a game, we get a reference to the global
`CameraShakeManager` that manages the play session. (Otherwise, we don't, and a
`NullPointerException` is thrown by the attempted `addEvent` call - oops!)

Then, we tell the `CameraShakeManager` that we want to add a `CameraShakeEvent`,
in this case a `BoomEvent`. And `BoomEvent`'s constructor lets us specify that
we want a magnitude (strength) of `.25` blocks, and we want it to last for `.5`
seconds.

You're also able to create your own `CameraShakeEvent`s by implementing the
interface, and then you can pass it to `addEvent` like normal.

But if one-time events aren't all you need, Camera Shake still has you covered.
It has the concept of *providers*, which are always-present.

If you want to implement a provider that, for example, shakes the screen when an
earthquake is happening, or shakes the screen when you are near a large
industrial machine, all you have to do is implement the `CameraShakeProvider`
interface and use `CameraShakeManager.addProvider` instead of `addEvent`.

In addition to providers being a thing, you can also specify a
`CameraShakeInitializer` as an entrypoint in your `fabric.mod.json`, and Camera
Shake will call your initializer with the `CameraShakeManager` every time the
client joins a new server. That way, if you add your providers there, you can
be sure your providers will always present because they will be added to every
`CameraShakeManager`.

## Using the library in a mod

1. Add my maven in your `repositories` block:

   ```groovy
   maven { url 'https://maven.logandark.net' }
   ```

2. Choose the version you want from
   [the maven](http://maven.logandark.net/net/logandark/camera-shake/) (you
   usually want the latest), and then depend on it in your `dependencies` block
   like so:

   ```groovy
   modImplementation 'net.logandark:camera-shake:<VERSION>'
   include 'net.logandark:camera-shake:<VERSION>'
   ```

   `include` just includes CameraShake in your mod's jar using Loom's JiJ
   system.

3. Add `"camera-shake"` to your `fabric.mod.json` as a dependency.

4. Explore the javadocs starting at `CameraShakeManager`, and you should be good
   to go.

**Note: classes outside the API package (`net.logandark.camerashake.api`) are
not subject to semantic versioning and may change at any time.**

## Hacking on the library

1. Clone the repo, open the project folder in IntelliJ IDEA, and import the
   Gradle project.

2. You're pretty much good to go now. The Minecraft Client run configuration can
   be used to run the client.

3. In-game, press the `K` key to create a BoomEvent as described above.

4. Daring developers can add features through the GitHub UI. :yeefuckinhaw:
