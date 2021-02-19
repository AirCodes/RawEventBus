package cn.squatam.api.event;

import java.lang.annotation.*;

@Documented
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface EventMark {
     byte value() default Priority.MEDIUM;
}
