package io.tharka.samvada.core.util;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.NoArgGenerator;

import java.util.UUID;

public class SecureIdGenerator {
    private static final NoArgGenerator v7Generator = Generators.timeBasedEpochGenerator();

    private SecureIdGenerator(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UUID generateOrderId(){
        return v7Generator.generate();
    }
}
