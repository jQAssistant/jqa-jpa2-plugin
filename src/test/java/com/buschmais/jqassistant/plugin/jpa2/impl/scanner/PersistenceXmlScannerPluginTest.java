package com.buschmais.jqassistant.plugin.jpa2.impl.scanner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.buschmais.jqassistant.core.analysis.api.rule.source.FileRuleSourceTest;
import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerContext;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.model.PropertyDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.java.api.model.TypeDescriptor;
import com.buschmais.jqassistant.plugin.java.api.scanner.JavaScope;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeCache;
import com.buschmais.jqassistant.plugin.java.api.scanner.TypeResolver;
import com.buschmais.jqassistant.plugin.jpa2.api.model.PersistenceUnitDescriptor;
import com.buschmais.jqassistant.plugin.jpa2.api.model.PersistenceXmlDescriptor;

@RunWith(MockitoJUnitRunner.class)
public class PersistenceXmlScannerPluginTest {

    private static PersistenceXmlScannerPlugin plugin;

    @Mock
    TypeResolver typeResolver;

    @Mock
    FileResource item;

    @Mock
    Store store;

    @Mock
    ScannerContext context;

    @Mock
    FileDescriptor fileDescriptor;

    @Mock
    TypeDescriptor jpaEntityDescriptor;

    @Mock
    TypeCache.CachedType<TypeDescriptor> cachedType;

    @Mock
    Scanner scanner;

    @Mock
    PersistenceXmlDescriptor persistenceDescriptor;

    @Spy
    List<PersistenceUnitDescriptor> persistenceUnitList = new LinkedList<>();

    @Spy
    List<TypeDescriptor> persistenceEntities = new LinkedList<>();

    @Mock
    PersistenceUnitDescriptor unitDescriptor;

    @Mock
    PropertyDescriptor propertyDescriptor;

    @Spy
    Set<PropertyDescriptor> properties = new HashSet<>();

    private String path = "/META-INF/persistence.xml";

    @Before
    public void createScanner() {
        plugin = new PersistenceXmlScannerPlugin();
        plugin.initialize();
    }

