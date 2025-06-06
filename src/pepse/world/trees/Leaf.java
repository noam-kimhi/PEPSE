package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.Color;
import java.util.Random;

/**
 * The Leaf class is responsible for creating leaf objects in the game.
 * Each leaf object is represented as a static block with a certain color pattern.
 *
 * @author Noam Kimhi
 * @author Or Forshmit
 */
class Leaf {

    // Private constants
    private static final float INITIAL_LEAF_ANGLE = -10; /* The initial angle of the leaf */
    private static final float FINAL_LEAF_ANGLE = 10; /* The final angle of the leaf */
    /* The duration of the leaf transitions */
    private static final float LEAF_TRANSITION_TIME_IN_SECONDS = 1.5f;
    /* The delay bound for the scheduled tasks */
    private static final float SCHEDULED_TASK_DELAY_BOUND = 2;
    /* The base color of the leaf */
    private static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
    /* The growth factor for the leaf dimensions */
    private static final Vector2 DIMENSIONS_GROWTH = Vector2.of(3, 3);
    /* The dimensions of the leaf */
    private static final Vector2 dimensions = Vector2.of(Block.SIZE, Block.SIZE);

    // Private final fields
    private final Random random; /* A random number generator */

    // Private fields
    private GameObject leaf; /* The leaf GameObject */

    /**
     * Constructs a new instance of the Leaf class.
     * This constructor initializes the Leaf class and allows for the creation
     * of leaf objects with predefined characteristics.
     */
    Leaf() {
        random = new Random();
    }

    /**
     * Creates an angle transition for the leaf GameObject.
     * <p>
     *      This transition modifies the rotation angle of the leaf's renderable over time,
     *      oscillating between a defined initial angle and a final angle.
     * </p>
     */
    private void createAngleTransition() {
        new Transition<>(
                leaf,
                (Float angle) -> leaf.renderer().setRenderableAngle(angle),
                INITIAL_LEAF_ANGLE,
                FINAL_LEAF_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                LEAF_TRANSITION_TIME_IN_SECONDS,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    /**
     * Creates a dimension transition for the leaf GameObject.
     * This method defines a smooth transition for the dimensions of the leaf.
     * <p>
     *      The transition starts from the current dimensions of the leaf and increases
     *      by a defined growth factor.
     *      The dimensions smoothly oscillate back and forth
     *      between the original size and the target size over a specified duration.
     * </p>
     */
    private void createDimensionsTransition() {
        new Transition<>(
                leaf,
                leaf::setDimensions,
                dimensions,
                dimensions.add(DIMENSIONS_GROWTH),
                Transition.LINEAR_INTERPOLATOR_VECTOR,
                LEAF_TRANSITION_TIME_IN_SECONDS,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    /**
     * Creates a new leaf GameObject at the specified position.
     *
     * @param position The top-left corner of the newly created leaf GameObject.
     * @return A GameObject representing a leaf with predefined size and color.
     */
    GameObject create(Vector2 position) {
        GameObject leaf =  new GameObject(
                position,
                dimensions,
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR))
        );

        this.leaf = leaf;

        // Make the leaves rotate in different delays
        new ScheduledTask(
                leaf,
                random.nextFloat(0, SCHEDULED_TASK_DELAY_BOUND),
                false,
                this::createAngleTransition
        );

        // Make the leaves grow and shrink in different delays
        new ScheduledTask(
                leaf,
                random.nextFloat(0, SCHEDULED_TASK_DELAY_BOUND),
                false,
                this::createDimensionsTransition
        );
        return leaf;
    }
}
