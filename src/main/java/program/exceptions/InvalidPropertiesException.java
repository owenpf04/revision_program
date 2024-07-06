package program.exceptions;

import program.helpers.InvalidProperty;

import java.util.LinkedHashSet;
import java.util.Set;

public class InvalidPropertiesException extends Exception {
    private final Set<InvalidProperty> invalidPropertySet;

    public InvalidPropertiesException(Set<InvalidProperty> invalidPropertySet) {
        this.invalidPropertySet = invalidPropertySet;
    }

    public Set<InvalidProperty> getInvalidPropertySet() {
        return invalidPropertySet;
    }

    public Set<String> getInvalidKeys() {
        Set<String> keys = new LinkedHashSet<>();
        for (InvalidProperty prop : invalidPropertySet) {
            keys.add(prop.getKey());
        }

        return keys;
    }
}
