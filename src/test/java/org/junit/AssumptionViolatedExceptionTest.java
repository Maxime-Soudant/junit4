package org.junit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class AssumptionViolatedExceptionTest {
    @DataPoint
    public static Integer TWO = 2;

    @DataPoint
    public static Matcher<Integer> IS_THREE = is(3);

    @DataPoint
    public static Matcher<Integer> NULL = null;

    @Rule
    public TestName name = new TestName();

    private static final String MESSAGE = "Assumption message";
    private static Matcher<Integer> SERIALIZABLE_IS_THREE = new SerializableIsThreeMatcher<Integer>();
    private static final UnserializableClass UNSERIALIZABLE_VALUE = new UnserializableClass();
    private static final Matcher<UnserializableClass> UNSERIALIZABLE_MATCHER = not(is(UNSERIALIZABLE_VALUE));

    @Theory
    public void toStringReportsMatcher(Integer actual, Matcher<Integer> matcher) {
        assumeThat(matcher, notNullValue());
        assertThat(new AssumptionViolatedExceptionJr(actual, matcher).toString(),
                containsString(matcher.toString()));
    }

    @Theory
    public void toStringReportsValue(Integer actual, Matcher<Integer> matcher) {
        assertThat(new AssumptionViolatedExceptionJr(actual, matcher).toString(),
                containsString(String.valueOf(actual)));
    }

    @Test
    public void assumptionViolatedExceptionWithMatcherDescribesItself() {
        AssumptionViolatedExceptionJr e = new AssumptionViolatedExceptionJr(3, is(2));
        assertThat(StringDescription.asString(e), is("got: <3>, expected: is <2>"));
    }

    @Test
    public void simpleAssumptionViolatedExceptionDescribesItself() {
        AssumptionViolatedExceptionJr e = new AssumptionViolatedExceptionJr("not enough money");
        assertThat(StringDescription.asString(e), is("not enough money"));
    }

    @Test
    public void canInitCauseWithInstanceCreatedWithString() {
      AssumptionViolatedExceptionJr e = new AssumptionViolatedExceptionJr("invalid number");
      Throwable cause = new RuntimeException("cause");
      e.initCause(cause);
      assertThat(e.getCause(), is(cause));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void canSetCauseWithInstanceCreatedWithObjectAndMatcher() {
      Throwable testObject = new Exception();
      org.junit.internal.AssumptionViolatedException e
              = new org.junit.internal.AssumptionViolatedException(
                      testObject, containsString("test matcher"));
      assertThat(e.getCause(), is(testObject));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void canSetCauseWithInstanceCreatedWithAssumptionObjectAndMatcher() {
      Throwable testObject = new Exception();
      org.junit.internal.AssumptionViolatedException e
              = new org.junit.internal.AssumptionViolatedException(
                      "sample assumption", testObject, containsString("test matcher"));
      assertThat(e.getCause(), is(testObject));
    }

    @Test
    @SuppressWarnings("deprecation")
    public void canSetCauseWithInstanceCreatedWithMainConstructor() {
      Throwable testObject = new Exception();
      org.junit.internal.AssumptionViolatedException e
              = new org.junit.internal.AssumptionViolatedException(
                      "sample assumption", false, testObject, containsString("test matcher"));
      assertThat(e.getCause(), is(testObject));
    }

    @Test
    public void canSetCauseWithInstanceCreatedWithExplicitThrowableConstructor() {
      Throwable cause = new Exception();
      AssumptionViolatedExceptionJr e = new AssumptionViolatedExceptionJr("invalid number", cause);
      assertThat(e.getCause(), is(cause));
    }

    @Test
    public void assumptionViolatedExceptionWithoutValueAndMatcherCanBeReserialized_v4_13()
            throws IOException, ClassNotFoundException {
        assertReserializable(new AssumptionViolatedExceptionJr(MESSAGE));
    }

    @Test
    public void assumptionViolatedExceptionWithValueAndMatcherCanBeReserialized_v4_13()
            throws IOException, ClassNotFoundException {
        assertReserializable(new AssumptionViolatedExceptionJr(MESSAGE, TWO, SERIALIZABLE_IS_THREE));
    }

    @Test
    public void unserializableValueAndMatcherCanBeSerialized() throws IOException, ClassNotFoundException {
        AssumptionViolatedExceptionJr exception = new AssumptionViolatedExceptionJr(MESSAGE,
                UNSERIALIZABLE_VALUE, UNSERIALIZABLE_MATCHER);

        assertCanBeSerialized(exception);
    }

    @Test
    public void nullValueAndMatcherCanBeSerialized() throws IOException, ClassNotFoundException {
        AssumptionViolatedExceptionJr exception = new AssumptionViolatedExceptionJr(MESSAGE);

        assertCanBeSerialized(exception);
    }

    @Test
    public void serializableValueAndMatcherCanBeSerialized() throws IOException, ClassNotFoundException {
        AssumptionViolatedExceptionJr exception = new AssumptionViolatedExceptionJr(MESSAGE,
                TWO, SERIALIZABLE_IS_THREE);

        assertCanBeSerialized(exception);
    }

    private void assertCanBeSerialized(AssumptionViolatedExceptionJr exception)
            throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(exception);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        AssumptionViolatedExceptionJr fromStream = (AssumptionViolatedExceptionJr) ois.readObject();

        assertSerializedCorrectly(exception, fromStream);
    }

    private void assertReserializable(AssumptionViolatedExceptionJr expected)
            throws IOException, ClassNotFoundException {
        String resourceName = name.getMethodName();
        InputStream resource = getClass().getResourceAsStream(resourceName);
        assertNotNull("Could not read resource " + resourceName, resource);
        ObjectInputStream objectInputStream = new ObjectInputStream(resource);
        AssumptionViolatedExceptionJr fromStream = (AssumptionViolatedExceptionJr) objectInputStream.readObject();

        assertSerializedCorrectly(expected, fromStream);
    }

    private void assertSerializedCorrectly(
            AssumptionViolatedExceptionJr expected, AssumptionViolatedExceptionJr fromStream) {
        assertNotNull(fromStream);

        // Exceptions don't implement equals() so we need to compare field by field
        assertEquals("message", expected.getMessage(), fromStream.getMessage());
        assertEquals("description", StringDescription.asString(expected), StringDescription.asString(fromStream));
        // We don't check the stackTrace as that will be influenced by how the test was started
        // (e.g. by maven or directly from IDE)
        // We also don't check the cause as that should already be serialized correctly by the superclass
    }

    private static class SerializableIsThreeMatcher<T> extends BaseMatcher<T> implements Serializable {

        public boolean matches(Object item) {
            return IS_THREE.matches(item);
        }

        public void describeTo(Description description) {
            IS_THREE.describeTo(description);
        }
    }

    private static class UnserializableClass {
        @Override
        public String toString() {
            return "I'm not serializable";
        }
    }
}
