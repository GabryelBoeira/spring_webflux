package com.gabryel.task.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.base.DescribedPredicate.not;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameEndingWith;

public class ArchitectureTest {

    private static final String CONTROLLER = "com.gabryel.task.controller..";
    private static final String SERVICE = "com.gabryel.task.service..";
    private static final String REPOSITORY = "com.gabryel.task.repository..";
    private static final String ASPECT = "com.gabryel.task.aspect..";
    private static final String DTO = "com.gabryel.task.dto..";
    private static final String ENTITY = "com.gabryel.task.entity..";
    private static final String CONVERTER = "com.gabryel.task.converter..";
    private static final String EXCEPTION = "com.gabryel.task.exception..";
    private static final String CONFIGURATION = "com.gabryel.task.configuration..";
    private static final String CLIENT = "com.gabryel.task.client..";
    private static final String PRODUCER = "com.gabryel.task.producer..";
    private static final String CONSUMER = "com.gabryel.task.consumer..";

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.gabryel");


    @Test
    void controllersShouldHaveCorrectNaming() {
        ArchRuleDefinition.classes()
                .that().resideInAPackage(CONTROLLER)
                .should().haveSimpleNameEndingWith("Controller")
                .check(classes);
    }

    @Test
    void configurationsShouldHaveCorrectNaming() {
        ArchRuleDefinition.classes()
                .that().resideInAPackage(CONFIGURATION)
                .should().haveSimpleNameEndingWith("Configuration")
                .check(classes);
    }

    @Test
    void converterShouldHaveCorrectNaming() {
        ArchRuleDefinition.classes()
                .that().resideInAPackage(CONVERTER)
                .should().haveSimpleNameEndingWith("Converter")
                .check(classes);
    }

    @Test
    void servicesShouldHaveCorrectNaming() {
        ArchRuleDefinition.classes()
                .that().resideInAPackage(SERVICE)
                .should().haveSimpleNameEndingWith("Service")
                .check(classes);
    }

    @Test
    void repositoriesShouldHaveCorrectNaming() {
        ArchRuleDefinition.classes()
                .that().resideInAPackage(REPOSITORY)
                .should().haveSimpleNameEndingWith("Repository")
                .orShould().haveSimpleNameEndingWith("RepositoryCustom")
                .orShould().haveSimpleNameEndingWith("RepositoryCustomImpl")
                .check(classes);
    }

    @Test
    void aspectsShouldHaveCorrectNaming() {
        ArchRuleDefinition.classes()
                .that().resideInAPackage(ASPECT)
                .should().haveSimpleNameEndingWith("Aspect")
                .check(classes);
    }

    @Test
    void onlyServiceCanAccessRepository() {
        ArchRuleDefinition.noClasses()
                .that().resideOutsideOfPackages(SERVICE, REPOSITORY)
                .should().accessClassesThat().resideInAPackage(REPOSITORY)
                .check(classes);
    }

    @Test
    void onlyControllerAndServiceCanAccessDto() {
        ArchRuleDefinition.noClasses()
                .that().resideOutsideOfPackages(EXCEPTION, CONTROLLER, SERVICE, CONVERTER, DTO, CLIENT, REPOSITORY, PRODUCER, CONSUMER)
                .and().doNotHaveSimpleName("TaskUtils")
                .should().accessClassesThat().resideInAPackage(DTO)
                .check(classes);
    }

    @Test
    void onlyRepositoryCanAccessEntity() {
        ArchRuleDefinition.noClasses()
                .that().resideOutsideOfPackages(REPOSITORY, CONVERTER, SERVICE)
                .and(not(nameEndingWith("Test")))
                .should().accessClassesThat().resideInAPackage(ENTITY)
                .check(classes);
    }

    @Test
    void controllersShouldOnlyBeAccessedByExternalPackages() {
        ArchRuleDefinition.noClasses()
                .that().resideOutsideOfPackage(CONTROLLER)
                .should().accessClassesThat().resideInAPackage(CONTROLLER)
                .check(classes);
    }

    @Test
    void servicesShouldOnlyBeAccessedByControllerOrOtherServices() {
        ArchRuleDefinition.noClasses()
                .that().resideOutsideOfPackages(CONTROLLER, SERVICE)
                .should().accessClassesThat().resideInAPackage(SERVICE)
                .check(classes);
    }

    @Test
    void aspectsShouldOnlyBeAccessedInternally() {
        ArchRuleDefinition.noClasses()
                .that().resideOutsideOfPackage(ASPECT)
                .should().accessClassesThat().resideInAPackage(ASPECT)
                .check(classes);
    }
}
