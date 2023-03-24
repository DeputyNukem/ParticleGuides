package me.monst.particleguides.configuration.exception;

/**
 * An exception that is thrown when a configuration value does not conform to its bounds, or is otherwise
 * invalid in a way that can be repaired.
 * This exception must be instantiated with the corrected/repaired value as a replacement.
 */
public class ValueOutOfBoundsException extends Exception {

    private final Object replacement;

    public <T> ValueOutOfBoundsException(T replacement) {
        this.replacement = replacement;
    }

    @SuppressWarnings("unchecked")
    public <T> T getReplacement() {
        return (T) replacement;
    }

}
