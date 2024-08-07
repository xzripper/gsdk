package gsdk.source.utils;

import java.util.function.Consumer;

import static gsdk.source.utils.Range.inRange;

import static gsdk.source.utils.Assert.assert_f;

/**
 * Scene manager utility.
 */
public class SceneManager<T> {
    private final Consumer<T[]>[] levels;

    private int id;

    private T[] tempArgs;

    /**
     * Initialize scene manager.
     * 
     * @param sceneLevels Scene levels.
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public SceneManager(Consumer<T[]> ...sceneLevels) {
        Consumer<T[]>[] _sLevelsT = (Consumer<T[]>[]) new Consumer<?>[sceneLevels.length];

        System.arraycopy(sceneLevels, 0, _sLevelsT, 0, sceneLevels.length);

        levels = _sLevelsT;
    }

    /**
     * Update scene ID and arguments.
     * 
     * @param sceneID Scene ID (from 1 to levels length).
     * @param arguments Arguments to pass to the next scene.
     */
    @SuppressWarnings("unchecked")
    public void updateScene(int sceneID, T ...arguments) {
        assert_f(inRange(sceneID, 1, levels.length), "invalid scene id");

        id = sceneID;

        tempArgs = arguments;
    }

    /**
     * Get current scene ID.
     */
    public int getSceneID() {
        return id;
    }

    /**
     * Render scene.
     */
    public void renderScene() {
        levels[id - 1].accept(tempArgs);
    }
}
