package junit.runner;

import static org.junit.Assert.*;

import org.junit.Test;

public class VersionTest {

    @Test
    public void testId() {
        assertEquals(Version.id(), "4.13.3-SNAPSHOT");
    }

}
