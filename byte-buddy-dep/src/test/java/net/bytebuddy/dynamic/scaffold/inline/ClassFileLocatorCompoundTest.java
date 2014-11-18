package net.bytebuddy.dynamic.scaffold.inline;

import net.bytebuddy.instrumentation.type.TypeDescription;
import net.bytebuddy.utility.MockitoRule;
import net.bytebuddy.utility.ObjectPropertyAssertion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class ClassFileLocatorCompoundTest {

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private ClassFileLocator classFileLocator, otherClassFileLocator;
    @Mock
    private TypeDescription typeDescription;
    @Mock
    private TypeDescription.BinaryRepresentation legal, illegal;

    @Before
    public void setUp() throws Exception {
        when(legal.isValid()).thenReturn(true);
    }

    @Test
    public void testApplicationOrderCallsSecond() throws Exception {
        when(classFileLocator.classFileFor(typeDescription)).thenReturn(illegal);
        when(otherClassFileLocator.classFileFor(typeDescription)).thenReturn(legal);
        assertThat(new ClassFileLocator.Compound(classFileLocator, otherClassFileLocator).classFileFor(typeDescription), is(legal));
        verify(classFileLocator).classFileFor(typeDescription);
        verifyNoMoreInteractions(classFileLocator);
        verify(otherClassFileLocator).classFileFor(typeDescription);
        verifyNoMoreInteractions(otherClassFileLocator);
    }

    @Test
    public void testApplicationOrderDoesNotCallSecond() throws Exception {
        when(classFileLocator.classFileFor(typeDescription)).thenReturn(legal);
        assertThat(new ClassFileLocator.Compound(classFileLocator, otherClassFileLocator).classFileFor(typeDescription), is(legal));
        verify(classFileLocator).classFileFor(typeDescription);
        verifyNoMoreInteractions(classFileLocator);
        verifyZeroInteractions(otherClassFileLocator);
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(ClassFileLocator.Compound.class).apply();
    }
}