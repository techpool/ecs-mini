package com.pratilipi.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Validate {
	
	public boolean required() default false;
	public String requiredErrMsg() default "";
	
	public String regEx() default "";
	public String regExErrMsg() default "";

	public int minInt() default Integer.MIN_VALUE;
	
	public long minLong() default Long.MIN_VALUE;

}