    @Before
    public void configureMocks() throws IOException {


        doReturn(persistenceEntities).when(unitDescriptor).getContains();
        doReturn(store).when(context).getStore();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return PersistenceXmlScannerPluginTest.class.getResourceAsStream("/2_0/full/META-INF/persistence.xml");
            }
        }).when(item).createStream();
        doReturn(properties).when(unitDescriptor).getProperties();
        doReturn(propertyDescriptor).when(store).create(PropertyDescriptor.class);
        doReturn(jpaEntityDescriptor).when(cachedType).getTypeDescriptor();
        doReturn(cachedType).when(typeResolver).resolve(eq("com.buschmais.jqassistant.plugin.jpa2.test.set.entity.JpaEntity"), eq(context));
        doReturn(typeResolver).when(context).peek(TypeResolver.class);
        doReturn(context).when(scanner).getContext();
        doReturn(fileDescriptor).when(context).peek(FileDescriptor.class);
        doReturn(persistenceDescriptor).when(store).addDescriptorType(fileDescriptor, PersistenceXmlDescriptor.class);
        doReturn(persistenceUnitList).when(persistenceDescriptor).getContains();
        doReturn(unitDescriptor).when(store).create(PersistenceUnitDescriptor.class);
    }

    @Test
    public void scannerAcceptsIfInClasspathScope() throws IOException {
        FileResource item = Mockito.mock(FileResource.class, new FileRuleSourceTest.MethodNotMockedAnswer());
        String path = "/META-INF/persistence.xml";
        Scope scope = JavaScope.CLASSPATH;

        assertThat(plugin.accepts(item, path, scope), is(true));
    }

    @Test
    public void scannerAcceptsIfPersistenceXMLIsInMETAINF() throws Exception {
        FileResource item = Mockito.mock(FileResource.class, new FileRuleSourceTest.MethodNotMockedAnswer());
        String path = "/META-INF/persistence.xml";
        Scope scope = JavaScope.CLASSPATH;

        assertThat(plugin.accepts(item, path, scope), is(true));
    }

    @Test
    public void scannerAcceptsIfPersistenceXMLIsInWEBINF() throws Exception {
        FileResource item = Mockito.mock(FileResource.class, new FileRuleSourceTest.MethodNotMockedAnswer());
        String path = "/WEB-INF/persistence.xml";
        Scope scope = JavaScope.CLASSPATH;

        assertThat(plugin.accepts(item, path, scope), is(true));
    }

    @Test
    public void scannerFindAllPropertisInPersistenceXMLV20() throws IOException {
        String path = "/META-INF/persistence.xml";

        doReturn(store).when(context).getStore();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return PersistenceXmlScannerPluginTest.class.getResourceAsStream("/2_0/full/META-INF/persistence.xml");
            }
        }).when(item).createStream();
        doReturn(properties).when(unitDescriptor).getProperties();
        doReturn(propertyDescriptor).when(store).create(PropertyDescriptor.class);
        doReturn(jpaEntityDescriptor).when(cachedType).getTypeDescriptor();
        doReturn(cachedType).when(typeResolver).resolve(eq("com.buschmais.jqassistant.plugin.jpa2.test.set.entity.JpaEntity"), eq(context));
        doReturn(typeResolver).when(context).peek(TypeResolver.class);
        doReturn(context).when(scanner).getContext();
        doReturn(fileDescriptor).when(context).peek(FileDescriptor.class);
        doReturn(persistenceDescriptor).when(store).addDescriptorType(fileDescriptor, PersistenceXmlDescriptor.class);
        doReturn(unitDescriptor).when(store).create(PersistenceUnitDescriptor.class);

        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        verify(store, times(1)).create(PropertyDescriptor.class);
        verify(propertyDescriptor, times(1)).setValue("stringValue");
        verify(propertyDescriptor, times(1)).setName("stringProperty");
        verify(properties).add(eq(propertyDescriptor));
    }

    @Test
    public void scannerFindVersionInPersistenceXMLV20() throws IOException {


        String path = "/META-INF/persistence.xml";

        doReturn(store).when(context).getStore();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return PersistenceXmlScannerPluginTest.class.getResourceAsStream("/2_0/full/META-INF/persistence.xml");
            }
        }).when(item).createStream();
        doReturn(properties).when(unitDescriptor).getProperties();
        doReturn(propertyDescriptor).when(store).create(PropertyDescriptor.class);
        doReturn(jpaEntityDescriptor).when(cachedType).getTypeDescriptor();
        doReturn(cachedType).when(typeResolver).resolve(eq("com.buschmais.jqassistant.plugin.jpa2.test.set.entity.JpaEntity"), eq(context));
        doReturn(typeResolver).when(context).peek(TypeResolver.class);
        doReturn(context).when(scanner).getContext();
        doReturn(fileDescriptor).when(context).peek(FileDescriptor.class);
        doReturn(persistenceDescriptor).when(store).addDescriptorType(fileDescriptor, PersistenceXmlDescriptor.class);
        doReturn(unitDescriptor).when(store).create(PersistenceUnitDescriptor.class);

        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        verify(persistenceDescriptor).setVersion(eq("2.0"));
    }

    @Test
    public void scannerFindsOnePersistenceUnitInPersistenceXMLV20() throws IOException {

        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        verify(persistenceUnitList).add(Mockito.any(PersistenceUnitDescriptor.class));
    }

    @Test
    public void scannerSetsCorrectNameForPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be on persistence unit.", persistenceUnitList, hasSize(1));

        verify(persistenceUnitList.get(0)).setName(eq("persistence-unit"));
    }

    @Test
    public void scannerSetcCorrectTransactionTypeForPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        assertThat(persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setTransactionType(eq("RESOURCE_LOCAL"));
    }

    @Test
    public void scannerSetsCorrectDescriptionFoundInPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setDescription(eq("description"));
    }

    @Test
    public void scannerSetsCorrectJTADataSourceFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setJtaDataSource(eq("jtaDataSource"));
    }

    @Test
    public void scannerSetsCorrectNonJTADataSourceFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setNonJtaDataSource(eq("nonJtaDataSource"));
    }

    @Test
    public void scannerSetsCorrectProviderFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setProvider(eq("provider"));
    }

    @Test
    public void scannerSetsCorrectValidationModeFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be one persistence unit.", persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setValidationMode(eq("AUTO"));
    }

    @Test
    public void scannerSetsCorrectSharedCacheModeFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        assertThat(persistenceUnitList, hasSize(1));
        verify(persistenceUnitList.get(0)).setValidationMode(eq("AUTO"));
    }

    @Test
    public void scannerAddsAllClasseseFromPersistenceUnitInPersistenceXMLV20() throws IOException {
        plugin.scan(item, path, JavaScope.CLASSPATH, scanner);

        assertThat("There must be unit persistence unit.", persistenceUnitList, hasSize(1));
        assertThat("There must be one JPA entity class.", persistenceUnitList.get(0).getContains(), hasSize(1));
        assertThat(persistenceUnitList.get(0).getContains(), hasItem(equalTo(cachedType.getTypeDescriptor())));
    }

}