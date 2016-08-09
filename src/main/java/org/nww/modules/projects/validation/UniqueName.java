/**
 * 
 */
package org.nww.modules.projects.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author mga
 */

@Documented
@Constraint(validatedBy = UniqueNameValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueName {
	String message() default "Es gibt bereits ein Projekt mit diesem Namen! Projektnamen müssen für einen Benutzer eindeutig sein.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
